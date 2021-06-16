package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Token;
import com.pz.KKBus.Model.Repositories.CustomerRepo;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Repositories.TokenRepo;
import com.pz.KKBus.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerManager {

    private CustomerRepo customerRepo;
    private PasswordEncoder passwordEncoder;
    private TokenRepo tokenRepo;
    private MailManager mailManager;

    @Autowired
    public CustomerManager(CustomerRepo customerRepo, PasswordEncoder passwordEncoder, TokenRepo tokenRepo, MailManager mailManager) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
        this.mailManager = mailManager;
    }

    public Iterable<Customer> findAll(){
        return customerRepo.findAll();
    }

    public Optional<Customer> findByLastName(String lastName){
        return customerRepo.findByLastName(lastName);
    }

    public Optional<Customer> findById(Long id){
        return customerRepo.findById(id);
    }

    public Customer save(Customer customer){
        customer.setUsername(usernameGenerator(customer));
        customer.setPassword(passwordEncoder.encode(passwordGenerator()));
        customer.setRole(Role.CustomerDisabled);
        sendToken(customer);

        return customerRepo.save(customer);
    }

    public Optional<Customer> deleteById(Long id){
        Optional<Customer> deleted = Optional.ofNullable(customerRepo.findById((Long) id).orElseThrow(() ->
                new IndexOutOfBoundsException("Customer not found")));
        customerRepo.deleteById(id);

        return deleted;
    }

    public Optional<Customer> passwordUpdate(String username, String updates){
        return Optional.ofNullable(customerRepo.findByUsername(username)
                .map(customer1 -> {
                    customer1.setPassword(passwordEncoder.encode(updates));
                    return customerRepo.save(customer1);
                })
                .orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    public String usernameGenerator(Customer customer){
        String username = customer.getFirstName().substring(0,3) + customer.getLastName().substring(0,3) +
                (customerRepo.findTopByOrderByIdDesc().getId() + 1);
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

    private void sendToken(Customer customer) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setCustomer(customer);
        tokenRepo.save(token);
        String url = "http://localhost:8080/token?value=" + tokenValue;
        try {
            mailManager.sendMail(customer.getEmail(), "Potwierdzenie konta", url + "\n" + "Username: " +
                            customer.getUsername(), false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillCustomer(){
        customerRepo.save(new Customer((long) 1, "Marek", "Kowalski", LocalDate.parse("1983-02-23"), "kowalski@gmail.com",
                123456789, "kowalski", passwordEncoder.encode("kowalski123"),
                Role.CustomerEnabled, true));
    }
}
