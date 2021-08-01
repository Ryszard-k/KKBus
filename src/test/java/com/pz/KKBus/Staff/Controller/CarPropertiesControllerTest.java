package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CarPropertiesManager;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.CarPropertiesRepo;
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
@WebMvcTest(CarPropertiesController.class)
class CarPropertiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CarPropertiesManager carPropertiesManager;

    @MockBean
    private CarPropertiesRepo carPropertiesRepo;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private CarManager carManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<CarProperties> carPropertiesList() {
        List<CarProperties> cars = new ArrayList<>();
        cars.add(new CarProperties((long) 1, carList().get(0), LocalDate.now(), State.Available, "parking1"));
        cars.add(new CarProperties((long) 2, carList().get(1), LocalDate.now(), State.Broken, "parking2"));
        cars.add(new CarProperties((long) 3, carList().get(0), LocalDate.now().plusDays(2), State.Available, "parking1"));
        return cars;
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
        when(carPropertiesManager.findAll()).thenReturn(carPropertiesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carPropertiesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findAll();
    }

    @Test
    void getAll_notFound() throws Exception {
        when(carPropertiesManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findAll();
    }

    @Test
    void getByCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.ofNullable(carList().get(0)));
        when(carPropertiesManager.findByCar(any(Car.class))).thenReturn(carPropertiesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carPropertiesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(carPropertiesManager, times(1)).findByCar(any(Car.class));
    }

    @Test
    void getByCar_emptyCar() throws Exception {
        when(carManager.findById(anyLong())).thenReturn(Optional.empty());
        when(carPropertiesManager.findByCar(any(Car.class))).thenReturn(carPropertiesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties/car/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carManager, times(1)).findById(anyLong());
        verify(carPropertiesManager, times(0)).findByCar(any(Car.class));
    }

    @Test
    void getByDate() throws Exception {
        when(carPropertiesManager.findByDate(any(LocalDate.class))).thenReturn(carPropertiesList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties/{date}", LocalDate.parse("2021-11-12"))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carPropertiesList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    void getByDate_null() throws Exception {
        when(carPropertiesManager.findByDate(any(LocalDate.class))).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/carProperties/{date}", LocalDate.parse("2021-11-12"))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad date";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    void addCarProperties() throws Exception {
        when(carPropertiesManager.save(any(CarProperties.class))).thenReturn(carPropertiesList().get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/carProperties")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(carPropertiesList().get(0)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(carPropertiesList().get(0)));

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).save(any(CarProperties.class));
    }

    @Test
    void addCar_withEmptyCarProperties() throws Exception {
        when(carPropertiesManager.save(any(CarProperties.class))).thenReturn(carPropertiesList().get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/carProperties")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(carPropertiesManager, times(0)).save(any(CarProperties.class));
    }

    @Test
    void deleteCarProperties() throws Exception {
        when(carPropertiesManager.findById(anyLong())).thenReturn(Optional.ofNullable(carPropertiesList().get(0)));
        when(carPropertiesManager.deleteById(anyLong())).thenReturn(Optional.of(carPropertiesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/carProperties/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(carPropertiesList().get(0));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findById(anyLong());
        verify(carPropertiesManager, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCar_withNullCarProperties() throws Exception {
        when(carPropertiesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(carPropertiesManager.deleteById(anyLong())).thenReturn(Optional.of(carPropertiesList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/carProperties/{id}", carList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found car properties to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(carPropertiesManager, times(1)).findById(anyLong());
        verify(carPropertiesManager, times(0)).deleteById(anyLong());
    }
}