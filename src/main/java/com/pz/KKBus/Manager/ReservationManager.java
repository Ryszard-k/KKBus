package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Employees;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Repositories.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationManager {

    private ReservationRepo reservationRepo;
    private CustomerManager customerManager;

    @Autowired
    public ReservationManager(ReservationRepo reservationRepo, CustomerManager customerManager) {
        this.reservationRepo = reservationRepo;
        this.customerManager = customerManager;
    }

    public Iterable<Reservation> findAll(){
        return reservationRepo.findAll();
    }

    public Optional<Reservation> findById(Long id){
        return reservationRepo.findById(id);
    }

    public Reservation save(Reservation reservation, Optional<Customer> customer){
        reservation.setCustomer(customer.get());
        return reservationRepo.save(reservation);
    }

    public Optional<Reservation> deleteById(Long id){
        Optional<Reservation> deleted = reservationRepo.findById(id);
        reservationRepo.deleteById(id);

        return deleted;
    }
}
