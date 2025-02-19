package com.billing.BillingSoftware.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.billing.BillingSoftware.DTO.BillReportDTO;
import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.model.BillDetails;
import com.billing.BillingSoftware.model.Product;
import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.repository.BillDetailsRepository;
import com.billing.BillingSoftware.repository.BillRepository;
import com.billing.BillingSoftware.repository.ProductRepository;
import com.billing.BillingSoftware.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	 @Autowired
	    private ProductService productService;
	 
	 @Autowired
	 private BillRepository billRepository;

	 @Autowired
	 private BillDetailsRepository billDetailsRepository;
	 
	 @Autowired
	 private ProductRepository productRepository;
	 
	    @GetMapping
	    public String listProducts(Model model) {
	        model.addAttribute("products", productService.getAllProducts());
	        return "product-list";
	    }

	@GetMapping("/add")
	public String showAddProductForm(Model model, @SessionAttribute(name = "loggedInUser", required = false) User loggedInUser) {
	    if (loggedInUser == null || !loggedInUser.getRole().equals("ADMIN")) {
	        return "redirect:/auth/login";
	    }
	    model.addAttribute("product", new Product());
	    return "product-form";
	}
	
	 @PostMapping("/add")
	    public String saveProduct(@ModelAttribute Product product) {
	        productService.saveProduct(product);
	        return "redirect:/products";
	    }
	 
	 @GetMapping("/edit/{id}")
	    public String showEditProductForm(@PathVariable Long id, Model model) {
	        model.addAttribute("product", productService.getAllProducts().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
	        return "product-form";
	    }
	 
	 @PostMapping("/edit/{id}")
	    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
	        productService.updateProduct(id, product);
	        return "redirect:/products";
	    }

	    @GetMapping("/delete/{id}")
	    public String deleteProduct(@PathVariable Long id) {
	        productService.deleteProduct(id);
	        return "redirect:/products";
	    }
	    
	    @GetMapping("/return-products")
	    public String viewReturnPage(Model model) {
	    	 model.addAttribute("billList", new ArrayList<>()); 
	    	return "product-return";
	    }
	    
	    @PostMapping("/returns-search")
	    public String searchBill(
	            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	            @RequestParam("phone") Long phone,
	            Model model) {

	        List<Bill> bills = billRepository.findByBillingDateAndCusPhone(date, phone);

	        List<BillReportDTO> billList = bills.stream()
	                .flatMap(bill -> bill.getBillDetails().stream()
	                        .map(detail -> new BillReportDTO(
	                                bill.getBillNo(),
	                                bill.getCusPhone(),
	                                bill.getBillingDate(),
	                                detail.getId(), // Use BillDetail ID for returning item
	                                detail.getItem(),
	                                detail.getMrp(),
	                                detail.getQuantity(),
	                                detail.getTotal()
	                        )))
	                .collect(Collectors.toList());

	        model.addAttribute("billList", billList);
	        return "product-return"; // Re-loads the page with results
	    }
	    
	    @PostMapping("/return/{id}")
	    public String returnItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        Optional<BillDetails> billDetailOpt = billDetailsRepository.findById(id);

	        if (billDetailOpt.isPresent()) {
	            BillDetails billDetail = billDetailOpt.get();

	            // Reduce stock from Product table
	            Product product = productRepository.findByName(billDetail.getItem());
	            if (product != null) {
	                product.setStock(product.getStock() + billDetail.getQuantity());
	                productRepository.save(product);
	            }
	            
	            Bill bill = billDetail.getBill();
	            billDetailsRepository.deleteById(id);

	            int remainingItems = billDetailsRepository.countBillDetailsByBill(bill);
	            if (remainingItems == 0) {
	                billRepository.delete(bill);
	            }
//	            billDetailsRepository.save(billDetail);

	            redirectAttributes.addFlashAttribute("successMessage", "Item returned successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Bill detail not found!");
	        }

	        return "redirect:/products/return-products"; // Redirect to return page
	    }


}
