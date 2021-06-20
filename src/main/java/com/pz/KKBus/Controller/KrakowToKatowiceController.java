package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.Schedules.KrakowToKatowiceManager;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/startPage")
public class KrakowToKatowiceController {

    private KrakowToKatowiceManager krakowToKatowiceManager;

    @Autowired
    public KrakowToKatowiceController(KrakowToKatowiceManager krakowToKatowiceManager) {
        this.krakowToKatowiceManager = krakowToKatowiceManager;
    }

    @GetMapping
    public ResponseEntity getSchedules(){
        Iterable<KrakowToKatowice> foundSchedules = krakowToKatowiceManager.findAllFromKrakowToKatowice();
        int iterations = 0;
        for (KrakowToKatowice krakowToKatowice: foundSchedules) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundSchedules, HttpStatus.OK);
    }

    @GetMapping("/{stop}")
    public ResponseEntity getByStop(@PathVariable String stop){
        Optional<KrakowToKatowice> foundStop = krakowToKatowiceManager.findByStopFromKrakowToKatowice(stop);
        if(foundStop.isEmpty()){
            return new ResponseEntity<>("Bad stop", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(foundStop, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSchedule(@RequestBody KrakowToKatowice krakowToKatowice){
        if (krakowToKatowice != null) {
            krakowToKatowiceManager.saveInKrakowToKatowice(krakowToKatowice);
            return new ResponseEntity<>(krakowToKatowice, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{stop}")
    public ResponseEntity updateKrakowToKatowice(@RequestBody KrakowToKatowice krakowToKatowice,
                                                 @PathVariable String stop) {
        Optional<KrakowToKatowice> foundKrkToKt = krakowToKatowiceManager.findByStopFromKrakowToKatowice(stop);
        if (foundKrkToKt.isPresent()) {
            krakowToKatowiceManager.updateToKrakowToKatowice(krakowToKatowice, foundKrkToKt);
            return new ResponseEntity(krakowToKatowice, HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found stop to update!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKrakowToKatowice(@PathVariable Long id) {
        Optional<KrakowToKatowice> foundKrkToKt = krakowToKatowiceManager.findByIdFromKrakowToKatowice(id);
        if (foundKrkToKt.isPresent()) {
            krakowToKatowiceManager.deleteFromKrakowToKatowice(foundKrkToKt);
            return new ResponseEntity<>(foundKrkToKt,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found stop to delete!", HttpStatus.NOT_FOUND);
    }
}
