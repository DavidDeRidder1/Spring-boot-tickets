package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class OlympischNr2Validation implements Validator {

	@Override
	public boolean supports(Class<?> klass) {
		return Wedstrijd.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		String olympischNr1 = wedstrijd.getOlympischNr1();
		String olympischNr2 = wedstrijd.getOlympischNr2();
		
		if(olympischNr1.isBlank() || olympischNr1 == null || olympischNr2.isBlank() || olympischNr2 == null) {
			
			return;
		}
		
		
		int nr1 = Integer.parseInt(olympischNr1);
		int nr2 = Integer.parseInt(olympischNr2);
		
		if(nr2 < (nr1 - 1000) || nr2 > (nr1 + 1000)) {
			errors.rejectValue("olympischNr2", "olympischNr2.Validation", new Object[] {1000}, "Olympisch nr 2 moet binnen een bepaalde range zijn");
			
		}
		
		
		
	}

}
