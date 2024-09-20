package com.springBoot_EWDJ_Project;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.MyUser;
import repository.SportRepository;
import repository.UserRepository;

@Controller
@RequestMapping("/sport")
public class SportController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SportRepository sportRepository;

	@GetMapping
	public String listGuest(Model model, Principal principal) {

		MyUser user = userRepository.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("role", user.getRole());
		model.addAttribute("username", principal.getName());
		
		model.addAttribute("sportList", sportRepository.findAll());
		model.addAttribute("aantalTicketsUser", user.getTickets().size());
		return "sport";
	}
}
