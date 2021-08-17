package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Customer.Manager.ReservationManager;
import com.pz.KKBus.Customer.Model.ReportWithReservationsIds;
import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Enums.Status;
import com.pz.KKBus.Staff.Manager.ReportManager;
import com.pz.KKBus.Staff.Manager.StopPassengersPairManager;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController implements CrudControllerMethods<ReportWithReservationsIds> {

    private final ReportManager reportManager;
    private final ReservationManager reservationManager;
    private final StopPassengersPairManager stopPassengersPairManager;

    @Autowired
    public ReportController(ReportManager reportManager, ReservationManager reservationManager, StopPassengersPairManager stopPassengersPairManager) {
        this.reportManager = reportManager;
        this.reservationManager = reservationManager;
        this.stopPassengersPairManager = stopPassengersPairManager;
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

    @Override
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> add(@RequestBody ReportWithReservationsIds object) {
        List<Reservation> reservationList = reservationManager.findByDate(object.getReport().getCourses().getDate());
        for (Reservation r : reservationList){
            object.getIds().stream().map(temp ->{
                if (temp.equals(r.getId())){
                    r.setStatus(Status.Realized);
                    return reservationManager.cleanSave(r);
                } else return null;
            });
        }
        if (object == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else
        object.getReport().getAmountOfPassengers().stream().map(temp -> {
            temp.setReport(object.getReport());
            return temp;
        }).collect(Collectors.toSet());
        reportManager.save(object.getReport());
        return new ResponseEntity<>(object, HttpStatus.CREATED);
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
