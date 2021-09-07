package com.pz.KKBus.Customer.Controller.Schedules;

import com.pz.KKBus.Customer.Manager.Schedules.KatowiceToKrakowManager;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/katowiceToKrakow")
public class KatowiceToKrakowController {

    private KatowiceToKrakowManager katowiceToKrakowManager;

    @Autowired
    public KatowiceToKrakowController(KatowiceToKrakowManager katowiceToKrakowManager) {
        this.katowiceToKrakowManager = katowiceToKrakowManager;
    }

    @GetMapping
    public ResponseEntity getSchedules(){
        Iterable<KatowiceToKrakow> foundSchedules = katowiceToKrakowManager.findAllFromKatowiceToKrakow();
        int iterations = 0;
        for (KatowiceToKrakow katowiceToKrakow: foundSchedules) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundSchedules, HttpStatus.OK);
    }

    @GetMapping("/{stop}")
    public ResponseEntity getByStop(@PathVariable String stop){
        Optional<KatowiceToKrakow> foundStop = katowiceToKrakowManager.findByStopFromKatowiceToKrakow(stop);
        if(foundStop.isEmpty()){
            return new ResponseEntity<>("Bad stop", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(foundStop, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSchedule(@RequestBody KatowiceToKrakow katowiceToKrakow){
        if (katowiceToKrakow != null) {
            katowiceToKrakowManager.saveInKatowiceToKrakow(katowiceToKrakow);
            return new ResponseEntity<>(katowiceToKrakow, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{stop}")
    public ResponseEntity updateToKatowiceToKrakow(@RequestBody KatowiceToKrakow katowiceToKrakow,
                                                 @PathVariable String stop) {
        Optional<KatowiceToKrakow> foundKrkToKt = katowiceToKrakowManager.findByStopFromKatowiceToKrakow(stop);
        if (foundKrkToKt.isPresent()) {
            katowiceToKrakowManager.updateToKatowiceToKrakow(katowiceToKrakow, foundKrkToKt);
            return new ResponseEntity(katowiceToKrakow, HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found stop to update!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKatowiceToKrakow(@PathVariable Long id) {
        Optional<KatowiceToKrakow> foundKrkToKt = katowiceToKrakowManager.findByIdFromKatowiceToKrakow(id);
        if (foundKrkToKt.isPresent()) {
            katowiceToKrakowManager.deleteFromKatowiceToKrakow(foundKrkToKt);
            return new ResponseEntity<>(foundKrkToKt,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found stop to delete!", HttpStatus.NOT_FOUND);
    }
}
