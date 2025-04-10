package com.billing.BillingSoftware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.model.Customer;
import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.repository.CustomerRepository;
import com.billing.BillingSoftware.repository.UserRepo;

import java.util.List;

@Service
public class CustomerService {

//	@Autowired
//    private  CustomerRepository customerRepository;
	
	@Autowired
	private UserRepo userRepo;

  

    public User addCustomer(User user) {
        return userRepo.save(user);
    }

    public User updateCustomer(Long id, User updatedCustomer) {
        return userRepo.findById(id).map(customer -> {
            customer.setUsername(updatedCustomer.getUsername());
            customer.setRole(updatedCustomer.getRole());
            customer.setPassword(updatedCustomer.getPassword());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setPhone(updatedCustomer.getPhone());
            return userRepo.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void deleteCustomer(Long id) {
        userRepo.deleteById(id);
    }
}
