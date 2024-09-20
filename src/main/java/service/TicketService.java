package service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import domain.MyUser;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;
import repository.TicketRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;

@Service
public class TicketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    private MessageSource messageSource;

    public String purchaseTickets(Long userId, Long wedstrijdId, int ticketCount, String principalName, Model model) {
        MyUser user = userRepository.findByUsername(principalName);
        Wedstrijd wedstrijd = wedstrijdRepository.findById(wedstrijdId).orElseThrow(() -> new IllegalArgumentException("Ongeldig wedstrijd ID"));

        List<Ticket> userTickets = ticketRepository.findByUser(user);
        long totalUserTickets = userTickets.size();
        long userTicketsForWedstrijd = userTickets.stream().filter(t -> t.getWedstrijd().equals(wedstrijd)).count();

        Sport wedstrijdSport = wedstrijd.getSport();
        
        model.addAttribute("wedstrijd", wedstrijd);
        model.addAttribute("user", user);
        model.addAttribute("sportNaam", wedstrijdSport.getNaam());
        model.addAttribute("wedstrijdDatum", wedstrijd.getDatum());
        model.addAttribute("userId", user.getId());
        model.addAttribute("wedstrijdId", wedstrijd.getId());

        if (totalUserTickets + ticketCount > 100) {
            String errorMessage = messageSource.getMessage("tickets.total.limit", new Object[]{100}, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "koopTicket";
        }

        if (userTicketsForWedstrijd + ticketCount > 20) {
            String errorMessage = messageSource.getMessage("tickets.wedstrijd.limit", new Object[]{20}, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "koopTicket";
        }

        if (wedstrijd.getAantalVrijePlaatsen() < ticketCount) {
            String errorMessage = messageSource.getMessage("tickets.teWeinigVrijePlaatsen", new Object[]{wedstrijd.getAantalVrijePlaatsen()}, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "koopTicket";
        }

        wedstrijd.setAantalVrijePlaatsen(wedstrijd.getAantalVrijePlaatsen() - ticketCount);
        wedstrijdRepository.save(wedstrijd);

        IntStream.range(0, ticketCount).forEach(i -> {
            Ticket ticket = Ticket.builder()
                    .price(wedstrijd.getPrijsTicket())
                    .wedstrijd(wedstrijd)
                    .user(user)
                    .build();
            ticketRepository.save(ticket);
        });

        return "redirect:/tickets/" + userId;
    }
}

