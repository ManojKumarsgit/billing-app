package com.billing.BillingSoftware.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.billing.BillingSoftware.DTO.BillDetailDTO;
import com.billing.BillingSoftware.DTO.BillReportDTO;
import com.billing.BillingSoftware.DTO.BillRequest;
import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.model.BillDetails;
import com.billing.BillingSoftware.model.Product;
import com.billing.BillingSoftware.repository.BillDetailsRepository;
import com.billing.BillingSoftware.repository.BillReportRepository;
import com.billing.BillingSoftware.repository.BillRepository;
import com.billing.BillingSoftware.repository.ProductRepository;
import com.billing.BillingSoftware.service.BillService;
import com.billing.BillingSoftware.service.GenerateBill;



@Controller
@RequestMapping("/billing")
public class BillingController {
	 @Autowired
	    private ProductRepository productRepository;
	
	 @Autowired
	private GenerateBill generateBill;
	 
	 @Autowired
	 private BillService billService;
	
	 @Autowired
	 private BillReportRepository billReportRepository;
	 
	 @Autowired
	 private BillRepository billRepository;
	 
	 @Autowired
	 private BillDetailsRepository billDetailsRepository;
	 
	 private List<Product> productList;
	
	 @GetMapping
	    public String showBillingPage(Model model) {
		 	productList = new ArrayList<>();
	        model.addAttribute("products", productList); // Use a list instead of a single product
	        
	        return "billing";
	    }

	    @PostMapping("/add")
	    public String searchProduct(@RequestParam("productid") Long productId, Model model) {
	        Product product = productRepository.findById(productId).orElse(null);
	        
	        if (product != null) {
	            productList.add(product); // Add to the list instead of replacing
	        } else {
	            model.addAttribute("error", "Product Not Found");
	        }
	        
	        model.addAttribute("products", productList); // Update model with the list
	        return "billing";
	    }

	    @PostMapping("/save-bill")
	    public ResponseEntity<byte[]> saveBill(@RequestBody BillRequest billRequest) {
	    	
	    	System.out.println(billRequest);

	        try {
	            // 1. Save to database
	            Bill bill = new Bill();
	            bill.setCusPhone(billRequest.getCusPhone());
	            bill.setBillingDate(LocalDate.now());

	            List<BillDetails> detailsList = billRequest.getBillDetails().stream()
	                .map(dto -> {
	                    BillDetails detail = new BillDetails();
	                    Product product = productRepository.findByName(dto.getItem());
	                    if (product != null) {
	    	                product.setStock(product.getStock() - dto.getQuantity());
	    	                productRepository.save(product);
	    	            }
	                    detail.setItem(dto.getItem());
	                    detail.setMrp(dto.getMrp());
	                    detail.setQuantity(dto.getQuantity());
	                    detail.setTotal(dto.getTotal());
	                    detail.setBill(bill);
	                    return detail;
	                })
	                .collect(Collectors.toList());

	            bill.setBillDetails(detailsList);
	            billRepository.save(bill);
	            
//	            Product product = productRepository.findByName(dto.getBillDetails());
	          

	            // 2. Convert to BillReportDTO
	         
	            List<BillReportDTO> reportDTOs = detailsList.stream()
	                .map(detail -> new BillReportDTO(
	                    bill.getBillNo(),
	                    bill.getCusPhone(),
	                    bill.getBillingDate(),
	                    detail.getItem(),
	                    detail.getMrp(),
	                    detail.getQuantity(),
	                    detail.getTotal()
	                ))
	                .collect(Collectors.toList());
	            
	           
	            
	            System.out.println(billRequest.getGst());
	            System.out.println(billRequest.getDiscount());

	            // 3. Generate PDF
	            byte[] pdfBytes = generateBill.generateBillPDF(
	                reportDTOs, 
	                billRequest.getGst(), 
	                billRequest.getDiscount()
	            );
	            
	     
	            
	        

	            // 4. Return PDF response
	            return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=bill.pdf")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(pdfBytes);

	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Error: " + e.getMessage()).getBytes());
	        }
	    }
}
