package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/car")
public class CarController {

    private final CarManager carManager;

    @Autowired
    public CarController(CarManager carManager) {
        this.carManager = carManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Car> found = carManager.findAll();
        if(found.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Car> founded = carManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addCar(@RequestBody Car car){
        if (car == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else
            carManager.save(car);
            return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        Optional<Car> car = carManager.findById(id);
        if (car.isPresent()) {
            carManager.deleteById(id);
            return new ResponseEntity<>(car,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found car to delete!", HttpStatus.NOT_FOUND);
    }
}
