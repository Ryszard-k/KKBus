package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Customer.Controller.RewardController;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Manager.AvailabilityManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.Availability;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.AvailabilityRepo;
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
@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private AvailabilityManager availabilityManager;

    @MockBean
    private AvailabilityRepo availabilityRepo;

    @MockBean
    private EmployeesManager employeesManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Availability> availabilityList() {
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 1, LocalDate.parse("2021-02-24"), employeesList().get(0)));
        availabilities.add(new Availability((long) 2, LocalDate.parse("2021-05-14"), employeesList().get(0)));
        availabilities.add(new Availability((long) 3, LocalDate.parse("2021-09-11"), employeesList().get(1)));
        return availabilities;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, 3000));
        return employees;
    }

    @Test
    void getAll() throws Exception {
        when(availabilityManager.findAll()).thenReturn(availabilityList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/availability")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(availabilityList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(1)).findAll();
    }

    @Test
    void getReservation_notFound() throws Exception {

        when(availabilityManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/availability")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(1)).findAll();
    }

    @Test
    void getById_notFound() throws Exception {
        when(availabilityManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/availability/{id}", availabilityList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(1)).findById(anyLong());
    }

    @Test
    void getById() throws Exception {
        when(availabilityManager.findById(anyLong())).thenReturn(Optional.ofNullable(availabilityList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/availability/{id}", availabilityList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(availabilityList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(1)).findById(anyLong());
    }

    @Test
    void addAvailabilities() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 4, LocalDate.parse("2021-11-24"), employees));
        availabilities.add(new Availability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employees));
        when(availabilityManager.saveAll(anyList())).thenReturn(availabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/availability/{id}", employees.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(availabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(availabilities);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(employees.getId());
        verify(availabilityManager, times(1)).saveAll(availabilities);
    }

    @Test
    void addAvailabilities_nullEmployee_returnFalse() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 4, LocalDate.parse("2021-11-24"), employees));
        availabilities.add(new Availability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(availabilityManager.saveAll(anyList())).thenReturn(availabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/availability/{id}", employees.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(availabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(employees.getId());
        verify(availabilityManager, times(0)).saveAll(availabilities);
    }

    @Test
    void addAvailabilities_nullAvailabilities_returnFalse() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 4, LocalDate.parse("2021-11-24"), employees));
        availabilities.add(new Availability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employees));
        when(availabilityManager.saveAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/availability/{id}", employees.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(List.of()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(employees.getId());
        verify(availabilityManager, times(0)).saveAll(List.of());
    }

    @Test
    void deleteAvailabilities() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 4, LocalDate.parse("2021-11-24"), employees));
        availabilities.add(new Availability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(availabilityManager.deleteAll(anyList())).thenReturn(availabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/availability")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(availabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(availabilities);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(1)).deleteAll(availabilities);
    }

    @Test
    void deleteAvailabilities_emptyList() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 4, LocalDate.parse("2021-11-24"), employees));
        availabilities.add(new Availability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(availabilityManager.deleteAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/availability")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(List.of()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found availabilities to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(availabilityManager, times(0)).deleteAll(availabilities);
    }
}