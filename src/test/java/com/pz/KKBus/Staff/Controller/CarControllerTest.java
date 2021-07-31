package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Manager.AvailabilityManager;
import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Repositories.AvailabilityRepo;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CarManager carManager;

    @MockBean
    private CarRepo carRepo;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 1, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 2, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 3, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    @Test
    void getAll() throws Exception {
        when(carManager.findAll()).thenReturn(carList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/car")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findAll();
    }

    @Test
    void getAll_notFound() throws Exception {
        when(carManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/car")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findAll();
    }

    @Test
    void getById() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.ofNullable(carList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(carList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
    }

    @Test
    void getById_notFound() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
    }

    @Test
    void addCar() throws Exception {
        when(carManager.save(any(Car.class))).thenReturn(carList().get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/car")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(carList().get(0)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(carList().get(0)));

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).save(any(Car.class));
    }

    @Test
    void addCar_withEmptyCar() throws Exception {
        when(carManager.save(any(Car.class))).thenReturn(carList().get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/car")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(carManager, times(0)).save(any(Car.class));
    }

    @Test
    void deleteCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.ofNullable(carList().get(0)));
        when(carManager.deleteById(anyLong())).thenReturn(Optional.of(carList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(carList().get(0)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carList().get(0));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(carManager, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCar_withNullCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.empty());
        when(carManager.deleteById(anyLong())).thenReturn(Optional.of(carList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(Optional.empty()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found car to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(carManager, times(0)).deleteById(anyLong());
    }
}