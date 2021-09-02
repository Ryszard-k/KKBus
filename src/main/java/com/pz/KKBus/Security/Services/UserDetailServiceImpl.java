package com.pz.KKBus.Security.Services;

import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Repositories.CustomerRepo;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Repositories.EmployeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final CustomerRepo customerRepo;
    private final EmployeesRepo employeesRepo;

    @Autowired
    public UserDetailServiceImpl(CustomerRepo customerRepo, EmployeesRepo employeesRepo) {
        this.customerRepo = customerRepo;
        this.employeesRepo = employeesRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            Optional<Customer> customer = customerRepo.findByUsername(s);
            Optional<Employees> employees = employeesRepo.findByUsername(s);
            if (customer.isPresent()){
                return new User(customer.get().getUsername(),
                        customer.get().getPassword(), customer.get().getAuthorities());
            } else if (employees.isPresent()){
                return new User(employees.get().getUsername(),
                        employees.get().getPassword(), employees.get().getAuthorities());
            } else throw new UsernameNotFoundException("Cannot find user");
    }
}
