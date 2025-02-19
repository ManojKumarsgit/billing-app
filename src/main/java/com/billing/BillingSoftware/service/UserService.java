package com.billing.BillingSoftware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

//    public User registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword())); 
//        return userRepository.save(user);
//    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
//        System.out.println(user.getUsername() + user.getPassword());
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return user;
//        }
        if(user != null && user.getPassword().equals(password)) {
        	return user;
        }
        return null;
    }
    public List<User> getAllCustomers() {
        return userRepository.findAll();
    }
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
    public User addCustomer(User user) {
        return userRepository.save(user);
    }

    public User updateCustomer(Long id, User updatedCustomer) {
        return userRepository.findById(id).map(customer -> {
            customer.setUsername(updatedCustomer.getUsername());
            customer.setRole(updatedCustomer.getRole());
            customer.setPassword(updatedCustomer.getPassword());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setPhone(updatedCustomer.getPhone());
            return userRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void deleteCustomer(Long id) {
    	userRepository.deleteById(id);
    }
}

