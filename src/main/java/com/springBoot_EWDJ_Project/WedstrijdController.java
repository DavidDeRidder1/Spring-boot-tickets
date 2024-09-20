package com.springBoot_EWDJ_Project;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.MyUser;
import domain.Sport;
import domain.Stadium;
import domain.Wedstrijd;
import jakarta.validation.Valid;
import repository.SportRepository;
import repository.StadiumRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;
import validator.AanvangsuurValidation;
import validator.DatumValidation;
import validator.DisciplineValidation;
import validator.OlympischNr1Validation;
import validator.OlympischNr2Validation;

@Controller
@RequestMapping("/wedstrijden")
public class WedstrijdController {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private WedstrijdRepository wedstrijdRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StadiumRepository stadiumRepository;
    
    @Autowired
    private DisciplineValidation disciplineValidation;
    
    @Autowired
    private DatumValidation datumValidation;
    
    @Autowired
    private AanvangsuurValidation aanvangsuurValidation;
    
    @Autowired
    private OlympischNr1Validation olympischNr1Validation;
    
    @Autowired
    private OlympischNr2Validation olympischNr2Validation;

    @GetMapping("/sport/{sportId}")
    public String getWedstrijdenForSport(@PathVariable("sportId") Long sportId, Model model, Principal principal) {
    	
        Sport sport = sportRepository.findById(sportId).orElse(null);
        if (sport == null) {
            
            return "403"; 
        }

        
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findBySportOrderByDatumDescAanvangsuurDesc(sport);
        
        MyUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("sport", sport);
        model.addAttribute("wedstrijden", wedstrijden);
        model.addAttribute("role", user.getRole());

       
        return "wedstrijden";
    }
    
    @GetMapping("/sport/{sportId}/voegToe")
    public String showWedstrijdForm(@PathVariable("sportId") Long sportId, Model model) {
        
    	Wedstrijd wedstrijd = new Wedstrijd();
        model.addAttribute("wedstrijd", wedstrijd);
        model.addAttribute("sportId", sportId); 
        model.addAttribute("stadiums", stadiumRepository.findAll()); 
        return "voegToe"; 
    }

    @PostMapping("/sport/{sportId}/voegToe")
    public String addWedstrijd(@PathVariable("sportId") Long sportId, @ModelAttribute("wedstrijd") @Valid Wedstrijd wedstrijd, BindingResult result, Model model) {
    	
    	disciplineValidation.validate(wedstrijd, result);
    	datumValidation.validate(wedstrijd, result);
    	aanvangsuurValidation.validate(wedstrijd, result);
    	olympischNr1Validation.validate(wedstrijd, result);
    	olympischNr2Validation.validate(wedstrijd, result);
    	
    	if (result.hasErrors()) {
            model.addAttribute("stadiums", stadiumRepository.findAll()); 
            model.addAttribute("sportId", sportId); 
            return "voegToe";
        }
    	
    	Optional<Stadium> stadium = stadiumRepository.findById(wedstrijd.getHulpStadiumId());
        if(stadium.isEmpty()) {
        	System.out.println(stadium);
        }
    	wedstrijd.setStadium(stadium.get());
        Sport sport = sportRepository.findById(sportId).orElse(null);
        if (sport == null) {
            
            return "403"; 
        }
        wedstrijd.setSport(sport);
        
        
        wedstrijdRepository.save(wedstrijd);
        
        
        return "redirect:/wedstrijden/sport/" + sportId;
    }

}

