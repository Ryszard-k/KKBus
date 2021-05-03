package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.EmployeesManager;
import com.pz.KKBus.Model.EmployeesRepo;
import com.pz.KKBus.Model.Entites.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeesManager employeesManager;

    @Autowired
    public EmployeesController(EmployeesManager employeesManager) {
        this.employeesManager = employeesManager;
    }

    @GetMapping
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

}
