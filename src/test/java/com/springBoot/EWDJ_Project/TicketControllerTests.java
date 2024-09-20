package com.springBoot.EWDJ_Project;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import com.springBoot_EWDJ_Project.EWDJProjectApplication;
import com.springBoot_EWDJ_Project.SecurityConfig;

import domain.MyUser;
import domain.Role;
import domain.Sport;
import domain.Stadium;
import domain.Ticket;
import domain.Wedstrijd;
import repository.TicketRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;
import service.TicketService;

@Import(SecurityConfig.class)
@SpringBootTest(classes = EWDJProjectApplication.class)
@AutoConfigureMockMvc
class TicketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private WedstrijdRepository wedstrijdRepository;

    private MyUser normalUser;
    private MyUser adminUser;
    private Sport sport;
    private Stadium stadium;
    private Wedstrijd wedstrijd;

    @BeforeEach
    public void setup() {
        normalUser = MyUser.builder()
            .id(1L)
            .username("user")
            .password("password")
            .role(Role.USER)
            .tickets(Collections.emptyList())
            .build();

        adminUser = MyUser.builder()
            .id(2L)
            .username("admin")
            .password("admin")
            .role(Role.ADMIN)
            .tickets(Collections.emptyList())
            .build();

        sport = Sport.builder()
            .id(1L)
            .naam("Soccer")
            .build();

        stadium = Stadium.builder()
            .id(1L)
            .naam("Main Stadium")
            .build();

        wedstrijd = Wedstrijd.builder()
            .id(1L)
            .sport(sport)
            .datum(LocalDate.of(2024, 5, 20))
            .aanvangsuur(LocalTime.of(18, 0))
            .stadium(stadium)
            .aantalVrijePlaatsen(50)
            .olympischNr1("100m")
            .olympischNr2("200m")
            .prijsTicket(20.0)
            .build();

        when(userService.loadUserByUsername("user")).thenReturn(new User(normalUser.getUsername(), normalUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        when(userService.loadUserByUsername("admin")).thenReturn(new User(adminUser.getUsername(), adminUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        when(userRepository.findByUsername("user")).thenReturn(normalUser);
        when(userRepository.findByUsername("admin")).thenReturn(adminUser);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessWithUserRoleTickets() throws Exception {
        mockMvc.perform(get("/tickets/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("tickets"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", normalUser))
            .andExpect(model().attributeExists("userTickets"))
            .andExpect(model().attribute("userTickets", ticketRepository.findByUserOrderBySportNameAndDatum(normalUser)))
            .andExpect(model().attributeExists("role"))
            .andExpect(model().attribute("role", normalUser.getRole()));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testNoAccessWithAdminRoleTickets() throws Exception {
        mockMvc.perform(get("/tickets/2"))
            .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessWithUserRoleKoopTicket() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(normalUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));

        mockMvc.perform(get("/tickets/1/koopTicket/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("koopTicket"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", normalUser))
            .andExpect(model().attributeExists("wedstrijd"))
            .andExpect(model().attribute("wedstrijd", wedstrijd));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testNoAccessWithAdminRoleKoopTicket() throws Exception {
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(adminUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));

        mockMvc.perform(get("/tickets/2/koopTicket/1"))
            .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testSuccessfulTicketPurchase() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(normalUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));
        when(ticketService.purchaseTickets(1L, 1L, 2, "user", new ExtendedModelMap())).thenReturn("redirect:/tickets/1");

        mockMvc.perform(post("/tickets/1/koopTicket/1")
                .param("ticketCount", "2")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tickets/1")) 
            .andExpect(flash().attribute("aantalTicketsGekocht", 2));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testExceedTotalTicketLimit() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(normalUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));
        
        
        //User heeft op deze manier 60 tickets, als hij er 40 of meer extra koopt zal dit niet lukken
        List<Ticket> tickets = IntStream.range(0, 60)
                .mapToObj(i -> Ticket.builder().user(normalUser).wedstrijd(wedstrijd).build())
                .collect(Collectors.toList());

        when(ticketRepository.findByUser(normalUser)).thenReturn(tickets);
        when(ticketService.purchaseTickets(1L, 1L, 50, "user", new ExtendedModelMap())).thenReturn("koopTicket");

        mockMvc.perform(post("/tickets/1/koopTicket/1")
                .param("ticketCount", "50")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("koopTicket"));
            
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testExceedPerWedstrijdLimit() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(normalUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));

        //User heeft op deze manier 20 tickets van 1 bepaalde sport, 1 ticket erbij is te veel
        List<Ticket> tickets = IntStream.range(0, 20)
                .mapToObj(i -> Ticket.builder().user(normalUser).wedstrijd(wedstrijd).build())
                .collect(Collectors.toList());

        when(ticketRepository.findByUser(normalUser)).thenReturn(tickets);
        when(ticketService.purchaseTickets(1L, 1L, 1, "user", new ExtendedModelMap())).thenReturn("koopTicket");

        mockMvc.perform(post("/tickets/1/koopTicket/1")
                .param("ticketCount", "1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("koopTicket"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testInsufficientAvailablePlaces() throws Exception {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(normalUser));
        when(wedstrijdRepository.findById(1L)).thenReturn(java.util.Optional.of(wedstrijd));

        wedstrijd.setAantalVrijePlaatsen(0);
        when(ticketService.purchaseTickets(1L, 1L, 1, "user", new ExtendedModelMap())).thenReturn("koopTicket");

        mockMvc.perform(post("/tickets/1/koopTicket/1")
                .param("ticketCount", "1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("koopTicket"));
     }
}
