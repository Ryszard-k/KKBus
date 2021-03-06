package com.pz.KKBus.Customer.Controller.Schedules;

import com.pz.KKBus.Customer.Manager.Schedules.KatowiceToKrakowManager;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Manager.ReservationManager;
import com.pz.KKBus.Customer.Manager.Schedules.KatowiceToKrakowDepartureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/katowiceToKrakow/departure")
public class KatowiceToKrakowDepartureController {

    private KatowiceToKrakowManager katowiceToKrakowManager;
    private KatowiceToKrakowDepartureManager katowiceToKrakowDepartureManager;
    private ReservationManager reservationManager;

    @Autowired
    public KatowiceToKrakowDepartureController(KatowiceToKrakowManager katowiceToKrakowManager, KatowiceToKrakowDepartureManager katowiceToKrakowDepartureManager, ReservationManager reservationManager) {
        this.katowiceToKrakowManager = katowiceToKrakowManager;
        this.katowiceToKrakowDepartureManager = katowiceToKrakowDepartureManager;
        this.reservationManager = reservationManager;
    }

    @GetMapping()
    public ResponseEntity getSchedules(){
        Iterable<KatowiceToKrakowDeparture> foundSchedules = katowiceToKrakowDepartureManager
                .findAllFromKatowiceToKrakowDeparture();
        int iterations = 0;
        for (KatowiceToKrakowDeparture katowiceToKrakowDeparture: foundSchedules) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundSchedules, HttpStatus.OK);
    }

    @GetMapping("/{stop}")
    public ResponseEntity getByStop(@PathVariable String stop){
        List<KatowiceToKrakowDeparture> foundStop = katowiceToKrakowDepartureManager
                .findByStopFromKatowiceToKrakowDeparture(stop);
        if(foundStop.isEmpty()){
            return new ResponseEntity<>("Bad stop", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(foundStop, HttpStatus.OK);
    }

    @PostMapping(path = "/{stop}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSchedule(@RequestBody KatowiceToKrakowDeparture katowiceToKrakowDeparture,
                                              @PathVariable String stop){
        Optional<KatowiceToKrakow> katowiceToKrakow = katowiceToKrakowManager.findByStopFromKatowiceToKrakow(stop);
        if (katowiceToKrakow != null && katowiceToKrakowDeparture != null) {
            katowiceToKrakowDepartureManager.saveInKatowiceToKrakowDeparture(katowiceToKrakowDeparture,
                    katowiceToKrakow);
            return new ResponseEntity<>(katowiceToKrakow, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{stop}/{id}")
    public ResponseEntity updateKatowiceToKrakow(@RequestBody KatowiceToKrakowDeparture katowiceToKrakowDeparture,
                                                 @PathVariable String stop, @PathVariable Long id) {
        Optional<KatowiceToKrakow> foundKrkToKt = katowiceToKrakowManager
                .findByStopFromKatowiceToKrakow(stop);
        if (foundKrkToKt.isEmpty()) {
            return new ResponseEntity<>("Not found stop to update!", HttpStatus.NOT_FOUND);
        } else {
            katowiceToKrakowDepartureManager.updateToKatowiceToKrakowDeparture(katowiceToKrakowDeparture,
                    id, foundKrkToKt.get());
            reservationManager.notificationsForCustomers(foundKrkToKt, "Departures for ",
                    " have been changed, pleas check Your reservation");
            return new ResponseEntity(katowiceToKrakowDeparture, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKatowiceToKrakow(@PathVariable Long id) {
        Optional<KatowiceToKrakowDeparture> foundKrkToKt = katowiceToKrakowDepartureManager
                .findByIdFromKatowiceToKrakowDeparture(id);
        if (foundKrkToKt.isPresent()) {
            katowiceToKrakowDepartureManager.deleteFromKatowiceToKrakowDeparture(foundKrkToKt);
            reservationManager.notificationsForCustomers(Optional.ofNullable(foundKrkToKt.get().getKatowiceToKrakow()),
                    "Departures for ", " have been canceled, pleas check Your reservation");
            return new ResponseEntity<>(foundKrkToKt,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found departure to delete!", HttpStatus.NOT_FOUND);
    }
}
