package com.pz.KKBus.Staff.Model.Repositories.Courses;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepo extends JpaRepository<Courses, Long> {

    List<Courses> findByDriver(Employees employees);
    List<Courses> findByCar(Car car);
}
