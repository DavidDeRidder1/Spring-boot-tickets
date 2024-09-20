package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class DisciplineValidation implements Validator {

	@Override
	public boolean supports(Class<?> klass) {
		return Wedstrijd.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		String discipline1 = wedstrijd.getDiscipline1();
		String discipline2 = wedstrijd.getDiscipline2();
		
		if (discipline1 != null && !discipline1.isBlank() &&
        discipline2 != null && !discipline2.isBlank() &&
        discipline1.equals(discipline2)) {
			errors.rejectValue("discipline1", "discipline.Validation", "Disciplines mogen niet gelijk zijn");
        }
		
	}

}
