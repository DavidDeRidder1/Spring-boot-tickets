package validator;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class DatumValidation implements Validator {
	
	@Override
	public boolean supports(Class<?> klass) {
		return Wedstrijd.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		LocalDate datum = wedstrijd.getDatum();
		
		LocalDate startDate = LocalDate.of(2024, 7, 26);
        LocalDate endDate = LocalDate.of(2024, 8, 11);
        
        if(datum == null) {
        	errors.rejectValue("datum", "datum.Validation.Required", "Datum is verplicht");
        	return;
        }
		
		if(datum.isBefore(startDate) || datum.isAfter(endDate)) {
			errors.rejectValue("datum", "datum.Validation", new Object[] {26, 11}, "Datum moet tussen 26 juli en 11 augustus liggen");
        }
		
	}

}
