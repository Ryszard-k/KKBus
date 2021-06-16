package com.pz.KKBus.Model.Repositories;

import com.pz.KKBus.Model.Entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByLastName(String lastName);

    Customer findTopByOrderByIdDesc();

}
