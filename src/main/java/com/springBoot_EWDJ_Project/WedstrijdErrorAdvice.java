package com.springBoot_EWDJ_Project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.SportNotFoundException;
import exceptions.WedstrijdNotFoundException;

@RestControllerAdvice
class WedstrijdErrorAdvice {
	
	  @ResponseBody
	  @ExceptionHandler(SportNotFoundException.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  String employeeNotFoundHandler(SportNotFoundException ex) {
	    return ex.getMessage();
	  }
	  
	  @ResponseBody
	  @ExceptionHandler(WedstrijdNotFoundException.class)
	  @ResponseStatus(HttpStatus.CONFLICT)
	  String duplicateEmployeeHandler(WedstrijdNotFoundException ex) {
	    return ex.getMessage();
	  }

}
