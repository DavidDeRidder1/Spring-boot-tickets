package perform;

import org.springframework.web.reactive.function.client.WebClient;

import domain.Wedstrijd;
import reactor.core.publisher.Mono;

public class PerformRestWedstrijd {
	
	private final String SERVER_URI = "http://localhost:8080/rest";
	private WebClient webClient = WebClient.create();
	
	public PerformRestWedstrijd() throws Exception {
		
		for(long i = 1L; i <= 5L; i++) {
		
			System.out.println("\n------- GET overzicht wedstrijden van sport met id " + i);
			getAllWedstrijdenBySport(i);
		
		}
		
		System.out.println("\n------- GET aantal vrije plaatsen voor wedstrijd met id 1-------");
		System.out.println(getAantalVrijePlaatsenByWedstrijd(1L));
		
		System.out.println("\n------- GET aantal vrije plaatsen voor wedstrijd met id 2-------");
		System.out.println(getAantalVrijePlaatsenByWedstrijd(2L));
		
	}
	
	private void getAllWedstrijdenBySport(long id) {
		webClient.get().uri(SERVER_URI + "/wedstrijd/sport/" + id).retrieve()
        .bodyToFlux(Wedstrijd.class)
        .flatMap(wedstrijd -> {
            printWedstrijdData(wedstrijd);
            return Mono.empty();
        })
        .blockLast();
	}
	
	public int getAantalVrijePlaatsenByWedstrijd(long id) {
	    return webClient.get().uri(SERVER_URI + "/wedstrijd/" + id + "/aantalVrijePlaatsen").retrieve()
	        .bodyToMono(Integer.class)
	        .block();
	}

	private void printWedstrijdData(Wedstrijd wedstrijd) {
		System.out.printf("ID=%s, Discipline1=%s, Discipline2=%s, "
				+ "aantal vrije plaatsen=%d, olympisch nummer 1=%s, olympisch nummer 2=%s, ticketprijs=%.2f%n", 
				wedstrijd.getId(),
				wedstrijd.getDiscipline1(), wedstrijd.getDiscipline2(), wedstrijd.getAantalVrijePlaatsen(), 
				wedstrijd.getOlympischNr1(), wedstrijd.getOlympischNr2(), wedstrijd.getPrijsTicket());
	}

}
