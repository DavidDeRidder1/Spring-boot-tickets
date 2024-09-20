package com.springBoot_EWDJ_Project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import domain.MyUser;
import domain.Role;
import domain.Sport;
import domain.Stadium;
import domain.Ticket;
import domain.Wedstrijd;
import repository.SportRepository;
import repository.StadiumRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;

@Component
public class InitDataConfig implements CommandLineRunner {

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String BCRYPTED_PASSWORD = "$2a$12$JYQJAl6IMCyGKVUOGJbdlu8MV2kwRs7m2nlDUUUVhNSRbYLZkh2cS";
    //wachtwoord = 'paswoord'

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void run(String... args) {

        // Create users
        var user = MyUser.builder()
                .username("nameUser")
                .role(Role.USER)
                .password(BCRYPTED_PASSWORD)
                .build();
        var admin = MyUser.builder()
                .username("admin")
                .role(Role.ADMIN)
                .password(encoder.encode("admin"))
                .build();

        List<MyUser> userList = Arrays.asList(admin, user);

        userRepository.saveAll(userList);

        Sport voetbal = new Sport("Voetbal");
        Sport basketbal = new Sport("Basketbal");
        Sport tennis = new Sport("Tennis");
        Sport tafeltennis = new Sport("Tafeltennis");
        Sport atletiek = new Sport("Atletiek");

        voetbal = sportRepository.save(voetbal);
        basketbal = sportRepository.save(basketbal);
        tennis = sportRepository.save(tennis);
        tafeltennis = sportRepository.save(tafeltennis);
        atletiek = sportRepository.save(atletiek);

       
        Stadium stadium1 = new Stadium("Stadium 1");
        Stadium stadium2 = new Stadium("Stadium 2");
        Stadium stadium3 = new Stadium("Stadium 3");

        stadiumRepository.saveAll(Arrays.asList(stadium1, stadium2, stadium3));

        
        var wedstrijd1 = Wedstrijd.builder()
                .aantalVrijePlaatsen(20)
                .aanvangsuur(LocalTime.of(14, 0))
                .datum(LocalDate.of(2024, 8, 6))
                .discipline1("100m mannen")
                .discipline2(null)
                .prijsTicket((double) 50)
                .olympischNr1("50000")
                .olympischNr2("50500")
                .sport(atletiek)
                .stadium(stadium3)
                .build();

        var wedstrijd2 = Wedstrijd.builder()
                .aantalVrijePlaatsen(10)
                .aanvangsuur(LocalTime.NOON)
                .datum(LocalDate.of(2024, 7, 31))
                .discipline1("400m vrouwen")
                .discipline2(null)
                .prijsTicket((double) 100)
                .olympischNr1("10000")
                .olympischNr2("10500")
                .sport(atletiek)
                .stadium(stadium1)
                .build();

        var wedstrijd3 = Wedstrijd.builder()
                .aantalVrijePlaatsen(2)
                .aanvangsuur(LocalTime.NOON)
                .datum(LocalDate.of(2024, 7, 31))
                .discipline1("3 tegen 3 mannen")
                .discipline2(null)
                .prijsTicket((double) 100)
                .olympischNr1("10000")
                .olympischNr2("10500")
                .sport(basketbal)
                .stadium(stadium1)
                .build();
        
        var wedstrijd4 = Wedstrijd.builder()
                .aantalVrijePlaatsen(30)
                .aanvangsuur(LocalTime.NOON)
                .datum(LocalDate.of(2024, 8, 2))
                .discipline1("2 tegen 2 mannen")
                .discipline2(null)
                .prijsTicket((double) 80)
                .olympischNr1("10050")
                .olympischNr2("10500")
                .sport(tennis)
                .stadium(stadium3)
                .build();
        
        var wedstrijd5 = Wedstrijd.builder()
                .aantalVrijePlaatsen(5)
                .aanvangsuur(LocalTime.NOON)
                .datum(LocalDate.of(2024, 8, 3))
                .discipline1(null)
                .discipline2(null)
                .prijsTicket((double) 60)
                .olympischNr1("90500")
                .olympischNr2("89700")
                .sport(tafeltennis)
                .stadium(stadium2)
                .build();
        
        var wedstrijd6 = Wedstrijd.builder()
                .aantalVrijePlaatsen(42)
                .aanvangsuur(LocalTime.NOON)
                .datum(LocalDate.of(2024, 8, 5))
                .discipline1(null)
                .discipline2(null)
                .prijsTicket((double) 120)
                .olympischNr1("90600")
                .olympischNr2("89900")
                .sport(voetbal)
                .stadium(stadium1)
                .build();

        wedstrijdRepository.save(wedstrijd1);
        wedstrijdRepository.save(wedstrijd2);
        wedstrijdRepository.save(wedstrijd3);
        wedstrijdRepository.save(wedstrijd4);
        wedstrijdRepository.save(wedstrijd5);
        wedstrijdRepository.save(wedstrijd6);

        
        IntStream.range(0, 3).forEach(i -> {
            Ticket ticket = Ticket.builder()
                    .price(wedstrijd1.getPrijsTicket())
                    .wedstrijd(wedstrijd1)
                    .user(userRepository.findByUsername("nameUser"))
                    .build();
            ticketRepository.save(ticket);
        });

        IntStream.range(0, 2).forEach(i -> {
            Ticket ticket = Ticket.builder()
                    .price(wedstrijd2.getPrijsTicket())
                    .wedstrijd(wedstrijd2)
                    .user(userRepository.findByUsername("nameUser"))
                    .build();
            ticketRepository.save(ticket);
        });

        IntStream.range(0, 1).forEach(i -> {
            Ticket ticket = Ticket.builder()
                    .price(wedstrijd3.getPrijsTicket())
                    .wedstrijd(wedstrijd3)
                    .user(userRepository.findByUsername("nameUser"))
                    .build();
            ticketRepository.save(ticket);
        });
    }
}
