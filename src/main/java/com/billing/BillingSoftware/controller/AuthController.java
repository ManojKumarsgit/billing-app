package com.billing.BillingSoftware.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.service.UserService;


@Controller
@RequestMapping("/auth")
@SessionAttributes("loggedInUser")
public class AuthController {
	 @Autowired
	    private UserService userService;

	    @GetMapping("/login")
	    public String showLoginPage(Model model) {
	        model.addAttribute("user", new User());
	        return "login";
	    }

	    @PostMapping("/login")
	    public String login(@ModelAttribute User user, Model model) {
	        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
	        if (authenticatedUser != null) {
	            model.addAttribute("loggedInUser", authenticatedUser);
	            return "redirect:/dashboard";
	        } else {
	            model.addAttribute("error", "Invalid username or password!");
	            return "login";
	        }
	    }

//	    @GetMapping("/register")
//	    public String showRegistrationPage(Model model) {
//	        model.addAttribute("user", new User());
//	        return "register";
//	    }
//
//	    @PostMapping("/register")
//	    public String register(@ModelAttribute User user, Model model) {
//	        if (userService.findByUsername(user.getUsername()).isPresent()) {
//	            model.addAttribute("error", "Username already exists!");
//	            return "register";
//	        }
//	        user.setRole("USER"); // Default role is "USER"
//	        userService.registerUser(user);
//	        return "redirect:/auth/login";
//	    }

	    @GetMapping("/logout")
	    public String logout(SessionStatus status) {
	        status.setComplete();
	        return "redirect:/auth/login";
	    }
}

