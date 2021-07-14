package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.CustomerManager;
import com.pz.KKBus.Manager.MailManager;
import com.pz.KKBus.Manager.ReservationManager;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Enums.Role;
import com.pz.KKBus.Model.Enums.Route;
import com.pz.KKBus.Model.Enums.Status;
import com.pz.KKBus.Model.Repositories.ReservationRepo;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationManager reservationManager;

    @MockBean
    private CustomerManager customerManager;

    @MockBean
    private MailManager mailManager;

    @MockBean
    private KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;

    @MockBean
    private KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReservation() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation((long) 1, LocalDate.parse("2020-06-26"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek2", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 2, LocalDate.parse("2020-06-30"), LocalTime.parse("08:30"), 1,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek3", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 3, LocalDate.parse("2021-03-06"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek3", customer, Status.Created));

        when(reservationManager.findAll()).thenReturn(reservations);

        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].fromStop", is("Przystanek1")))
                .andExpect(jsonPath("$[2].toStop", is("Przystanek3")));

        verify(reservationManager, times(1)).findAll();
    }

    @Test
    void getReservation_notFound() throws Exception {

        when(reservationManager.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/reservation"))
                .andExpect(status().isNotFound());

        verify(reservationManager, times(1)).findAll();
    }

    @Test
    void getById() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Optional<Reservation> found = Optional.of(new Reservation((long) 1, LocalDate.parse("2020-06-26"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek2", customer, Status.Unrealized));

        when(reservationManager.findById(1L)).thenReturn(found);

        mockMvc.perform(get("/reservation/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromStop", is("Przystanek1")))
                .andExpect(jsonPath("$.seats", is(2)))
                .andExpect(jsonPath("$.toStop", is("Przystanek2")));

        verify(reservationManager, times(1)).findById(1L);
    }

    @Test
    void getById_notFound() throws Exception {

        when(reservationManager.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/reservation/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(reservationManager, times(1)).findById(1L);
    }

    @Test
    void addReservationAdmin() {
    }

    @Test
    void addReservation() {
    }

    @Test
    void deleteReservation() {
    }

    @Test
    void deleteReservationAdmin() {
    }
}