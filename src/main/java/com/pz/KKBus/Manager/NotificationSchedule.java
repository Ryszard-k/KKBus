package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDate;

@Component
public class NotificationSchedule {

    private ReservationManager reservationManager;
    private MailManager mailManager;

    @Autowired
    public NotificationSchedule(ReservationManager reservationManager, MailManager mailManager) {
        this.reservationManager = reservationManager;
        this.mailManager = mailManager;
    }

    @Scheduled(cron = "0 51 16 * *  ?", zone = "Europe/Warsaw")
    public void sendNotifications() {
        Iterable<Reservation> foundReservation = reservationManager.findAll();
        for (Reservation reservation: foundReservation) {
            if (reservation.getDate().isEqual(LocalDate.now())){
                try {
                    mailManager.sendMail(reservation.getCustomer().getEmail(), "Reservation",
                            "You have reservation today at: " + reservation.getTime(), false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
