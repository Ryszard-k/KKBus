package com.pz.KKBus.Controller;

import antlr.MismatchedTokenException;
import com.pz.KKBus.Manager.CustomerManager;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Token;
import com.pz.KKBus.Model.Repositories.CustomerRepo;
import com.pz.KKBus.Model.Repositories.TokenRepo;
import com.pz.KKBus.Model.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CustomerController {

    private CustomerManager customerManager;
    private TokenRepo tokenRepo;
    private CustomerRepo customerRepo;

    @Autowired
    public CustomerController(CustomerManager customerManager, TokenRepo tokenRepo, CustomerRepo customerRepo) {
        this.customerManager = customerManager;
        this.tokenRepo = tokenRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/sign-up")
    public String signup(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String register(Customer customer) {
        customerManager.save(customer);
        return "register";
    }

    @GetMapping("/token")
    public String token(@RequestParam String value) throws MismatchedTokenException {
        Optional<Token> byValue = Optional.ofNullable(tokenRepo.findByValue(value).orElseThrow(() ->
                new MismatchedTokenException()));

        Customer customer = byValue.get().getCustomer();
        customer.setEnabled(true);
        customer.setRole(Role.CustomerEnabled);
        customerRepo.save(customer);
        return "chpassword";
    }

    @GetMapping("/password")
    public String password(Model model) {
        model.addAttribute("password", new Customer().getPassword());
        return "chpassword";
    }

    @PostMapping("/chpassword")
    public String chpassword(String password, String username) {
        customerManager.passwordUpdate(username, password);
        return "hello";
    }
}
