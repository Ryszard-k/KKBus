package com.pz.KKBus.Staff.Model.Repositories.Courses;

import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CoursesRepo extends JpaRepository<Courses, Long> {

    List<Courses> findByDriver(Employees employees);
    List<Courses> findByCar(Car car);

    @Query(value = "SELECT report FROM Courses c WHERE c.date = :date AND c.route = :route",
            nativeQuery = true)
    List<Report> findByDate(@Param(value = "date") LocalDate date, @Param(value = "route") Route route);
}
