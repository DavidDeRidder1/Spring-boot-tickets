package com.springBoot.EWDJ_Project;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.springBoot_EWDJ_Project.EWDJProjectApplication;  
import com.springBoot_EWDJ_Project.SecurityConfig;

import domain.MyUser;
import domain.Role;
import repository.UserRepository;
@Import(SecurityConfig.class)
@SpringBootTest(classes = EWDJProjectApplication.class)
@AutoConfigureMockMvc
class SecurityEnSportTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MyUser normalUser = MyUser.builder()
        		.id(1L)
                .username("user")
                .password("password")
                .role(Role.USER)
                .tickets(Collections.emptyList())
                .build();
        
        MyUser adminUser = MyUser.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .tickets(Collections.emptyList())
                .build();
        

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        User user = new User(normalUser.getUsername(), normalUser.getPassword(), Collections.singletonList(authority));

        when(userService.loadUserByUsername("user")).thenReturn(user);
        when(userRepository.findByUsername("user")).thenReturn(normalUser);
    }

    @Test
    public void loginGet() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
        .andExpect(view().name("login"));
    }
     
    @Test
    public void accessDeniedPageGet() throws Exception {
        mockMvc.perform(get("/403"))
            .andExpect(status().isOk())
            .andExpect(view().name("403"));
    }
     
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/sport"))
            .andExpect(status().isOk())
            .andExpect(view().name("sport"))
            .andExpect(model().attributeExists("username"))
            .andExpect(model().attribute("username", "user"));
        }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAccessWithAdminRole() throws Exception {
    	
    	MyUser adminUser = MyUser.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .tickets(Collections.emptyList())
                .build();
        

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        User admin = new User(adminUser.getUsername(), adminUser.getPassword(), Collections.singletonList(authority));

        when(userService.loadUserByUsername("admin")).thenReturn(admin);
        when(userRepository.findByUsername("admin")).thenReturn(adminUser);
    	
        mockMvc.perform(get("/sport"))
        .andExpect(status().isOk())
        .andExpect(view().name("sport"))
        .andExpect(model().attributeExists("username"))
        .andExpect(model().attribute("username", "admin"));
    }

    @WithAnonymousUser
    @Test
    public void testNoAccessAnonymous() throws Exception {
        mockMvc.perform(get("/sport"))
                .andExpect(redirectedUrlPattern("**/login"));
    }
    }

