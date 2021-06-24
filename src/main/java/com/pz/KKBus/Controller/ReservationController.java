package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.CustomerManager;
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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationManager reservationManager;
    private CustomerManager customerManager;
    private KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;
    private KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;

    @Autowired
    public ReservationController(ReservationManager reservationManager, CustomerManager customerManager, KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo, KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo) {
        this.reservationManager = reservationManager;
        this.customerManager = customerManager;
        this.katowiceToKrakowDepartureRepo = katowiceToKrakowDepartureRepo;
        this.krakowToKatowiceDepartureRepo = krakowToKatowiceDepartureRepo;
    }

    @GetMapping()
    public ResponseEntity getEmployees(){
        Iterable<Reservation> foundEmployees = reservationManager.findAll();
        int iterations = 0;
        for (Reservation reservation: foundEmployees) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundEmployees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Reservation> foundDate = reservationManager.findById(id);
        if(foundDate.isEmpty()){
            return new ResponseEntity<>("Bad brand", HttpStatus.BAD_REQUEST);
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
        if (reservation != null && customer != null) {
            reservationManager.save(reservation, customer);
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
        if (LocalDate.now().isBefore(reservation.getDate()) || LocalDate.now().isEqual(reservation.getDate())) {
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

            if (reservation != null && customer != null && difference > 120) {
                reservationManager.save(reservation, customer);
                return new ResponseEntity<>(reservation, HttpStatus.CREATED);
            } else
                return new ResponseEntity<>("Empty input data or bad time", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Bad date", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> foundReservation = reservationManager.findById(id);
        long difference = 0;
        if (LocalDate.now().isBefore(foundReservation.get().getDate())) {
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
            return new ResponseEntity<>(foundReservation,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found departure to delete!", HttpStatus.NOT_FOUND);
    }
}
