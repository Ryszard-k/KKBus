package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.CustomerManager;
import com.pz.KKBus.Manager.ReservationManager;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationManager reservationManager;
    private CustomerManager customerManager;

    @Autowired
    public ReservationController(ReservationManager reservationManager, CustomerManager customerManager) {
        this.reservationManager = reservationManager;
        this.customerManager = customerManager;
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

    @PostMapping(path = "/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReservation(@RequestBody Reservation reservation,
                                              @PathVariable String username){
        Optional<Customer> customer = customerManager.findByUsername(username);
        if (reservation != null && customer != null) {
            reservationManager.save(reservation, customer);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> foundReservation = reservationManager.findById(id);
        if (foundReservation.isPresent()) {
            reservationManager.deleteById(id);
            return new ResponseEntity<>(foundReservation,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found departure to delete!", HttpStatus.NOT_FOUND);
    }
}
