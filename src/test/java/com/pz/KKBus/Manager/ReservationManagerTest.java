package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Enums.Role;
import com.pz.KKBus.Model.Enums.Route;
import com.pz.KKBus.Model.Enums.Status;
import com.pz.KKBus.Model.Repositories.ReservationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ReservationManagerTest {

    @Mock
    private ReservationRepo reservationRepo;

    @InjectMocks
    private ReservationManager reservationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    private List<Reservation> reservationsList() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation((long) 1, LocalDate.parse("2021-06-26"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek2", customerList().get(0), Status.Unrealized));
        reservations.add(new Reservation((long) 2, LocalDate.parse("2021-06-30"), LocalTime.parse("08:30"), 1,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek3", customerList().get(0), Status.Created));
        reservations.add(new Reservation((long) 3, LocalDate.parse("2021-07-06"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek3", customerList().get(0), Status.Created));
        return reservations;
    }

    private List<Customer> customerList() {
        List<Customer> customers = new ArrayList<>();

    customers.add(new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
            123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true));
        return customers;
    }

    @Test
    void findAll_with_three_reservation() {
        when(reservationRepo.findAll()).thenReturn(reservationsList());

        Iterable<Reservation> reservations2 = reservationManager.findAll();
        int iterations = 0;
        for (Reservation reservation: reservations2) {
            iterations++;
        }
        assertEquals(3, iterations);
        verify(reservationRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(reservationRepo.findAll()).thenReturn(null);

        Iterable<Reservation> reservations2 = reservationManager.findAll();

        assertNull(reservations2);
        verify(reservationRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(reservationRepo.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(reservationsList().get(0)));

        Optional<Reservation> reservations2 = reservationManager.findById(1L);

        assertEquals(reservationsList().get(0).getId(), reservations2.get().getId());
        assertEquals(reservationsList().get(0).getDate(), reservations2.get().getDate());
        assertEquals(reservationsList().get(0).getCustomer().getFirstName(), reservations2.get().getCustomer().getFirstName());

        verify(reservationRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(reservationRepo.findById(anyLong())).thenReturn(null);

        Optional<Reservation> reservations2 = reservationManager.findById(1L);

        assertNull(reservations2);
        verify(reservationRepo, times(1)).findById(1L);
    }

    @Test
    void save() {
        Reservation reservation = new Reservation((long) 4, LocalDate.parse("2021-06-28"), LocalTime.parse("07:30"), 2,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek4", customerList().get(0), Status.Unrealized);

        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservation);

        Reservation reservation1 = reservationManager.save(reservation, Optional.ofNullable(customerList().get(0)));

        assertEquals(reservation1.getId(), reservation.getId());
        assertEquals(reservation1.getRoute(), reservation.getRoute());
        assertEquals(reservation1.getCustomer().getFirstName(), reservation.getCustomer().getFirstName());
        verify(reservationRepo, times(1)).save(reservation);
    }

    @Test
    void deleteById() {
    }

    @Test
    void notificationsForCustomers() {
    }

    @Test
    void notificationsForCustomersKrkToKt() {
    }

    @Test
    void enableReservation() {
    }
}