package com.pz.KKBus.Customer.Manager;

import com.pz.KKBus.Customer.Manager.MailManager;
import com.pz.KKBus.Customer.Manager.ReservationManager;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Customer.Model.Enums.Status;
import com.pz.KKBus.Customer.Model.Repositories.ReservationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
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

    @Mock
    private MailManager mailManager;

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

        List<Reservation> reservations2 = reservationManager.findAll();

        assertEquals(3, reservations2.size());
        verify(reservationRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(reservationRepo.findAll()).thenReturn(null);

        List<Reservation> reservations2 = reservationManager.findAll();

        assertNull(reservations2);
        verify(reservationRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(reservationRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(reservationsList().get(0)));

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
    void save_with_null_reservation() {
        when(reservationRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> reservationManager.save(null,
                Optional.ofNullable(customerList().get(0))));
    }

    @Test
    void save_with_null_customer() {
        Reservation reservation = new Reservation((long) 4, LocalDate.parse("2021-06-28"), LocalTime.parse("07:30"), 2,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek4", null, Status.Unrealized);

        when(reservationRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> reservationManager.save(reservation,
                null));
    }

    @Test
    void save_with_null_parameters() {
        when(reservationRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> reservationManager.save(null,
                null));
    }

    @Test
    void deleteById() {
        when(reservationRepo.findById(1L)).thenReturn(Optional.ofNullable(reservationsList().get(0)));

        Optional<Reservation> reservations2 = reservationManager.deleteById(1L);

        assertEquals(reservationsList().get(0).getId(), reservations2.get().getId());
        assertEquals(reservationsList().get(0).getDate(), reservations2.get().getDate());
        assertEquals(reservationsList().get(0).getCustomer().getFirstName(), reservations2.get().getCustomer().getFirstName());

        verify(reservationRepo, times(1)).findById(1L);
        verify(reservationRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_not_found_id() {
        when(reservationRepo.findById(1L)).thenReturn(isNull());

        Optional<Reservation> reservations2 = reservationManager.deleteById(1L);

        assertNull(reservations2);
        verify(reservationRepo, times(1)).findById(1L);
        verify(reservationRepo, times(1)).deleteById(1L);
    }

    @Test
    void notificationsForCustomers() throws MessagingException {
        KatowiceToKrakow stop = new KatowiceToKrakow((long) 2,"Przystanek2",
                null, 7, 32);

        when(reservationRepo.findAll()).thenReturn(reservationsList());

        reservationManager.notificationsForCustomers(Optional.of(stop), "part1", "part2");

        verify(mailManager, times(1)).sendMail(anyString(),
                anyString(), anyString(), eq(false));
    }

    @Test
    void notificationsForCustomers_with_null_reservations() {
        KatowiceToKrakow stop = new KatowiceToKrakow((long) 2,"Przystanek2",
                null, 7, 32);

        when(reservationRepo.findAll()).thenReturn(null).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> reservationManager.notificationsForCustomers(Optional.of(stop),
                "part1", "part2"));
        verifyNoInteractions(mailManager);
    }

    @Test
    void notificationsForCustomers_with_null_field_reservations() {

        when(reservationRepo.findAll()).thenReturn(reservationsList());

        assertThrows(NullPointerException.class, () -> reservationManager.notificationsForCustomers(null,
                "part1", "part2"));
        verifyNoInteractions(mailManager);
    }

    @Test
    void notificationsForCustomersKrkToKt() throws MessagingException {
        KrakowToKatowice stop = new KrakowToKatowice((long) 2, "Przystanek2", null
                , 10, 40);

        when(reservationRepo.findAll()).thenReturn(reservationsList());

        reservationManager.notificationsForCustomersKrkToKt(Optional.of(stop), "part1", "part2");

        verify(mailManager, times(1)).sendMail(anyString(),
                anyString(), anyString(), eq(false));
    }

    @Test
    void notificationsForCustomersKrkToKt_with_null_reservations() {
        KrakowToKatowice stop = new KrakowToKatowice((long) 2, "Przystanek2", null
                , 10, 40);

        when(reservationRepo.findAll()).thenReturn(null).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> reservationManager.notificationsForCustomersKrkToKt(Optional.of(stop),
                "part1", "part2"));
        verifyNoInteractions(mailManager);
    }

    @Test
    void notificationsForCustomersKrkToKt_with_null_field_reservations() {
        when(reservationRepo.findAll()).thenReturn(reservationsList());

        assertThrows(NullPointerException.class, () -> reservationManager.notificationsForCustomers(null,
                "part1", "part2"));
        verifyNoInteractions(mailManager);
    }

    @Test
    void enableReservation_shouldReturnTrueWithNothingToDo() {
        when(reservationRepo.countByStatusAndCustomer(Status.Unrealized ,
                Optional.of(customerList().get(0)))).thenReturn(1);
        when(reservationRepo.findByStatus(Status.Unrealized)).thenReturn(reservationsList());

        assertTrue(reservationManager.enableReservation(Optional.of(customerList().get(0))));
        verify(reservationRepo, times(0)).findByStatus(Status.Unrealized);
        verify(reservationRepo, times(0)).save(any());
        verifyNoInteractions(mailManager);
    }

    @Test
    void enableReservation_shouldReturnTrue_saveToDB() {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation((long) 1, LocalDate.parse("2020-06-26"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek2", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 2, LocalDate.parse("2020-06-30"), LocalTime.parse("08:30"), 1,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek3", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 3, LocalDate.parse("2021-03-06"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek3", customer, Status.Created));

        when(reservationRepo.countByStatusAndCustomer(Status.Unrealized ,
                Optional.of(customer))).thenReturn(3);
        when(reservationRepo.findByStatus(Status.Unrealized)).thenReturn(reservations);
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservations.get(0));

        assertTrue(reservationManager.enableReservation(Optional.of(customer)));

        verify(reservationRepo, times(1)).countByStatusAndCustomer(Status.Unrealized ,
                Optional.of(customer));
        verify(reservationRepo, times(1)).findByStatus(Status.Unrealized);
        verify(reservationRepo, times(3)).save(any(Reservation.class));
        verifyNoInteractions(mailManager);
    }

    @Test
    void enableReservation_shouldReturnFalse_sendEmail() throws MessagingException {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation((long) 1, LocalDate.parse("2020-06-26"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek2", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 2, LocalDate.parse("2020-06-30"), LocalTime.parse("08:30"), 1,
                Route.KrakowToKatowice, "Przystanek2", "Przystanek3", customer, Status.Unrealized));
        reservations.add(new Reservation((long) 3, LocalDate.parse("2021-07-06"), LocalTime.parse("08:30"), 2,
                Route.KrakowToKatowice, "Przystanek1", "Przystanek3", customer, Status.Created));

        when(reservationRepo.countByStatusAndCustomer(Status.Unrealized ,
                Optional.of(customer))).thenReturn(3);
        when(reservationRepo.findByStatus(Status.Unrealized)).thenReturn(reservations);
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservations.get(0));

        assertFalse(reservationManager.enableReservation(Optional.of(customer)));

        verify(reservationRepo, times(1)).countByStatusAndCustomer(Status.Unrealized ,
                Optional.of(customer));
        verify(reservationRepo, times(1)).findByStatus(Status.Unrealized);
        verify(reservationRepo, times(0)).save(any(Reservation.class));
        verify(mailManager, times(1)).sendMail(anyString(), anyString(), anyString(), eq(false));
    }
}