package com.pz.KKBus.Staff.Model.Repositories.Courses;

import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopPassengersPairRepo extends JpaRepository<StopPassengersPair, Long> {
}
