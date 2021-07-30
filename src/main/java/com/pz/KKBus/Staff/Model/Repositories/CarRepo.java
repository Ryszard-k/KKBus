package com.pz.KKBus.Staff.Model.Repositories;

import com.pz.KKBus.Staff.Model.Entites.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {
}
