package com.springBoot.EWDJ_Project;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.springBoot_EWDJ_Project.EWDJProjectApplication;
import com.springBoot_EWDJ_Project.SecurityConfig;
import com.springBoot_EWDJ_Project.WedstrijdRestController;

import domain.Sport;
import domain.Stadium;
import domain.Wedstrijd;
import repository.SportRepository;
import repository.WedstrijdRepository;

@Import(SecurityConfig.class)
@SpringBootTest(classes = EWDJProjectApplication.class)
@AutoConfigureMockMvc
class WedstrijdRestMockTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private WedstrijdRepository wedstrijdRepository;
	
	@MockBean
    private SportRepository sportRepository;
	
	private WedstrijdRestController controller;
	
	private Sport sport;
	
	private Wedstrijd wedstrijd;
	
	private Stadium stadium;
	
	@BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new WedstrijdRestController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        ReflectionTestUtils.setField(controller, "wedstrijdRepository", wedstrijdRepository);
        ReflectionTestUtils.setField(controller, "sportRepository", sportRepository);

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
                .build();
    }

    @Test
    public void testGetWedstrijdenBySport_emptyList() throws Exception {
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(wedstrijdRepository.findBySportOrderByDatumDescAanvangsuurDesc(sport)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/wedstrijd/sport/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetWedstrijdenBySport_withResults() throws Exception {
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(wedstrijdRepository.findBySportOrderByDatumDescAanvangsuurDesc(sport)).thenReturn(Arrays.asList(wedstrijd));

        mockMvc.perform(get("/rest/wedstrijd/sport/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(wedstrijd.getId()))
                .andExpect(jsonPath("$[0].sport.naam").value(sport.getNaam()));
    }

    @Test
    public void testGetAantalVrijePlaatsenByWedstrijd() throws Exception {
        when(wedstrijdRepository.findById(1L)).thenReturn(Optional.of(wedstrijd));

        mockMvc.perform(get("/rest/wedstrijd/1/aantalVrijePlaatsen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(wedstrijd.getAantalVrijePlaatsen()));
    }

    @Test
    public void testGetAantalVrijePlaatsenByWedstrijd_notFound() throws Exception {
        when(wedstrijdRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rest/wedstrijd/1/aantalVrijePlaatsen"))
                .andExpect(status().isNotFound());
    }

}
