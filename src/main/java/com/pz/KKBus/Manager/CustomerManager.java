package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.CustomerRepo;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerManager {

    private CustomerRepo customerRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerManager(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<Customer> findAll(){
        return customerRepo.findAll();
    }

    public List<Customer> findByLastName(String lastName){
        return customerRepo.findByLastName(lastName);
    }

    public Optional<Customer> findById(Long id){
        return customerRepo.findById(id);
    }

    public Customer save(Customer customer){
        customer.setUsername(usernameGenerator(customer));
        customer.setPassword(passwordEncoder.encode(passwordGenerator()));
        customer.setRole(Role.CustomerDisabled);
        customer.setEnabled(true);

        return customerRepo.save(customer);
    }

    public Optional<Customer> deleteById(Long id){
        Optional<Customer> deleted = customerRepo.findById((Long) id);
        customerRepo.deleteById(id);

        return deleted;
    }

    public String usernameGenerator(Customer customer){
        String username = customer.getFirstName().substring(0,3) + customer.getLastName().substring(0,3);
        return username;
    }

    public String passwordGenerator(){
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";
        String values = Capital_chars + Small_chars + numbers + symbols;
        Random randomInt = new Random();
        int length = randomInt.ints(10, 15)
                .findFirst()
                .getAsInt();

        Random random = new Random();

        char[] password = new char[length];

        for (int i = 0; i < length; i++)
        {
            password[i] = values.charAt(random.nextInt(values.length()));
        }
        System.out.println(String.valueOf(password));
        return String.valueOf(password);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillCustomer(){
        customerRepo.save(new Customer((long) 1, "Marek", "Kowalski", LocalDate.parse("1983-02-23"), "kowalski@gmail.com",
                123456789, "kowalski", passwordEncoder.encode("kowalski123"),
                Role.CustomerEnabled, true));
    }
}
