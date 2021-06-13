package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.EmployeesManager;
import com.pz.KKBus.Model.Entites.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class EmployeesController {

    private final EmployeesManager employeesManager;

    @Autowired
    public EmployeesController(EmployeesManager employeesManager) {
        this.employeesManager = employeesManager;
    }

    @GetMapping("/employees")
    public ResponseEntity getEmployees(){
        Iterable<Employees> foundEmployees = employeesManager.findAll();
        int iterations = 0;
        for (Employees employees: foundEmployees) {
            iterations++;
        }
        if(iterations == 0){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundEmployees, HttpStatus.OK);
    }

    @GetMapping("/employees/{lastName}")
    public ResponseEntity getByLastName(@PathVariable String lastName){
        List<Employees> foundLastName = employeesManager.findByLastName(lastName);
        if(foundLastName.isEmpty()){
            return new ResponseEntity<>("Bad brand", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(foundLastName, HttpStatus.OK);
    }

    @PostMapping(path = "/employees",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addEmployees(@RequestBody Employees employees){
        if (employees != null) {
            employeesManager.save(employees);
            return new ResponseEntity<>(employees, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity updateEmployee(@RequestBody Employees employees, @PathVariable Long id) {
        Optional<Employees> foundCar = employeesManager.findById(id);
        if (foundCar.isPresent()) {
            employeesManager.save(employees);
            return new ResponseEntity(employees, HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found car to update!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        Optional<Employees> foundCar = employeesManager.findById(id);
        if (foundCar.isPresent()) {
            employeesManager.deleteById(id);
            return new ResponseEntity<>(foundCar,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found car to delete!", HttpStatus.NOT_FOUND);
    }
}
