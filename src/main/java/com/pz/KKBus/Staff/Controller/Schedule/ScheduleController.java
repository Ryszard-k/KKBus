package com.pz.KKBus.Staff.Controller.Schedule;

import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Manager.Schedule.ScheduleManager;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleManager scheduleManager;
    private final EmployeesManager employeesManager;

    @Autowired
    public ScheduleController(ScheduleManager scheduleManager, EmployeesManager employeesManager) {
        this.scheduleManager = scheduleManager;
        this.employeesManager = employeesManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Schedule> founded = scheduleManager.findAll();
        if(founded.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Schedule> founded = scheduleManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/{fromDate}/{toDate}")
    public ResponseEntity getByPeriod(@PathVariable String fromDate, @PathVariable String toDate){
        List<Schedule> founded = scheduleManager.findByPeriodAll(LocalDate.parse(fromDate), LocalDate.parse(toDate));
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad period", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/{id}/{fromDate}/{toDate}")
    public ResponseEntity getByEmployeePeriod(@PathVariable String fromDate, @PathVariable String toDate,
                                              @PathVariable Long id) {
        Optional<Employees> employee = employeesManager.findById(id);
        if (employee.isEmpty()) {
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            List<Schedule> founded = scheduleManager.findByPeriodForEmployee(employee.get(), LocalDate.parse(fromDate),
                    LocalDate.parse(toDate));
            if (founded.isEmpty()) {
                return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(founded, HttpStatus.OK);
        }
    }

        @GetMapping("/employee/{id}")
        public ResponseEntity getByEmployee(@PathVariable Long id) {
            Optional<Employees> employee = employeesManager.findById(id);
            if (employee.isEmpty()) {
                return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
            } else {
                List<Schedule> founded = scheduleManager.findByEmployee(employee.get());
                if (founded.isEmpty()) {
                    return new ResponseEntity<>("Schedule not founded", HttpStatus.NOT_FOUND);
                } else
                    return new ResponseEntity<>(founded, HttpStatus.OK);
            }
        }

    @PostMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSchedules(@RequestBody List<Schedule> schedules, @PathVariable Long id){
        Optional<Employees> employees = employeesManager.findById(id);
        if (!schedules.isEmpty() && employees.isPresent()) {
            scheduleManager.saveAll(schedules);
            return new ResponseEntity<>(schedules, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteSchedules(@RequestBody List<Schedule> schedules) {
        if (!schedules.isEmpty()) {
            scheduleManager.deleteAll(schedules);
            return new ResponseEntity<>(schedules,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found schedules to delete!", HttpStatus.NOT_FOUND);
    }
}