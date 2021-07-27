package com.pz.KKBus.Staff.Model.Repositories;

import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    List<Schedule> findByEmployeesSchedule(Employees employees);

    List<Schedule> findAllByWorkDateBetween(LocalDate start, LocalDate end);

    @Query(value = "SELECT * FROM Schedule c WHERE c.employee_id = :employee AND c.work_Date BETWEEN :start AND :end",
            nativeQuery = true)
    List<Schedule> findAllByEmployeesAndWorkDateBetween(@Param(value = "employee") Employees employee,
                                                        @Param(value = "start") LocalDate start,
                                                        @Param(value = "end") LocalDate end);
}
