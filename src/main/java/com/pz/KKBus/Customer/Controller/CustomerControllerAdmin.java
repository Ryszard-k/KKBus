package com.pz.KKBus.Customer.Controller;

import com.pz.KKBus.Customer.Manager.CustomerManager;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Staff.Model.Entites.Car;
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
@RequestMapping("/customer")
public class CustomerControllerAdmin {

    private final CustomerManager customerManager;

    @Autowired
    public CustomerControllerAdmin(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Customer> found = customerManager.findAll();
        if(found.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Customer> founded = customerManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer){
        if (customer == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else
            customerManager.save(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@RequestBody Customer customer, @PathVariable Long id) {
        Optional<Customer> foundCustomer = customerManager.findById(id);
        if (foundCustomer.isPresent()) {
            customer.setId(foundCustomer.get().getId());
            customerManager.update(customer);
            return new ResponseEntity(customer, HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found customer to update!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> founded = customerManager.findById(id);
        if (founded.isPresent()) {
            customerManager.deleteById(id);
            return new ResponseEntity<>(founded,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found customer to delete!", HttpStatus.NOT_FOUND);
    }
}
