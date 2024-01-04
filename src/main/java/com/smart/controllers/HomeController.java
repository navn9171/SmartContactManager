package com.smart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.entities.User;
import com.smart.helpers.Message;
import com.smart.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/register")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/do_register")
	public String userRegistration(@ModelAttribute("user") User user, Model model, HttpSession session) {
		try {
			System.out.println(user);
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			User result = userRepository.save(user);
			System.out.println(result);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("User Registered Successfully!", "alert-success"));
			return "register";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!", "alert-danger"));
			return "register";
		}
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
}
