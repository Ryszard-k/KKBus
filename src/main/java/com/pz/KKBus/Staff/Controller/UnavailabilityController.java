package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Staff.Manager.AvailabilityManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Manager.UnavailabilityManager;
import com.pz.KKBus.Staff.Model.Entites.Availability;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Unavailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/unavailability")
public class UnavailabilityController {

    private final UnavailabilityManager unavailabilityManager;
    private final EmployeesManager employeesManager;

    @Autowired
    public UnavailabilityController(UnavailabilityManager unavailabilityManager, EmployeesManager employeesManager) {
        this.unavailabilityManager = unavailabilityManager;
        this.employeesManager = employeesManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Unavailability> founded = unavailabilityManager.findAll();
        if(founded.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Unavailability> founded = unavailabilityManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @PostMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addUnavailabilities(@RequestBody List<Unavailability> unavailabilities, @PathVariable Long id){
        Optional<Employees> employees = employeesManager.findById(id);
        if (!unavailabilities.isEmpty() && employees.isPresent()) {
            unavailabilityManager.saveAll(unavailabilities);
            return new ResponseEntity<>(unavailabilities, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteUnavailabilities(@RequestBody List<Unavailability> unavailabilities) {
        if (!unavailabilities.isEmpty()) {
            unavailabilityManager.deleteAll(unavailabilities);
            return new ResponseEntity<>(unavailabilities,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found unavailabilities to delete!", HttpStatus.NOT_FOUND);
    }
}
