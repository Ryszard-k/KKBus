package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Repositories.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class ReservationManager {

    private ReservationRepo reservationRepo;
    private MailManager mailManager;
    private CustomerManager customerManager;

    @Autowired
    public ReservationManager(ReservationRepo reservationRepo, MailManager mailManager, CustomerManager customerManager) {
        this.reservationRepo = reservationRepo;
        this.mailManager = mailManager;
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

    public void notificationsForCustomers(Optional<KatowiceToKrakow> stop, String textPart1, String textPart2){
        Iterable<Reservation> foundReservation = reservationRepo.findAll();
        for (Reservation reservation: foundReservation) {
            if (reservation.getFromStop().equals(stop.get().getStop())){
                try {
                    mailManager.sendMail(reservation.getCustomer().getEmail(), "Reservation",
                            textPart1 + stop.get().getStop() + textPart2, false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    public void notificationsForCustomersKrkToKt(Optional<KrakowToKatowice> stop, String textPart1, String textPart2){
        Iterable<Reservation> foundReservation = reservationRepo.findAll();
        for (Reservation reservation: foundReservation) {
            if (reservation.getFromStop().equals(stop.get().getStop())){
                try {
                    mailManager.sendMail(reservation.getCustomer().getEmail(), "Reservation",
                            textPart1 + stop.get().getStop() + textPart2, false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

}
