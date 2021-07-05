package com.pz.KKBus.Model.Repositories;

import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    int countByStatus(Status status);

    List<Reservation> findByStatus(Status status);
}
