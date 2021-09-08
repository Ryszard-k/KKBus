package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Customer.Manager.ReservationManager;
import com.pz.KKBus.Customer.Model.ReportWithReservationsIds;
import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Enums.Status;
import com.pz.KKBus.Staff.Manager.CoursesManager;
import com.pz.KKBus.Staff.Manager.ReportManager;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/report")
public class ReportController implements CrudControllerMethods<ReportWithReservationsIds> {

    private final ReportManager reportManager;
    private final ReservationManager reservationManager;
    private final CoursesManager coursesManager;

    @Autowired
    public ReportController(ReportManager reportManager, ReservationManager reservationManager, CoursesManager coursesManager) {
        this.reportManager = reportManager;
        this.reservationManager = reservationManager;
        this.coursesManager = coursesManager;
    }

    @Override
    @GetMapping()
    public ResponseEntity getAll() {
        List<Report> found = reportManager.findAll();
        if(found.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        Optional<Report> founded = reportManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(founded, HttpStatus.OK);
        }
    }

    @GetMapping("/course/{id}")
    public ResponseEntity getByCourse(@PathVariable Long id) {
        Optional<Report> founded = reportManager.findByCourses(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(founded, HttpStatus.OK);
        }
    }

    @Override
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> add(@RequestBody ReportWithReservationsIds object) {
        List<Reservation> reservationList = reservationManager.findByDate(object.getReport().getCourses().getDate());
        for (Reservation r : reservationList){
            object.getIds().stream().forEach(temp ->{
                if (temp.equals(r.getId())){
                    r.setStatus(Status.Realized);
                    reservationManager.cleanSave(r);
                }
            });
        }
        if (object == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else {
            Report reports1 = (Report) object.getReport().getAmountOfPassengers().stream()
                    .peek(temp -> temp.setReport(object.getReport()));
            reportManager.save(reports1);
            return new ResponseEntity<>(object, HttpStatus.CREATED);
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        Optional<Report> report = reportManager.findById(id);
        if (report.isPresent()) {
            reportManager.deleteById(id);
            return new ResponseEntity<>(report,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found report to delete!", HttpStatus.NOT_FOUND);
    }
}
