package validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;
import repository.WedstrijdRepository;

public class OlympischNr1Validation implements Validator {
	
	@Autowired
	private WedstrijdRepository wedstrijdRepository;
	
	@Override
	public boolean supports(Class<?> klass) {
		return Wedstrijd.class.isAssignableFrom(klass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		String olympischNr1 = wedstrijd.getOlympischNr1();
		
		if(olympischNr1.isBlank() || olympischNr1.isEmpty() || olympischNr1 == null) {
			errors.rejectValue(olympischNr1, "NotNull");
		}
		
		if (!olympischNr1.matches("\\d{5}")) {
            errors.rejectValue("olympischNr1", "olympischNr1.Validation.Lengte", new Object[] {5}, "Olympisch nummer 1 moet exact 5 cijfers bevatten");
            return;
        }
		
		if (olympischNr1.charAt(0) == '0') {
            errors.rejectValue("olympischNr1", "olympischNr1.Validation.EersteCijfer", new Object[] {0}, "Het eerste cijfer van olympisch nummer 1 mag niet 0 zijn");
            return;
        }
		
		if (olympischNr1.charAt(0) == olympischNr1.charAt(4)) {
            errors.rejectValue("olympischNr1", "olympischNr1.Validation.EersteLaatsteGelijk", "Het eerste en vijfde cijfer van olympisch nummer 1 mogen niet hetzelfde zijn");
            return;
        }
		
		
		List<Wedstrijd> wedstrijden = new ArrayList<>();
		wedstrijdRepository.findAll().forEach(wedstrijden::add);
		for (Wedstrijd w : wedstrijden) {
            if (w.getOlympischNr1().equals(olympischNr1)) {
                errors.rejectValue("olympischNr1", "olympischNr1.Validation.Uniek", "Olympisch nummer 1 moet uniek zijn");
                break;
            }
        }
	}

}
