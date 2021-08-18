package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Manager.*;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/summations")
public class SummationsController {

    private final ReportManager reportManager;
    private final StopPassengersPairManager stopPassengersPairManager;
    private final CoursesManager coursesManager;
    private final EmployeesManager employeesManager;
    private final CarManager carManager;

    @Autowired
    public SummationsController(ReportManager reportManager, StopPassengersPairManager stopPassengersPairManager, CoursesManager coursesManager, EmployeesManager employeesManager, CarManager carManager) {
        this.reportManager = reportManager;
        this.stopPassengersPairManager = stopPassengersPairManager;
        this.coursesManager = coursesManager;
        this.employeesManager = employeesManager;
        this.carManager = carManager;
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity daily(String date) {
        List<Report> ktToKrk = coursesManager.findByDate(LocalDate.parse(date), Route.KatowiceToKrakow);
        List<Report> krkToKt = coursesManager.findByDate(LocalDate.parse(date), Route.KrakowToKatowice);

        List<SummationsModel> finish = dataMappingReport(ktToKrk, krkToKt);

        if(ktToKrk.isEmpty() && krkToKt.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(finish, HttpStatus.OK);
    }

    private static class SummationsModel{
        private String firstName;
        private String lastName;
        private long income;
        private long refuelingCost;
        private long balance;
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

        public SummationsModel(String firstName, String lastName, long income, long refuelingCost, long balance, Set<StopPassengersPairSetModel> stopPassengersPairSetModels) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.income = income;
            this.refuelingCost = refuelingCost;
            this.balance = balance;
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

        public long getBalance() {
            return balance;
        }

        public void setBalance(long income, long refuelingCost) {
            this.balance = income - refuelingCost;
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
        employees.stream().map(temp ->{
            models.add(new SummationsModel(temp.getFirstName(), temp.getLastName(), 0, 0, 0, null));
            return models;
        });

        List<Car> cars = carManager.findAll();
        cars.stream().map(temp ->{
            models.add(new SummationsModel(temp.getModel(), temp.getBrand(), 0, 0, 0, null));
            return models;
        });
    }

    public List<SummationsModel> dataMappingReport(List<Report> ktToKrk, List<Report> krkToKt){
        List<SummationsModel> summationsModels = new ArrayList<>();
        fillWithDriverAndCar(summationsModels);
        List<SummationsModel> summationsModelsKrkToKt = new ArrayList<>();
        fillWithDriverAndCar(summationsModelsKrkToKt);

        ktToKrk.stream().map(temp -> {
            for (SummationsModel s : summationsModels){
                if (s.getFirstName().equals(temp.getCourses().getDriver().getFirstName()) &&
                        s.getLastName().equals(temp.getCourses().getDriver().getLastName())){
                    s.setIncome(temp.getIncome());
                    s.setRefuelingCost(temp.getRefuelingCost());

                    temp.getAmountOfPassengers().stream().map(t -> {
                        AtomicInteger counter = new AtomicInteger();
                        counter.set(0);
                        s.getStopPassengersPairSetModels().stream().map(st -> {
                            if (st.getStop().equals(t.getStop()) && st.getRoute().equals(Route.KatowiceToKrakow)){
                                st.setPassengers(st.getPassengers() + t.getPassengers());
                                return st;
                            } else {
                                return counter.getAndIncrement();
                            }
                        });
                        if (counter.get() == s.getStopPassengersPairSetModels().size()){
                            SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = null;
                            stopPassengersPairSetModel.setRoute(Route.KatowiceToKrakow);
                            stopPassengersPairSetModel.setStop(t.getStop());
                            stopPassengersPairSetModel.setPassengers(t.getPassengers());
                            s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                        }
                        return s;
                    });
                } else if (s.getFirstName().equals(temp.getCourses().getCar().getModel()) &&
                        s.getLastName().equals(temp.getCourses().getCar().getBrand())){
                    s.setIncome(temp.getIncome());
                    s.setRefuelingCost(temp.getRefuelingCost());

                    temp.getAmountOfPassengers().stream().map(t -> {
                        AtomicInteger counter = new AtomicInteger();
                        counter.set(0);
                        s.getStopPassengersPairSetModels().stream().map(st -> {
                            if (st.getStop().equals(t.getStop()) && st.getRoute().equals(Route.KrakowToKatowice)){
                                st.setPassengers(st.getPassengers() + t.getPassengers());
                                return st;
                            } else {
                                return counter.getAndIncrement();
                            }
                        });
                        if (counter.get() == s.getStopPassengersPairSetModels().size()){
                            SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = null;
                            stopPassengersPairSetModel.setRoute(Route.KrakowToKatowice);
                            stopPassengersPairSetModel.setStop(t.getStop());
                            stopPassengersPairSetModel.setPassengers(t.getPassengers());
                            s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                        }
                        return s;
                    });
                }
            }

            return summationsModels;
        });

        krkToKt.stream().map(temp -> {
            for (SummationsModel s : summationsModelsKrkToKt){
                if (s.getFirstName().equals(temp.getCourses().getDriver().getFirstName()) &&
                        s.getLastName().equals(temp.getCourses().getDriver().getLastName())){
                    s.setIncome(temp.getIncome());
                    s.setRefuelingCost(temp.getRefuelingCost());

                    temp.getAmountOfPassengers().stream().map(t -> {
                        AtomicInteger counter = new AtomicInteger();
                        counter.set(0);
                        s.getStopPassengersPairSetModels().stream().map(st -> {
                            if (st.getStop().equals(t.getStop()) && st.getRoute().equals(Route.KatowiceToKrakow)){
                                st.setPassengers(st.getPassengers() + t.getPassengers());
                                return st;
                            } else {
                                return counter.getAndIncrement();
                            }
                        });
                        if (counter.get() == s.getStopPassengersPairSetModels().size()){
                            SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = null;
                            stopPassengersPairSetModel.setRoute(Route.KatowiceToKrakow);
                            stopPassengersPairSetModel.setStop(t.getStop());
                            stopPassengersPairSetModel.setPassengers(t.getPassengers());
                            s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                        }
                        return s;
                    });
                } else if (s.getFirstName().equals(temp.getCourses().getCar().getModel()) &&
                        s.getLastName().equals(temp.getCourses().getCar().getBrand())){
                    s.setIncome(temp.getIncome());
                    s.setRefuelingCost(temp.getRefuelingCost());

                    temp.getAmountOfPassengers().stream().map(t -> {
                        AtomicInteger counter = new AtomicInteger();
                        counter.set(0);
                        s.getStopPassengersPairSetModels().stream().map(st -> {
                            if (st.getStop().equals(t.getStop()) && st.getRoute().equals(Route.KrakowToKatowice)){
                                st.setPassengers(st.getPassengers() + t.getPassengers());
                                return st;
                            } else {
                                return counter.getAndIncrement();
                            }
                        });
                        if (counter.get() == s.getStopPassengersPairSetModels().size()){
                            SummationsModel.StopPassengersPairSetModel stopPassengersPairSetModel = null;
                            stopPassengersPairSetModel.setRoute(Route.KrakowToKatowice);
                            stopPassengersPairSetModel.setStop(t.getStop());
                            stopPassengersPairSetModel.setPassengers(t.getPassengers());
                            s.getStopPassengersPairSetModels().add(stopPassengersPairSetModel);
                        }
                        return s;
                    });
                }
            }
            return summationsModelsKrkToKt;
        });
        summationsModels.addAll(summationsModelsKrkToKt);
        return summationsModels;
    }
}
