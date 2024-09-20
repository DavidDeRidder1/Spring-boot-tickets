package com.springBoot_EWDJ_Project;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import perform.PerformRestWedstrijd;
import service.MyUserDetailsService;
import service.TicketService;
import validator.AanvangsuurValidation;
import validator.DatumValidation;
import validator.DisciplineValidation;
import validator.OlympischNr1Validation;
import validator.OlympischNr2Validation;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class EWDJProjectApplication implements WebMvcConfigurer{

	/*
	 * Inloggegevens:
	 * User: 
	 *   -gebruikersnaam: nameUser
	 *   -wachtwoord: paswoord
	 * Admin:
	 *   -gebruikersnaam: admin
	 *   -wachtwoord: admin
	 *  
	 */
	
	public static void main(String[] args) {
		SpringApplication.run(EWDJProjectApplication.class, args);
		
		try {
			new PerformRestWedstrijd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
	   registry.addRedirectViewController("/", "/sport");
	   registry.addViewController("/403").setViewName("403");
    }

	@Bean
	UserDetailsService myUserDetailsService() {
		return new MyUserDetailsService();
	}
	
	@Bean
	LocaleResolver localeResolver() {
	    CookieLocaleResolver slr = new CookieLocaleResolver();
	    slr.setDefaultLocale(Locale.ENGLISH);
	    return slr;
	}
	
	@Bean
	DisciplineValidation disciplineValidation() {
		return new DisciplineValidation();
	}
	
	@Bean
	DatumValidation datumValidation() {
		return new DatumValidation();
	}
	
	@Bean
	AanvangsuurValidation aanvangsuurValidation() {
		return new AanvangsuurValidation();
	}
	
	@Bean
	OlympischNr1Validation olympischNr1Validation() {
		return new OlympischNr1Validation();
	}
	
	@Bean
	OlympischNr2Validation olympischNr2Validation() {
		return new OlympischNr2Validation();
	}
	
	@Bean
	TicketService ticketService() {
		return new TicketService();
	}

}
