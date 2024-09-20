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
import repository.SportRepository;
import repository.StadiumRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.WedstrijdRepository;
import service.TicketService;
import validator.AanvangsuurValidation;
import validator.DatumValidation;
import validator.DisciplineValidation;
import validator.OlympischNr1Validation;
import validator.OlympischNr2Validation;

@Import(SecurityConfig.class)
@SpringBootTest(classes = EWDJProjectApplication.class)
@AutoConfigureMockMvc
class WedstrijdControllerTests {

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
    public void testAccessWithUserRoleWedstrijdenList() throws Exception {
        mockMvc.perform(get("/wedstrijden/sport/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("wedstrijden"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", normalUser))
            .andExpect(model().attributeExists("sport"))
            .andExpect(model().attributeExists("wedstrijden"))
            .andExpect(model().attributeExists("role"))
            .andExpect(model().attribute("role", normalUser.getRole()));
    }
    
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAccessWithAdminRoleWedstrijdenList() throws Exception {
        mockMvc.perform(get("/wedstrijden/sport/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("wedstrijden"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", adminUser))
            .andExpect(model().attributeExists("sport"))
            .andExpect(model().attributeExists("wedstrijden"))
            .andExpect(model().attributeExists("role"))
            .andExpect(model().attribute("role", adminUser.getRole()));
    }
    
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAccessWithAdminRoleVoegToeForm() throws Exception {
        mockMvc.perform(get("/wedstrijden/sport/1/voegToe"))
            .andExpect(status().isOk())
            .andExpect(view().name("voegToe"))
            .andExpect(model().attributeExists("wedstrijd"))
            .andExpect(model().attributeExists("sportId"))
            .andExpect(model().attributeExists("stadiums"));
    }
    
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testNoAccessWithUserRoleVoegToeForm() throws Exception {
        mockMvc.perform(get("/wedstrijden/sport/1/voegToe"))
            .andExpect(status().isForbidden());
    }
    
    

    
}
