package com.pz.KKBus.Staff.Controller.Schedule;

import com.pz.KKBus.Staff.Manager.Schedule.AvailabilityManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Availability;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityManager availabilityManager;
    private final EmployeesManager employeesManager;

    @Autowired
    public AvailabilityController(AvailabilityManager availabilityManager, EmployeesManager employeesManager) {
        this.availabilityManager = availabilityManager;
        this.employeesManager = employeesManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Availability> founded = availabilityManager.findAll();
        if(founded.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Availability> founded = availabilityManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @PostMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addAvailabilities(@RequestBody List<Availability> availabilities, @PathVariable Long id){
        Optional<Employees> employees = employeesManager.findById(id);
        if (!availabilities.isEmpty() && employees.isPresent()) {
            availabilityManager.saveAll(availabilities);
            return new ResponseEntity<>(availabilities, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAvailabilities(@RequestBody List<Availability> availabilities) {
        if (!availabilities.isEmpty()) {
            availabilityManager.deleteAll(availabilities);
            return new ResponseEntity<>(availabilities,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found availabilities to delete!", HttpStatus.NOT_FOUND);
    }
}
