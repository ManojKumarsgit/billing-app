package com.billing.BillingSoftware.controller;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.repository.BillDetailsRepository;
import com.billing.BillingSoftware.repository.BillRepository;

@Controller

public class DashboardController {
	
	@Autowired
	private BillRepository billRepository;
	
	@Autowired
	private BillDetailsRepository billDetailsRepository;
	
    @GetMapping("/dashboard")
    public String showDashboard(@SessionAttribute(name = "loggedInUser", required = false) User loggedInUser, Model model) {
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }
        final DecimalFormat decfor = new DecimalFormat("0.00");  
        double totalSales = Double.parseDouble(decfor.format(billDetailsRepository.getTotalSales()));
        
        long totalBills = billRepository.count();
        long totalCustomers = billRepository.countDistinctCustomers();
        
        List<Bill> recentBills = billRepository.findTop5ByOrderByBillingDateDesc();
        List<Map<String, Object>> recentBillDetails = new ArrayList<>();

        recentBills.forEach(bill -> {
            Map<String, Object> map = new HashMap<>();
            map.put("billNo", bill.getBillNo());
            map.put("cusPhone", bill.getCusPhone());
            map.put("billingDate", bill.getBillingDate());

            // Convert int to Long and fetch total amount
            Long billNoLong = (long) bill.getBillNo();
            Double totalAmount = billDetailsRepository.getBillTotal(billNoLong);
            map.put("totalAmount", totalAmount != null ? totalAmount : 0.0);

            recentBillDetails.add(map);
        });

     


        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalBills", totalBills);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("recentBills", recentBillDetails);
        
        model.addAttribute("role", loggedInUser.getRole());
        model.addAttribute("username", loggedInUser.getUsername());
        return "dashboard";
    }   
}

