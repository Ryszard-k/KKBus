package com.pz.KKBus.Staff.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Controller.Schedule.ScheduleController;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Manager.Schedule.ScheduleManager;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Schedule;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.Schedule.ScheduleRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ScheduleManager scheduleManager;

    @MockBean
    private ScheduleRepo scheduleRepo;

    @MockBean
    private EmployeesManager employeesManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Schedule> scheduleList() {
        List<Schedule> schedule = new ArrayList<>();
        schedule.add(new Schedule((long) 1, LocalDate.parse("2021-12-22"), LocalTime.parse("08:10"), LocalTime.parse("14:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 2, LocalDate.parse("2021-12-23"), LocalTime.parse("09:15"), LocalTime.parse("17:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 3, LocalDate.parse("2021-12-24"), LocalTime.parse("12:10"), LocalTime.parse("18:15"),
                employeesList().get(0)));
        return schedule;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, "KowalskiJan",
                passwordEncoder.encode("KowalskiJan"), 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, "NowakAnna",
                passwordEncoder.encode("NowakAnna"), 3000));
        return employees;
    }

    @Test
    void getAll() throws Exception {
        when(scheduleManager.findAll()).thenReturn(scheduleList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findAll();
    }

    @Test
    void getReservation_notFound() throws Exception {

        when(scheduleManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findAll();
    }

    @Test
    void getById_notFound() throws Exception {
        when(scheduleManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{id}", scheduleList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findById(anyLong());
    }

    @Test
    void getById() throws Exception {
        when(scheduleManager.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{id}", scheduleList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(scheduleList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findById(anyLong());
    }

    @Test
    void getByPeriod() throws Exception {
        when(scheduleManager.findByPeriodAll(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(scheduleList().subList(1,3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{fromDate}/{toDate}", String.valueOf(scheduleList().get(0).getWorkDate()),
        String.valueOf(scheduleList().get(0).getWorkDate().plusDays(4)))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList().subList(1,3));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findByPeriodAll(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getByPeriod_notFound() throws Exception {
        when(scheduleManager.findByPeriodAll(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{fromDate}/{toDate}", String.valueOf(scheduleList().get(0).getWorkDate()),
                        String.valueOf(scheduleList().get(0).getWorkDate().plusDays(4)))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad period";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).findByPeriodAll(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getByEmployeePeriod() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(0)));
        when(scheduleManager.findByPeriodForEmployee(any(Employees.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(scheduleList().subList(1,3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{id}/{fromDate}/{toDate}", employeesList().get(0).getId(),
                        String.valueOf(scheduleList().get(0).getWorkDate()),
                        String.valueOf(scheduleList().get(0).getWorkDate().plusDays(4)))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList().subList(1,3));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(1)).findByPeriodForEmployee(any(Employees.class),
                any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getByEmployeePeriod_notFound_badIdEmployee() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleManager.findByPeriodForEmployee(any(Employees.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(scheduleList().subList(1,3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{id}/{fromDate}/{toDate}", 4L,
                        String.valueOf(scheduleList().get(0).getWorkDate()),
                        String.valueOf(scheduleList().get(0).getWorkDate().plusDays(4)))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(0)).findByPeriodForEmployee(any(Employees.class),
                any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getByEmployeePeriod_notFound_badPeriod() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(0)));
        when(scheduleManager.findByPeriodForEmployee(any(Employees.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/{id}/{fromDate}/{toDate}", employeesList().get(0).getId(),
                        String.valueOf(scheduleList().get(0).getWorkDate()),
                        String.valueOf(scheduleList().get(0).getWorkDate().plusDays(4)))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(1)).findByPeriodForEmployee(any(Employees.class),
                any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getByEmployee() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(0)));
        when(scheduleManager.findByEmployee(any(Employees.class))).thenReturn(scheduleList().subList(1,3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/employee/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList().subList(1,3));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(1)).findByEmployee(any(Employees.class));
    }

    @Test
    void getByEmployee_notFoundEmployee() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleManager.findByEmployee(any(Employees.class))).thenReturn(scheduleList().subList(1,3));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/employee/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(0)).findByEmployee(any(Employees.class));
    }

    @Test
    void getByEmployee_notFoundSchedule() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.ofNullable(employeesList().get(0)));
        when(scheduleManager.findByEmployee(any(Employees.class))).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/schedule/employee/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Schedule not founded";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(1)).findByEmployee(any(Employees.class));
    }

    @Test
    void addSchedules() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employeesList().get(0)));
        when(scheduleManager.saveAll(anyList())).thenReturn(scheduleList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/schedule/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(scheduleList()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList());

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(1)).saveAll(anyList());
    }

    @Test
    void addSchedules_notFoundEmployee() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleManager.saveAll(anyList())).thenReturn(scheduleList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/schedule/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(scheduleList()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(0)).saveAll(anyList());
    }

    @Test
    void addSchedules_notScheduleToAdd() throws Exception {
        when(employeesManager.findById(anyLong())).thenReturn(Optional.of(employeesList().get(0)));
        when(scheduleManager.saveAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/schedule/{id}", employeesList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(List.of()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(employeesManager, times(1)).findById(anyLong());
        verify(scheduleManager, times(0)).saveAll(anyList());
    }

    @Test
    void deleteSchedules() throws Exception {
        when(scheduleManager.deleteAll(anyList())).thenReturn(scheduleList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/schedule")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(scheduleList()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(scheduleList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(1)).deleteAll(scheduleList());
    }

    @Test
    void deleteSchedules_emptyList() throws Exception {
        when(scheduleManager.deleteAll(anyList())).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/schedule")
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(List.of()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found schedules to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(scheduleManager, times(0)).deleteAll(scheduleList());
    }
}