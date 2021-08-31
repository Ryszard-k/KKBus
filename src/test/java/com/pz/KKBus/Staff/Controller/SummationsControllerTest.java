package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CoursesManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Manager.ReportManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
import com.pz.KKBus.Staff.Model.Repositories.Courses.CoursesRepo;
import com.pz.KKBus.Staff.Model.Repositories.Courses.ReportRepo;
import com.pz.KKBus.Staff.Model.Repositories.EmployeesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SummationsController.class)
class SummationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CoursesRepo coursesRepo;

    @MockBean
    private CoursesManager coursesManager;

    @MockBean
    private EmployeesRepo employeesRepo;

    @MockBean
    private EmployeesManager employeesManager;

    @MockBean
    private CarRepo carRepo;

    @MockBean
    private CarManager carManager;

    @MockBean
    private ReportManager reportManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.OfficeWorker, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.Driver, 3000));
        employees.add(new Employees((long) 3,"Marek",
                "Markowski", LocalDate.parse("1997-05-20"), Role.Driver, 3000));
        return employees;
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 1, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 2, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 3, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    private List<Courses> coursesList() {
        List<Courses> courses = new ArrayList<>();
        courses.add(new Courses((long) 1, LocalDate.now(), Route.KatowiceToKrakow, LocalTime.parse("08:11"),
                carList().get(0), employeesList().get(1)));
        courses.add(new Courses((long) 2, LocalDate.now().minusDays(3), Route.KatowiceToKrakow, LocalTime.parse("11:11"),
                carList().get(1), employeesList().get(1)));
        courses.add(new Courses((long) 3, LocalDate.now().plusDays(3), Route.KrakowToKatowice, LocalTime.parse("18:51"),
                carList().get(2), employeesList().get(2)));
        return courses;
    }

    private List<Report> reportList() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report((long) 1, 100, 30, 430,
                coursesList().get(0)));
        Set<StopPassengersPair> set1 = new HashSet<>();
        set1.add(new StopPassengersPair((long) 1, "Przystanek1", 5, reports.get(0)));
        set1.add(new StopPassengersPair((long) 2, "Przystanek2", 5, reports.get(0)));
        reports.get(0).setAmountOfPassengers(set1);

        reports.add(new Report((long) 2, 120, 32, 450,
                coursesList().get(1)));
        set1 = new HashSet<>();
        set1.add(new StopPassengersPair((long) 3, "Przystanek1", 10, reports.get(1)));
        set1.add(new StopPassengersPair((long) 4, "Przystanek4", 15, reports.get(1)));
        reports.get(1).setAmountOfPassengers(set1);

        reports.add(new Report((long) 3, 140, 64, 470,
                coursesList().get(2)));
        set1 = new HashSet<>();
        set1.add(new StopPassengersPair((long) 5, "Przystanek5", 10, reports.get(2)));
        set1.add(new StopPassengersPair((long) 6, "Przystanek6", 20, reports.get(2)));
        reports.get(2).setAmountOfPassengers(set1);

        return reports;
    }

    @Test
    void daily() throws Exception {
        when(reportManager.findAll()).thenReturn(reportList());
        when(employeesManager.findByRole(Role.Driver)).thenReturn(employeesList().subList(1,3));
        when(carManager.findAll()).thenReturn(carList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/summations/daily/{date}", LocalDate.now())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
       // String expectedResponseBody = mapper.writeValueAsString(finish);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
      //  assertEquals(expectedResponseBody, actualResponseBody);

        verify(reportManager, times(2)).findAll();
        verify(employeesManager, times(1)).findByRole(Role.Driver);
        verify(carManager, times(1)).findAll();
    }

    @Test
    void freelyDates() throws Exception {
        when(reportManager.findAll()).thenReturn(reportList());
        when(employeesManager.findByRole(Role.Driver)).thenReturn(employeesList().subList(1,3));
        when(carManager.findAll()).thenReturn(carList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/summations/freely/{fromDate}/{toDate}", LocalDate.now().minusDays(3), LocalDate.now().plusDays(3))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        // String expectedResponseBody = mapper.writeValueAsString(finish);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        //  assertEquals(expectedResponseBody, actualResponseBody);

        verify(reportManager, times(2)).findAll();
        verify(employeesManager, times(1)).findByRole(Role.Driver);
        verify(carManager, times(1)).findAll();
    }
}