package com.pz.KKBus.Staff.Model.Repositories.Schedule;

import com.pz.KKBus.Staff.Model.Entites.Schedule.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepo extends JpaRepository<Availability, Long> {
}
