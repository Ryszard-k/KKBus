package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Controller.Schedule.UnavailabilityController;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Manager.Schedule.UnavailabilityManager;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Unavailability;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.Schedule.UnavailabilityRepo;
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
@WebMvcTest(UnavailabilityController.class)
class UnavailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UnavailabilityManager unavailabilityManager;

    @MockBean
    private UnavailabilityRepo unavailabilityRepo;

    @MockBean
    private EmployeesManager employeesManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Unavailability> unavailabilityList() {
        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 1, LocalDate.parse("2021-02-24"), employeesList().get(0)));
        unavailabilities.add(new Unavailability((long) 2, LocalDate.parse("2021-05-14"), employeesList().get(0)));
        unavailabilities.add(new Unavailability((long) 3, LocalDate.parse("2021-09-11"), employeesList().get(1)));
        return unavailabilities;
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
        when(unavailabilityManager.findAll()).thenReturn(unavailabilityList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/unavailability")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(unavailabilityList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(1)).findAll();
    }

    @Test
    void getReservation_notFound() throws Exception {

        when(unavailabilityManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/unavailability")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(1)).findAll();
    }

    @Test
    void getById_notFound() throws Exception {
        when(unavailabilityManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/unavailability/{id}", unavailabilityList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(1)).findById(anyLong());
    }

    @Test
    void getById() throws Exception {
        when(unavailabilityManager.findById(anyLong())).thenReturn(Optional.ofNullable(unavailabilityList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/unavailability/{id}", unavailabilityList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(unavailabilityList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(1)).findById(anyLong());
    }

    @Test
    void addUnavailabilities() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 4, LocalDate.parse("2021-11-24"), employees));
        unavailabilities.add(new Unavailability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employees));
        when(unavailabilityManager.saveAll(anyList())).thenReturn(unavailabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/unavailability/{id}", employees.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(unavailabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(unavailabilities);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(employees.getId());
        verify(unavailabilityManager, times(1)).saveAll(unavailabilities);
    }

    @Test
    void addUnavailabilities_nullEmployee_returnFalse() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 4, LocalDate.parse("2021-11-24"), employees));
        unavailabilities.add(new Unavailability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(unavailabilityManager.saveAll(anyList())).thenReturn(unavailabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/unavailability/{id}", employees.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(unavailabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(employees.getId());
        verify(unavailabilityManager, times(0)).saveAll(unavailabilities);
    }

    @Test
    void addUnavailabilities_nullUnavailabilities_returnFalse() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 4, LocalDate.parse("2021-11-24"), employees));
        unavailabilities.add(new Unavailability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employees));
        when(unavailabilityManager.saveAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/unavailability/{id}", employees.getId())
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
        verify(unavailabilityManager, times(0)).saveAll(List.of());
    }

    @Test
    void deleteUnavailabilities() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 4, LocalDate.parse("2021-11-24"), employees));
        unavailabilities.add(new Unavailability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(unavailabilityManager.deleteAll(anyList())).thenReturn(unavailabilities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/unavailability")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(unavailabilities))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(unavailabilities);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(1)).deleteAll(unavailabilities);
    }

    @Test
    void deleteUnavailabilities_emptyList() throws Exception {
        Employees employees = new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000);

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 4, LocalDate.parse("2021-11-24"), employees));
        unavailabilities.add(new Unavailability((long) 5, LocalDate.parse("2021-05-14"), employees));

        when(unavailabilityManager.deleteAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/unavailability")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(List.of()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found unavailabilities to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(unavailabilityManager, times(0)).deleteAll(unavailabilities);
    }
}