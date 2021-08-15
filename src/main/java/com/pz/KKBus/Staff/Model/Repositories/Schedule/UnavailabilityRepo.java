package com.pz.KKBus.Staff.Model.Repositories.Schedule;

import com.pz.KKBus.Staff.Model.Entites.Schedule.Unavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnavailabilityRepo extends JpaRepository<Unavailability, Long> {
}
