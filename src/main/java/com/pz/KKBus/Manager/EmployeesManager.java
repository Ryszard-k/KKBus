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

@Service
public class EmployeesManager {

    private EmployeesRepo employeesRepo;

    @Autowired
    public EmployeesManager(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
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
