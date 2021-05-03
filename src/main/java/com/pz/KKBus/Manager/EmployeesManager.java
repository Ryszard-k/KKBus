package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.EmployeesRepo;
import com.pz.KKBus.Model.Entites.Employees;
import com.pz.KKBus.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeesManager {

    private EmployeesRepo employeesRepo;

    @Autowired
    public EmployeesManager(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
    }

    public Iterable<Employees> findAll(){
        return employeesRepo.findAll();
    }

    public List<Employees> findByLastName(String lastName){
        return employeesRepo.findByLastName(lastName);
    }

    public Optional<Employees> findById(Long id){
        return employeesRepo.findById(id);
    }

    public Employees save(Employees employees){
        return employeesRepo.save(employees);
    }

    public Optional<Employees> deleteById(Long id){
        Optional<Employees> deleted = employeesRepo.findById((Long) id);
        employeesRepo.deleteById(id);

        return deleted;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillEmployees(){
        employeesRepo.save(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin));
        employeesRepo.save(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker));
        employeesRepo.save(new Employees((long) 3,"Andrzej",
                "Konrad", LocalDate.parse("1988-05-20"), Role.Driver));
    }
}
