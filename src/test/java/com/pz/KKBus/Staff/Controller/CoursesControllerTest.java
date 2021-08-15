package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CarPropertiesManager;
import com.pz.KKBus.Staff.Manager.CoursesManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.Courses.CoursesRepo;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CoursesController.class)
class CoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CoursesRepo coursesRepo;

    @MockBean
    private CoursesManager coursesManager;

    @MockBean
    private EmployeesManager employeesManager;

    @MockBean
    private CarManager carManager;

    @MockBean
    private CarPropertiesManager carPropertiesManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<KrakowToKatowiceDeparture> KrakowToKatowiceDepartureList() {
        List<KrakowToKatowiceDeparture> krakowToKatowiceDepartures = new ArrayList<>();
        KrakowToKatowice krakowToKatowice = new KrakowToKatowice((long) 1, "Przystanek1", null
                , 0, 0);
        KrakowToKatowice krakowToKatowice1 = new KrakowToKatowice((long) 2, "Przystanek2", null
                , 10, 40);

        krakowToKatowiceDepartures.add(new KrakowToKatowiceDeparture((long) 1, LocalTime.parse("08:01"),
                LocalTime.parse("11:47"), krakowToKatowice));
        krakowToKatowiceDepartures.add(new KrakowToKatowiceDeparture((long) 2, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), krakowToKatowice1));

        return krakowToKatowiceDepartures;
    }
    private List<Courses> coursesList() {
        List<Courses> courses = new ArrayList<>();

        courses.add(new Courses((long) 1, LocalDate.parse("2021-04-23"), Route.KatowiceToKrakow,
                LocalTime.parse("8:01"), carList().get(1), employeesList().get(0)));
        courses.add(new Courses((long) 2, LocalDate.parse("2021-04-23"),Route.KatowiceToKrakow,
                LocalTime.parse("11:47") , carList().get(1), employeesList().get(0)));
        courses.add(new Courses((long) 3, LocalDate.parse("2021-04-23"),Route.KrakowToKatowice ,
                LocalTime.parse("16:47"), carList().get(1), employeesList().get(0)));
        return courses;
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 1, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 2, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 3, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();
        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, 3000));
        employees.add(new Employees((long) 3,"Andrzej",
                "Konrad", LocalDate.parse("1988-05-20"), Role.Driver, 4000));
        return employees;
    }

    private List<CarProperties> carPropertiesList() {
        List<CarProperties> cars = new ArrayList<>();
        cars.add(new CarProperties((long) 1, carList().get(0), LocalDate.now(), State.Available, "parking1"));
        cars.add(new CarProperties((long) 2, carList().get(1), LocalDate.now(), State.Broken, "parking2"));
        cars.add(new CarProperties((long) 3, carList().get(0), LocalDate.now().plusDays(2), State.Available, "parking1"));
        return cars;
    }

    @Test
    void getAll() throws Exception {
        when(coursesManager.findAll()).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(coursesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findAll();
    }

    @Test
    void getAll_notFound() throws Exception {
        when(coursesManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findAll();
    }

    @Test
    void getById() throws Exception {
        when(coursesManager.findById(anyLong())).thenReturn(Optional.ofNullable(coursesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/{id}", coursesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(coursesList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findById(anyLong());
    }

    @Test
    void getById_notFound() throws Exception {
        when(coursesManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/{id}", coursesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findById(anyLong());
    }

    @Test
    void findByDriver() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(2)));
        when(coursesManager.findByDriver(any(Employees.class))).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/driver/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(coursesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(coursesManager, times(1)).findByDriver(any(Employees.class));
    }

    @Test
    void findByDriver_emptyDriver() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(coursesManager.findByDriver(any(Employees.class))).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/driver/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(coursesManager, times(0)).findByDriver(any(Employees.class));
    }

    @Test
    void findByDriver_notDriver() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(1)));
        when(coursesManager.findByDriver(any(Employees.class))).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/driver/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(coursesManager, times(0)).findByDriver(any(Employees.class));
    }

    @Test
    void findByCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.ofNullable(carList().get(0)));
        when(coursesManager.findByCar(any(Car.class))).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(coursesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(coursesManager, times(1)).findByCar(any(Car.class));
    }

    @Test
    void getByCar_emptyCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.empty());
        when(coursesManager.findByCar(any(Car.class))).thenReturn(coursesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/courses/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(coursesManager, times(0)).findByCar(any(Car.class));
    }

    @Test
    void add() throws Exception {
        when(carPropertiesManager.findByCarAndDate(any(Car.class), any(LocalDate.class)))
                .thenReturn(Optional.ofNullable(carPropertiesList().get(0)));
        when(coursesManager.save(any(Courses.class))).thenReturn(coursesList().get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/courses")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(coursesList().get(0)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(coursesList().get(0)));

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).save(any(Courses.class));
        verify(carPropertiesManager, times(1)).findByCarAndDate(any(Car.class), any(LocalDate.class));
    }

    @Test
    void addCar_withEmptyCourses() throws Exception {
        when(carPropertiesManager.findByCarAndDate(any(Car.class), any(LocalDate.class)))
                .thenReturn(Optional.ofNullable(carPropertiesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/courses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(carPropertiesManager, times(0)).findByCarAndDate(any(Car.class), any(LocalDate.class));
        verify(coursesManager, times(0)).save(any(Courses.class));
    }

    @Test
    void addCar_withEmptyCarProperties() throws Exception {
        when(carPropertiesManager.findByCarAndDate(any(Car.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/courses")
                .content(this.mapper.writeValueAsString(coursesList().get(0)))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findByCarAndDate(any(Car.class), any(LocalDate.class));
        verify(coursesManager, times(0)).save(any(Courses.class));
    }

    @Test
    void deleteById() throws Exception {
        when(coursesManager.findById(anyLong())).thenReturn(Optional.ofNullable(coursesList().get(0)));
        when(coursesManager.deleteById(anyLong())).thenReturn(Optional.of(coursesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/courses/{id}", coursesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(coursesList().get(0));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findById(anyLong());
        verify(coursesManager, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteById_withBadId() throws Exception {
        when(coursesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(coursesManager.deleteById(anyLong())).thenReturn(Optional.of(coursesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/courses/{id}", coursesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found course to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(coursesManager, times(1)).findById(anyLong());
        verify(coursesManager, times(0)).deleteById(anyLong());
    }
}