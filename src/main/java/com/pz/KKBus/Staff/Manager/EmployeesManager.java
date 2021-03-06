package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Repositories.EmployeesRepo;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeesManager {

    private final EmployeesRepo employeesRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeesManager(EmployeesRepo employeesRepo, PasswordEncoder passwordEncoder) {
        this.employeesRepo = employeesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<Employees> findAll(){
        return employeesRepo.findAll();
    }

    public List<Employees> findByLastName(String lastName){
        return employeesRepo.findByLastName(lastName);
    }

    public List<Employees> findByRole(Role role){
        return employeesRepo.findByRole(role);
    }

    public Optional<Employees> findById(Long id){
        return employeesRepo.findById(id);
    }

    public Employees save(Employees employees){
        employees.setPassword(passwordEncoder.encode(employees.getPassword()));
        return employeesRepo.save(employees);
    }

    public Optional<Employees> deleteById(Long id){
        Optional<Employees> deleted = employeesRepo.findById(id);
        employeesRepo.deleteById(id);

        return deleted;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillEmployees(){
        employeesRepo.save(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Driver, "KowalskiJan",
                passwordEncoder.encode("KowalskiJan"), 5000));
        employeesRepo.save(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, "NowakAnna",
                passwordEncoder.encode("NowakAnna"),3000));
        employeesRepo.save(new Employees((long) 3,"Andrzej",
                "Konrad", LocalDate.parse("1988-05-20"), Role.Admin, "KonradAndrzej",
                passwordEncoder.encode("KonradAndrzej"),4000));
    }
}
