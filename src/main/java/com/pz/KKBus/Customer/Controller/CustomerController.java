package com.pz.KKBus.Customer.Controller;

import antlr.MismatchedTokenException;
import com.pz.KKBus.Customer.Manager.CustomerManager;
import com.pz.KKBus.Customer.Model.Entites.Token;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Customer.Model.Repositories.TokenRepo;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class CustomerController {

    private final CustomerManager customerManager;
    private final TokenRepo tokenRepo;

    @Autowired
    public CustomerController(CustomerManager customerManager, TokenRepo tokenRepo) {
        this.customerManager = customerManager;
        this.tokenRepo = tokenRepo;
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

    @GetMapping("/token/token")
    public String token(@RequestParam String value, HttpServletRequest request) throws Exception {
        Optional<Token> byValue = Optional.ofNullable(tokenRepo.findByValue(value)
                .orElseThrow(MismatchedTokenException::new));
        Customer customer = byValue.get().getCustomer();

        customer.setEnabled(true);
        customer.setRole(Role.CustomerEnabled);
        customerManager.update(customer);
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
        return "chpassword";
    }
}
