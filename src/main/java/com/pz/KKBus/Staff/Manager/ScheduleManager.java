package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule;
import com.pz.KKBus.Staff.Model.Entites.Unavailability;
import com.pz.KKBus.Staff.Model.Repositories.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleManager {

    private final ScheduleRepo scheduleRepo;

    @Autowired
    public ScheduleManager(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public List<Schedule> findAll(){
        return scheduleRepo.findAll();
    }

    public Optional<Schedule> findById(Long id){
        return scheduleRepo.findById(id);
    }

    public List<Schedule> findByPeriodAll(LocalDate fromDate, LocalDate toDate){
        return scheduleRepo.findAllByWorkDateBetween(fromDate, toDate);
    }

    public List<Schedule> findByPeriodForEmployee(Employees employee, LocalDate fromDate, LocalDate toDate){
        return scheduleRepo.findAllByEmployeesAndWorkDateBetween(employee, fromDate, toDate);
    }

    public List<Schedule> findByEmployee(Employees employees){
        return scheduleRepo.findByEmployeesSchedule(employees);
    }

    public List<Schedule> saveAll(List<Schedule> schedules){
        return scheduleRepo.saveAll(schedules);
    }

    public List<Schedule> deleteAll(List<Schedule> deleted){
        deleted.removeIf(unavailability -> !scheduleRepo.existsById(unavailability.getId()));
        scheduleRepo.deleteAll(deleted);
        return deleted;
    }
}
