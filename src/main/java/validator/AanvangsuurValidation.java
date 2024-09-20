package validator;

import java.time.LocalTime;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class AanvangsuurValidation implements Validator {

	@Override
	public boolean supports(Class<?> klass) {
		return Wedstrijd.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		LocalTime aanvangsuur = wedstrijd.getAanvangsuur();
		
		LocalTime vroegsteAanvangsuur = LocalTime.of(8, 0); 
        LocalTime laatsteAanvangsuur = LocalTime.of(23, 0);
        
        if(aanvangsuur == null) {
        	errors.rejectValue("aanvangsuur", "aanvangsuur.Validation.Required", "Aanvangsuur is verplicht");
        	return;
        }
        
        if(aanvangsuur.isBefore(vroegsteAanvangsuur) || aanvangsuur.isAfter(laatsteAanvangsuur) ) {
        	errors.rejectValue("aanvangsuur", "aanvangsuur.Validation", new Object[] {8, 23}, "Aanvangsuur moet tussen 8u en 23u liggen");
        }
        
        
		
	}

}