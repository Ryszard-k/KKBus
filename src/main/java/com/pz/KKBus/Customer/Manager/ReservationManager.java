package com.pz.KKBus.Customer.Manager;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Customer.Model.Repositories.ReservationRepo;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationManager {

    private final ReservationRepo reservationRepo;
    private final MailManager mailManager;

    @Autowired
    public ReservationManager(ReservationRepo reservationRepo, MailManager mailManager) {
        this.reservationRepo = reservationRepo;
        this.mailManager = mailManager;
    }

    public List<Reservation> findAll(){
        return reservationRepo.findAll();
    }

    public Optional<Reservation> findById(Long id){
        return reservationRepo.findById(id);
    }

    public List<Reservation> findByDate(LocalDate date){
        return reservationRepo.findByDate(date);
    }

    public Reservation save(Reservation reservation, Optional<Customer> customer){
            reservation.setCustomer(customer.get());
            reservation.setStatus(Status.Created);
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
    }

    public boolean enableReservation(Optional<Customer> customer){
        int countConfirm = reservationRepo.countByStatusAndCustomer(Status.Unrealized, customer);
        if(countConfirm == 3){
            List<Reservation> foundReservation = reservationRepo.findByStatus(Status.Unrealized);
            foundReservation.sort(Comparator.comparing(Reservation::getDate));
            if (foundReservation.get(2).getDate().plusMonths(2).isEqual(LocalDate.now()) ||
                    foundReservation.get(2).getDate().plusMonths(2).isBefore(LocalDate.now())){
                for (Reservation reservation : foundReservation) {
                    reservation.setStatus(Status.ArchiveUnrealized);
                    reservationRepo.save(reservation);
                }
                return true;
            } else
            try {
                mailManager.sendMail(foundReservation.get(2).getCustomer().getEmail(), "Reservation",
                        "Your possibility of booking is blocked to " + foundReservation.get(2).getDate(), false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        } else return true;
    }
}
