package com.pz.KKBus.Model;

import com.pz.KKBus.Model.Entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer findByUsername(String username);
}
