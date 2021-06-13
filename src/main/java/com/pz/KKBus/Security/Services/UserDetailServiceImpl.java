package com.pz.KKBus.Security.Services;

import com.pz.KKBus.Model.CustomerRepo;
import com.pz.KKBus.Model.Entites.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private CustomerRepo customerRepo;

    @Autowired
    public UserDetailServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return customerRepo.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Cannot find Customer"));
    }
}
