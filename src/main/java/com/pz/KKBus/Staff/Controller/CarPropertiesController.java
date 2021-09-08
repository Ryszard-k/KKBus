package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CarPropertiesManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/carProperties")
public class CarPropertiesController {

    private final CarPropertiesManager carPropertiesManager;
    private final CarManager carManager;

    @Autowired
    public CarPropertiesController(CarPropertiesManager carPropertiesManager, CarManager carManager) {
        this.carPropertiesManager = carPropertiesManager;
        this.carManager = carManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<CarProperties> found = carPropertiesManager.findAll();
        if(found.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity getByCar(@PathVariable Long id){
        Optional<Car> founded = carManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            List<CarProperties> foundedProperties = carPropertiesManager.findByCar(founded.get());
            return new ResponseEntity<>(foundedProperties, HttpStatus.OK);
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity getByDate(@PathVariable String date){
        List<CarProperties> founded = carPropertiesManager.findByDate(LocalDate.parse(date));
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad date", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addCarProperties(@RequestBody CarProperties car){
        if (car == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else
            carPropertiesManager.save(car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCarProperties(@PathVariable Long id) {
        Optional<CarProperties> car = carPropertiesManager.findById(id);
        if (car.isPresent()) {
            carPropertiesManager.deleteById(id);
            return new ResponseEntity<>(car,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found car properties to delete!", HttpStatus.NOT_FOUND);
    }
}
