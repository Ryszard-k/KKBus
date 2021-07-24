package com.pz.KKBus.Customer.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pz.KKBus.Customer.Controller.RewardController;
import com.pz.KKBus.Customer.Manager.CustomerManager;
import com.pz.KKBus.Customer.Manager.MailManager;
import com.pz.KKBus.Customer.Manager.ReservationManager;
import com.pz.KKBus.Customer.Manager.RewardManager;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Customer.Model.Repositories.RewardRepo;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private RewardManager rewardManager;

    @MockBean
    private RewardRepo rewardRepo;

    @MockBean
    private ReservationManager reservationManager;

    @MockBean
    private CustomerManager customerManager;

    @MockBean
    private KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;

    @MockBean
    private KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;

    @MockBean
    private MailManager mailManager;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Reward> rewardsList() {
        List<Reward> rewards = new ArrayList<>();
        rewards.add(new Reward((long) 1, "Discount -30%", 30, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized));
        rewards.add(new Reward((long) 1, "Discount -20%", 40, LocalDate.parse("2021-06-25"), customerList().get(0),
                RewardStatus.Unrealized));
        return rewards;
    }

    private List<Customer> customerList() {
        List<Customer> customers = new ArrayList<>();

        customers.add(new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true));
        return customers;
    }

    @Test
    void getAll() throws Exception {
        when(rewardManager.findAll()).thenReturn(rewardsList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/reward")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(rewardsList());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findAll();
    }

    @Test
    void getReservation_notFound() throws Exception {

        when(rewardManager.findAll()).thenReturn(List.of());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/reward")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Repository is empty!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findAll();
    }

    @Test
    void getById_notFound() throws Exception {
        when(rewardManager.findById(anyLong())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/reward/{id}", rewardsList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Bad id";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findById(anyLong());
    }

    @Test
    void getById() throws Exception {
        when(rewardManager.findById(anyLong())).thenReturn(Optional.ofNullable(rewardsList().get(0)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/reward/{id}", rewardsList().get(0).getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(Optional.ofNullable(rewardsList().get(0)));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findById(anyLong());
    }

    @Test
    void addReward() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized);

        when(customerManager.findByUsername(anyString())).thenReturn(Optional.of(customer));
        when(rewardManager.save(reward, Optional.of(customer))).thenReturn(reward);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/reward/{username}", customer.getUsername())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(reward))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(reward);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(customerManager, times(1)).findByUsername(customer.getUsername());
        verify(rewardManager, times(1)).save(reward, Optional.of(customer));
    }

    @Test
    void addReward_nullCustomer_returnFalse() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized);

        when(customerManager.findByUsername(anyString())).thenReturn(Optional.empty());
        when(rewardManager.save(reward, Optional.of(customer))).thenReturn(reward);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/reward/{username}", customer.getUsername())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(reward))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Empty input data";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(customerManager, times(1)).findByUsername(customer.getUsername());
        verify(rewardManager, times(0)).save(reward, Optional.of(customer));
    }

    @Test
    void addReward_nullReward_returnFalse() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized);

        when(customerManager.findByUsername(anyString())).thenReturn(Optional.of(customer));
        when(rewardManager.save(reward, Optional.of(customer))).thenReturn(reward);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/reward/{username}", customer.getUsername())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "";

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(expectedResponseBody, actualResponseBody);

        verify(customerManager, times(0)).findByUsername(customer.getUsername());
        verify(rewardManager, times(0)).save(reward, Optional.of(customer));
    }

    @Test
    void deleteReward() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized);

        when(rewardManager.findById(anyLong())).thenReturn(Optional.of(reward));
        when(rewardManager.deleteById(anyLong())).thenReturn(Optional.of(reward));
        when(customerManager.update(customer)).thenReturn(customer);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/reward/{id}", customer.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = mapper.writeValueAsString(reward);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findById(anyLong());
        verify(rewardManager, times(1)).deleteById(anyLong());
        verify(customerManager, times(1)).update(customer);
    }

    @Test
    void deleteReward_NullReward_returnFalse() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized);

        when(rewardManager.findById(anyLong())).thenReturn(Optional.empty());
        when(rewardManager.deleteById(anyLong())).thenReturn(Optional.of(reward));
        when(customerManager.update(customer)).thenReturn(customer);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/reward/{id}", customer.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found reward to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findById(anyLong());
        verify(rewardManager, times(0)).deleteById(anyLong());
        verify(customerManager, times(0)).update(customer);
    }

    @Test
    void deleteReward_falseIf_returnFalse() throws Exception {
        Customer customer = new Customer((long) 1,"Marek","Kowalski",LocalDate.parse("1983-02-23"),"piotr.wojcik543@gmail.com",
                123456789,"kowalski", "kowalski123", Role.CustomerEnabled,true);

        Reward reward = new Reward((long) 3, "Discount -45%", 50, LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Realized);

        when(rewardManager.findById(anyLong())).thenReturn(Optional.of(reward));
        when(rewardManager.deleteById(anyLong())).thenReturn(Optional.of(reward));
        when(customerManager.update(customer)).thenReturn(customer);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/reward/{id}", customer.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = "Not found reward to delete!";

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(actualResponseBody, expectedResponseBody);

        verify(rewardManager, times(1)).findById(anyLong());
        verify(rewardManager, times(0)).deleteById(anyLong());
        verify(customerManager, times(0)).update(customer);
    }
}