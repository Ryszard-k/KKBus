package com.pz.KKBus.Customer.Model.Repositories;

import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    int countByStatusAndCustomer(Status status, Optional<Customer> customer);

    List<Reservation> findByStatus(Status status);
}
