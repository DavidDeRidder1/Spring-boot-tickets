package com.springBoot_EWDJ_Project;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import domain.MyUser;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;
import repository.TicketRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;
import service.TicketService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private TicketRepository ticketRepository;
	
	@Autowired
	private WedstrijdRepository wedstrijdRepository;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private TicketService ticketService;

	@GetMapping("/{userId}")
    public String getUserTickets(@PathVariable("userId") Long userId, Principal principal, Model model) {
        MyUser user = userRepository.findByUsername(principal.getName());

        List<Ticket> userTickets = ticketRepository.findByUserOrderBySportNameAndDatum(user);
        

        model.addAttribute("userTickets", userTickets);
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());

        return "tickets";
    }
    
    @GetMapping("/{userId}/koopTicket/{wedstrijdId}")
    public String showTicketPurchaseForm(@PathVariable("userId") Long userId,
                                         @PathVariable("wedstrijdId") Long wedstrijdId,
                                         Model model) {
        MyUser user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Wedstrijd wedstrijd = wedstrijdRepository.findById(wedstrijdId).orElseThrow(() -> new IllegalArgumentException("Invalid wedstrijd ID"));
        Sport wedstrijdSport = wedstrijd.getSport();
        model.addAttribute("user", user);
        model.addAttribute("wedstrijd", wedstrijd);
        model.addAttribute("sportNaam", wedstrijdSport.getNaam());
        model.addAttribute("wedstrijdDatum", wedstrijd.getDatum());
        model.addAttribute("userId", user.getId());
        model.addAttribute("wedstrijdId", wedstrijd.getId());

        return "koopTicket";
    }

    @PostMapping("/{userId}/koopTicket/{wedstrijdId}")
    public String purchaseTickets(@PathVariable("userId") Long userId,
                                  @PathVariable("wedstrijdId") Long wedstrijdId,
                                  @RequestParam("ticketCount") int ticketCount,
                                  Principal principal, Model model, RedirectAttributes redirectAttributes) {
    	String result = ticketService.purchaseTickets(userId, wedstrijdId, ticketCount, principal.getName(), model);
        if (result.startsWith("redirect:")) {
            redirectAttributes.addFlashAttribute("aantalTicketsGekocht", ticketCount);
        }
        return result;
    	
    }
}
