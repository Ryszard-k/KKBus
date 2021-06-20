package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.Schedules.KrakowToKatowiceDepartureManager;
import com.pz.KKBus.Manager.Schedules.KrakowToKatowiceManager;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/startPage/departure")
public class KrakowToKatowiceDepartureController {

    private KrakowToKatowiceDepartureManager krakowToKatowiceDepartureManager;
    private KrakowToKatowiceManager krakowToKatowiceManager;

    @Autowired
    public KrakowToKatowiceDepartureController(KrakowToKatowiceDepartureManager krakowToKatowiceDepartureManager, KrakowToKatowiceManager krakowToKatowiceManager) {
        this.krakowToKatowiceDepartureManager = krakowToKatowiceDepartureManager;
        this.krakowToKatowiceManager = krakowToKatowiceManager;
    }

    @GetMapping
    public ResponseEntity getSchedules(){
        Iterable<KrakowToKatowiceDeparture> foundSchedules = krakowToKatowiceDepartureManager
                .findAllFromKrakowToKatowiceDeparture();
        int iterations = 0;
        for (KrakowToKatowiceDeparture krakowToKatowiceDeparture: foundSchedules) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundSchedules, HttpStatus.OK);
    }

    @GetMapping("/{stop}")
    public ResponseEntity getByStop(@PathVariable String stop){
        List<KrakowToKatowiceDeparture> foundStop = krakowToKatowiceDepartureManager
                .findByStopFromKrakowToKatowiceDeparture(stop);
        if(foundStop.isEmpty()){
            return new ResponseEntity<>("Bad stop", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(foundStop, HttpStatus.OK);
    }

    @PostMapping(path = "/{stop}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSchedule(@RequestBody KrakowToKatowiceDeparture krakowToKatowiceDeparture,
                                              @PathVariable String stop){
        Optional<KrakowToKatowice> krakowToKatowice = krakowToKatowiceManager.findByStopFromKrakowToKatowice(stop);
        if (krakowToKatowice != null && krakowToKatowiceDeparture != null) {
            krakowToKatowiceDepartureManager.saveInKrakowToKatowiceDeparture(krakowToKatowiceDeparture,
                    krakowToKatowice);
            return new ResponseEntity<>(krakowToKatowice, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{stop}/{id}")
    public ResponseEntity updateKrakowToKatowice(@RequestBody KrakowToKatowiceDeparture krakowToKatowiceDeparture,
                                                 @PathVariable String stop, @PathVariable Long id) {
        Optional<KrakowToKatowice> foundKrkToKt = krakowToKatowiceManager
                .findByStopFromKrakowToKatowice(stop);
        if (foundKrkToKt.isEmpty()) {
            return new ResponseEntity<>("Not found stop to update!", HttpStatus.NOT_FOUND);
        } else {
            krakowToKatowiceDepartureManager.updateToKrakowToKatowiceDeparture(krakowToKatowiceDeparture,
                    id, foundKrkToKt.get());
            return new ResponseEntity(krakowToKatowiceDeparture, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKrakowToKatowice(@PathVariable Long id) {
        Optional<KrakowToKatowiceDeparture> foundKrkToKt = krakowToKatowiceDepartureManager
                .findByIdFromKrakowToKatowiceDeparture(id);
        if (foundKrkToKt.isPresent()) {
            krakowToKatowiceDepartureManager.deleteFromKrakowToKatowiceDeparture(foundKrkToKt);
            return new ResponseEntity<>(foundKrkToKt,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found stop to delete!", HttpStatus.NOT_FOUND);
    }
}
