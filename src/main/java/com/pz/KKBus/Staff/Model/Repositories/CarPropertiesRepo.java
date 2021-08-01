package com.pz.KKBus.Staff.Model.Repositories;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarPropertiesRepo extends JpaRepository<CarProperties, Long> {

    List<CarProperties> findByCar(Car car);

    List<CarProperties> findByDate(LocalDate date);
}
