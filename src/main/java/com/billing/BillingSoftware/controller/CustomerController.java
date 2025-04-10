package com.billing.BillingSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.billing.BillingSoftware.model.Customer;
import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.service.UserService;

@Controller
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
    private  UserService userService;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", userService.getAllCustomers());
        return "customers";
    }

    @GetMapping("/add")
    public String showAddCustomerForm(@SessionAttribute(name = "loggedInUser", required = false) User loggedInUser, Model model) {
        if (loggedInUser == null || !loggedInUser.getRole().equals("ADMIN")) {
            return "redirect:/auth/login";
        }
        model.addAttribute("customer", new User());
        return "customer-form";
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute User user) {
    	userService.addCustomer(user);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", userService.getAllCustomers().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null));
        return "customer-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute User customer) {
    	userService.updateCustomer(id, customer);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
    	userService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
