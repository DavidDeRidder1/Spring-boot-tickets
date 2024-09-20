package com.springBoot_EWDJ_Project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import domain.Sport;
import domain.Wedstrijd;
import exceptions.SportNotFoundException;
import exceptions.WedstrijdNotFoundException;
import repository.SportRepository;
import repository.WedstrijdRepository;

@RestController
@RequestMapping(value = "/rest")
public class WedstrijdRestController {
	
	@Autowired
	private WedstrijdRepository wedstrijdRepository;
	
	@Autowired
	private SportRepository sportRepository;
	
	@GetMapping(value = "/wedstrijd/sport/{id}")
	public List<Wedstrijd> getWedstrijdenBySport(@PathVariable("id") long sportId) {
		Sport sport = sportRepository.findById(sportId).orElseThrow(() -> new SportNotFoundException(sportId));
		return wedstrijdRepository.findBySportOrderByDatumDescAanvangsuurDesc(sport);
	}
	
	@GetMapping(value = "/wedstrijd/{id}/aantalVrijePlaatsen")
	public int getAantalVrijePlaatsenByWedstrijd(@PathVariable("id") long wedstrijdId) {
		Wedstrijd wedstrijd = wedstrijdRepository.findById(wedstrijdId).orElseThrow(() -> new WedstrijdNotFoundException(wedstrijdId));
		return wedstrijd.getAantalVrijePlaatsen();
	}
	
	

}
