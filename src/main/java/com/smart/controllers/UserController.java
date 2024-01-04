package com.smart.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repositories.ContactRepository;
import com.smart.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@ModelAttribute
	public void getUser(Model model, Principal principal) {
		String email = principal.getName();
		
		System.out.println(email);
		
		User user = userRepository.getUserByUserName(email);
		
		System.out.println(user);
		
		model.addAttribute("user", user);
	}

	@GetMapping("/user_dashboard")
	public String dashboard() throws IOException {
		System.out.println(new ClassPathResource("").getFile().getAbsolutePath());
		return "user/dashboard";
	}
	
	@GetMapping("/add_contact")
	public String newContact(Model model) {
		
		model.addAttribute("contact", new Contact());
		
		return "user/add_contact_form";
	}
	
	@PostMapping("/submit_contact")
	public String create_contact(@ModelAttribute("contact") Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal) throws IOException {
		System.out.println(contact);
		
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
//		setting image
//		if(!file.isEmpty()) {
//			contact.setImage(file.getOriginalFilename());
//			
//			File folder = new ClassPathResource("static/files").getFile();
//			
//			Path savePath = Paths.get(folder.getAbsolutePath()+File.separator+file.getOriginalFilename());
//			
//			Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
//		}
		
//		setting user
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepository.save(user);
		
		return "user/dashboard";
	}
	
	@GetMapping("/contacts/{page}")
	public String getAllUserContacts(Model model, Principal principal, @PathVariable("page") Integer page) {
		
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
//		pagination and sorting
		Pageable pageable = PageRequest.of(page, 2, Sort.by("name"));
		
		Page<Contact> userContacts = contactRepository.getUserContacts(user.getId(), pageable);
		
		System.out.println(userContacts);
		
		model.addAttribute("contacts", userContacts);
		model.addAttribute("totalPages", userContacts.getTotalPages());
		model.addAttribute("currentPage", page);
		
		return "user/view_contacts";
	}
	
	@GetMapping("/contact/{id}")
	public String seeContact(@PathVariable("id") Integer id, Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		
		Optional<Contact> contact = contactRepository.findById(id);
		
		if(user.getId() == contact.get().getUser().getId())
			model.addAttribute("contact", contact.get());
		
		return "user/see_contact";
	}
	
	@GetMapping("/contact/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id) {
		
//		Contact contact = contactRepository.findById(id).get();
//		contact.setUser(null);
//		contactRepository.delete(contact);
		contactRepository.deleteContactByIdCustom(id);
		
		return "redirect:/user/contacts/0";
	}
	
	@PostMapping("/contact/edit/{id}")
	public String editContact(@PathVariable("id") Integer id, Model model) {
		Contact contact = contactRepository.findById(id).get();
		model.addAttribute("contact", contact);
		
		return "user/edit_form";
	}
	
	@PostMapping("/update_contact")
	public String updateContact(@ModelAttribute("contact") Contact contact, Principal principal) {
		User user = userRepository.getUserByUserName(principal.getName());
		
		contact.setUser(user);
		contactRepository.save(contact);
		
		return "redirect:/user/contacts/0";
	}
	
}
