package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.CustomerManager;
import com.pz.KKBus.Manager.MailManager;
import com.pz.KKBus.Manager.ReservationManager;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationManager reservationManager;
    private final CustomerManager customerManager;
    private final KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;
    private final KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;
    private final MailManager mailManager;

    @Autowired
    public ReservationController(ReservationManager reservationManager, CustomerManager customerManager, KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo, KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo, MailManager mailManager) {
        this.reservationManager = reservationManager;
        this.customerManager = customerManager;
        this.katowiceToKrakowDepartureRepo = katowiceToKrakowDepartureRepo;
        this.krakowToKatowiceDepartureRepo = krakowToKatowiceDepartureRepo;
        this.mailManager = mailManager;
    }

    @GetMapping()
    public ResponseEntity getReservation(){
        List<Reservation> foundReservation = reservationManager.findAll();
        if(foundReservation.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundReservation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Reservation> foundDate = reservationManager.findById(id);
        if(foundDate.isEmpty()){
            return new ResponseEntity<>("Bad brand", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundDate, HttpStatus.OK);
    }

    @PostMapping(path = "/for-admin/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReservationAdmin(@RequestBody Reservation reservation,
                                              @PathVariable String username){
        Optional<Customer> customer = customerManager.findByUsername(username);
        if (reservation != null && customer.isPresent()) {
            reservationManager.save(reservation, customer);
            try {
                mailManager.sendMail(customer.get().getEmail(), "Reservation", "You have new reservation on: "
                        + reservation.getDate() + " added by our team", false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReservation(@RequestBody Reservation reservation,
                                                 @PathVariable String username) {
        Optional<Customer> customer = customerManager.findByUsername(username);
        long difference = 0;
        if ((LocalDate.now().isBefore(reservation.getDate()) || LocalDate.now().isEqual(reservation.getDate())) &&
                reservation.getDate().isBefore(LocalDate.now().plusDays(7)) &&
                reservationManager.enableReservation(customer)) {
            switch (reservation.getRoute()) {
                case KrakowToKatowice:
                    if (reservation.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                            reservation.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()), reservation.getDate()
                                .atTime(krakowToKatowiceDepartureRepo.findTopByOrderBySatSunDepartureAsc()
                                        .getSatSunDeparture())).toMinutes();
                    } else {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.of(10, 30)),
                                reservation.getDate().atTime(krakowToKatowiceDepartureRepo
                                        .findTopByOrderByMonFriDepartureAsc().getMonFriDeparture())).toMinutes();
                    }
                    break;

                case KatowiceToKrakow:
                    if (reservation.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                            reservation.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()), reservation.getDate()
                                .atTime(katowiceToKrakowDepartureRepo.findTopByOrderBySatSunDepartureAsc()
                                        .getSatSunDeparture())).toMinutes();
                    } else {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()), reservation.getDate()
                                .atTime(katowiceToKrakowDepartureRepo.findTopByOrderByMonFriDepartureAsc()
                                        .getMonFriDeparture())).toMinutes();
                    }
                    break;

                default:
                    return new ResponseEntity<>("Bad trace", HttpStatus.BAD_REQUEST);
            }

            if (customer.isPresent() && difference > 120) {
                reservationManager.save(reservation, customer);
                try {
                    mailManager.sendMail(customer.get().getEmail(), "Reservation",
                            "Your reservation on: " + reservation.getDate() + " has been saved", false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<>(reservation, HttpStatus.CREATED);
            } else
                return new ResponseEntity<>("Empty input data or bad time", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Bad date", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> foundReservation = reservationManager.findById(id);
        long difference = 0;
        if (LocalDate.now().isBefore(foundReservation.get().getDate()) || LocalDate.now().isEqual(foundReservation.get().getDate())) {
            switch (foundReservation.get().getRoute()) {
                case KrakowToKatowice:
                    if (foundReservation.get().getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                            foundReservation.get().getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()),
                                foundReservation.get().getDate().atTime(krakowToKatowiceDepartureRepo
                                .findTopByOrderBySatSunDepartureAsc().getSatSunDeparture())).toMinutes();
                    } else {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()),
                                foundReservation.get().getDate().atTime(krakowToKatowiceDepartureRepo
                                .findTopByOrderByMonFriDepartureAsc().getMonFriDeparture())).toMinutes();
                    }
                    break;

                case KatowiceToKrakow:
                    if (foundReservation.get().getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                            foundReservation.get().getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()),
                                foundReservation.get().getDate().atTime(katowiceToKrakowDepartureRepo
                                .findTopByOrderBySatSunDepartureAsc().getSatSunDeparture())).toMinutes();
                    } else {
                        difference = Duration.between(LocalDate.now().atTime(LocalTime.now()),
                                foundReservation.get().getDate().atTime(katowiceToKrakowDepartureRepo
                                .findTopByOrderByMonFriDepartureAsc().getMonFriDeparture())).toMinutes();
                    }
                    break;

                default:
                    return new ResponseEntity<>("Bad trace", HttpStatus.BAD_REQUEST);
            }
        if (foundReservation.isPresent() && difference > 1440) {
            reservationManager.deleteById(id);
            try {
                mailManager.sendMail(foundReservation.get().getCustomer().getEmail(), "Reservation",
                        "Your reservation on: " + foundReservation.get().getDate() + " has been canceled", false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(foundReservation,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found departure to delete!", HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>("Not permission to canceling reservation", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/for-admin/{id}")
    public ResponseEntity<Object> deleteReservationAdmin(@PathVariable Long id) {
        Optional<Reservation> foundReservation = reservationManager.findById(id);
        if (foundReservation.isPresent()) {
            reservationManager.deleteById(id);
            try {
                mailManager.sendMail(foundReservation.get().getCustomer().getEmail(), "Reservation",
                        "Your reservation on: " + foundReservation.get().getDate() + " has been canceled by our team", false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(foundReservation,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found departure to delete!", HttpStatus.NOT_FOUND);
    }
}
