package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Manager.*;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/summations")
public class SummationsController {

    private final EmployeesManager employeesManager;
    private final CarManager carManager;
    private final ReportManager reportManager;

    @Autowired
    public SummationsController(EmployeesManager employeesManager, CarManager carManager, ReportManager reportManager) {
        this.employeesManager = employeesManager;
        this.carManager = carManager;
        this.reportManager = reportManager;
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity daily(@PathVariable String date) {
        List<Report> ktToKrk = reportManager.findAll().stream()
                .filter(k -> k.getCourses().getDate().equals(LocalDate.parse(date)))
                .filter(k -> k.getCourses().getRoute().equals(Route.KatowiceToKrakow))
                .collect(Collectors.toList());
        List<Report> krkToKt = reportManager.findAll().stream()
                .filter(k -> k.getCourses().getDate().equals(LocalDate.parse(date)))
                .filter(k -> k.getCourses().getRoute().equals(Route.KrakowToKatowice))
                .collect(Collectors.toList());

        List<SummationsModel> finish = dataMappingReport(ktToKrk, krkToKt);

        if(ktToKrk.isEmpty() && krkToKt.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(finish, HttpStatus.OK);
    }

    @GetMapping("/freely/{fromDate}/{toDate}")
    public ResponseEntity freelyDates(@PathVariable String fromDate, @PathVariable String toDate) {
        List<Report> ktToKrk = reportManager.findAll().stream()
                .filter(k -> k.getCourses().getDate().isAfter(LocalDate.parse(fromDate).minusDays(1)))
                .filter(k -> k.getCourses().getDate().isBefore(LocalDate.parse(toDate).plusDays(1)))
                .filter(k -> k.getCourses().getRoute().equals(Route.KatowiceToKrakow))
                .collect(Collectors.toList());

        List<Report> krkToKt = reportManager.findAll().stream()
                .filter(k -> k.getCourses().getDate().isAfter(LocalDate.parse(fromDate).minusDays(1)))
                .filter(k -> k.getCourses().getDate().isBefore(LocalDate.parse(toDate).plusDays(1)))
                .filter(k -> k.getCourses().getRoute().equals(Route.KrakowToKatowice))
                .collect(Collectors.toList());

        List<SummationsModel> finish = dataMappingReport(ktToKrk, krkToKt);

        if(ktToKrk.isEmpty() && krkToKt.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(finish, HttpStatus.OK);
    }

    protected static class SummationsModel{
        private String firstName;
        private String lastName;
        private long income;
        private long refuelingCost;
        private long distance;
        private Set<StopPassengersPairSetModel> stopPassengersPairSetModels;

        private static class StopPassengersPairSetModel{

            private Route route;
            private String stop;
            private int passengers;

            public String getStop() {
                return stop;
            }

            public void setStop(String stop) {
                this.stop = stop;
            }

            public int getPassengers() {
                return passengers;
            }

            public void setPassengers(int passengers) {
                this.passengers += passengers;
            }

            public Route getRoute() {
                return route;
            }

            public void setRoute(Route route) {
                this.route = route;
            }
        }

        public SummationsModel(String firstName, String lastName, long income, long refuelingCost, long distance, Set<StopPassengersPairSetModel> stopPassengersPairSetModels) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.income = income;
            this.refuelingCost = refuelingCost;
            this.distance = distance;
            this.stopPassengersPairSetModels = stopPassengersPairSetModels;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public long getIncome() {
            return income;
        }

        public void setIncome(long income) {
            this.income += income;
        }

        public long getRefuelingCost() {
            return refuelingCost;
        }

        public void setRefuelingCost(long refuelingCost) {
            this.refuelingCost += refuelingCost;
        }

        public long getDistance() {
            return distance;
        }

        public void setDistance(long distance) {
            this.distance += distance;
        }

        public Set<StopPassengersPairSetModel> getStopPassengersPairSetModels() {
            return stopPassengersPairSetModels;
        }

        public void setStopPassengersPairSetModels(Set<StopPassengersPairSetModel> stopPassengersPairSetModels) {
            this.stopPassengersPairSetModels = stopPassengersPairSetModels;
        }
    }

    public void fillWithDriverAndCar(List<SummationsModel> models){
        List<Employees> employees = employeesManager.findByRole(Role.Driver);
        List<SummationsModel> employeesStream = employees.stream().map(temp ->
                new SummationsModel(temp.getFirstName(), temp.getLastName(), 0, 0, 0,
                        new HashSet<>()))
                .collect(Collectors.toList());

        List<Car> cars = carManager.findAll();
        List<SummationsModel> carsStream = cars.stream().map(temp ->
            new SummationsModel(temp.getBrand(), temp.getModel(), 0, 0, 0,
                    new HashSet<>())
        ).collect(Collectors.toList());

        models.addAll(employeesStream);
        models.addAll(carsStream);
    }

    public List<SummationsModel> dataMappingReport(List<Report> ktToKrk, List<Report> krkToKt){
        List<SummationsModel> summationsModels = new ArrayList<>();
        fillWithDriverAndCar(summationsModels);
        AtomicInteger counter = new AtomicInteger();

        for (SummationsModel s : summationsModels){
            ktToKrk.stream()
                    .filter(k -> (s.getFirstName().equals(k.getCourses().getDriver().getFirstName()) &&
                            s.getLastName().equals(k.getCourses().getDriver().getLastName()) ||
                            (s.getFirstName().equals(k.getCourses().getCar().getBrand()) &&
                                    s.getLastName().equals(k.getCourses().getCar().getModel()))))
                    .forEach(k -> {
                        s.setIncome(k.getIncome());
                        s.setRefuelingCost(k.getRefuelingCost());
                        s.setDistance(k.getDistance());

                        for (StopPassengersPair ka : k.getAmountOfPassengers()){
                            counter.set(0);
                            for (SummationsModel.StopPassengersPairSetModel sa : s.getStopPassengersPairSetModels()) {
                                if (ka.getStop().equals(sa.getStop()) && ka.getReport().getCourses()
                                        .getRoute().equals(Route.KatowiceToKrakow)) {
                                    sa.setPassengers(ka.getPassengers());
                                    counter.getAndIncrement();
                                }
                            }
                            if (counter.get() == 0){
                                SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = new SummationsModel.StopPassengersPairSetModel();
                                stopPassengersPairSetModel.setRoute(Route.KatowiceToKrakow);
                                stopPassengersPairSetModel.setStop(ka.getStop());
                                stopPassengersPairSetModel.setPassengers(ka.getPassengers());
                                s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                            }
                        }
                    });

            krkToKt.stream()
                    .filter(k -> (s.getFirstName().equals(k.getCourses().getDriver().getFirstName()) &&
                            s.getLastName().equals(k.getCourses().getDriver().getLastName()) ||
                            (s.getFirstName().equals(k.getCourses().getCar().getBrand()) &&
                                    s.getLastName().equals(k.getCourses().getCar().getModel()))))
                    .forEach(k -> {
                        s.setIncome(k.getIncome());
                        s.setRefuelingCost(k.getRefuelingCost());
                        s.setDistance(k.getDistance());
                        
                        for (StopPassengersPair ka : k.getAmountOfPassengers()){
                            counter.set(0);
                            for (SummationsModel.StopPassengersPairSetModel sa : s.getStopPassengersPairSetModels()) {
                                if (ka.getStop().equals(sa.getStop()) && ka.getReport().getCourses()
                                        .getRoute().equals(Route.KrakowToKatowice)) {
                                    sa.setPassengers(ka.getPassengers());
                                    counter.getAndIncrement();
                                }
                            }
                            if (counter.get() == 0){
                                SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = new SummationsModel.StopPassengersPairSetModel();
                                stopPassengersPairSetModel.setRoute(Route.KrakowToKatowice);
                                stopPassengersPairSetModel.setStop(ka.getStop());
                                stopPassengersPairSetModel.setPassengers(ka.getPassengers());
                                s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                            }
                        }
                    });
        }
        return summationsModels;
    }
}
