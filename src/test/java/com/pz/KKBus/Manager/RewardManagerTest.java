package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Entites.Reward;
import com.pz.KKBus.Model.Enums.RewardStatus;
import com.pz.KKBus.Model.Enums.Role;
import com.pz.KKBus.Model.Enums.Route;
import com.pz.KKBus.Model.Enums.Status;
import com.pz.KKBus.Model.Repositories.RewardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class RewardManagerTest {

    @Mock
    private RewardRepo rewardRepo;

    @InjectMocks
    private RewardManager rewardManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Reward> rewardsList() {
        List<Reward> rewards = new ArrayList<>();
        rewards.add(new Reward((long) 1, "Discount -30%", LocalDate.parse("2021-06-30"), customerList().get(0),
                RewardStatus.Unrealized));
        rewards.add(new Reward((long) 1, "Discount -20%", LocalDate.parse("2021-06-25"), customerList().get(0),
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
    void findAll() {
        when(rewardRepo.findAll()).thenReturn(rewardsList());

        List<Reward> rewards = rewardManager.findAll();

        assertEquals(2, rewardsList().size());
        verify(rewardRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(rewardRepo.findAll()).thenReturn(null);

        List<Reward> rewards = rewardManager.findAll();

        assertNull(rewards);
        verify(rewardRepo, times(1)).findAll();
    }

    @Test
    void findByName() {
        when(rewardRepo.findByName(anyString())).thenReturn(java.util.Optional.ofNullable(rewardsList().get(0)));

        Optional<Reward> reward = rewardManager.findByName(rewardsList().get(0).getName());

        assertEquals(rewardsList().get(0).getId(), reward.get().getId());
        assertEquals(rewardsList().get(0).getDate(), reward.get().getDate());
        assertEquals(rewardsList().get(0).getCustomer().getFirstName(), reward.get().getCustomer().getFirstName());

        verify(rewardRepo, times(1)).findByName(rewardsList().get(0).getName());
    }

    @Test
    void findByName_not_found() {
        when(rewardRepo.findByName(anyString())).thenReturn(null);

        Optional<Reward> reward = rewardManager.findByName(rewardsList().get(0).getName());

        assertNull(reward);
        verify(rewardRepo, times(1)).findByName(rewardsList().get(0).getName());
    }

    @Test
    void save() {
        Reward reward = new Reward((long) 5, "Discount -40%", LocalDate.parse("2021-06-27"), customerList().get(0),
                RewardStatus.Unrealized);
        when(rewardRepo.save(any(Reward.class))).thenReturn(reward);

        Reward reward1 = rewardManager.save(reward, Optional.ofNullable(customerList().get(0)));

        assertEquals(reward1.getId(), reward.getId());
        assertEquals(reward1.getName(), reward.getName());
        assertEquals(reward1.getCustomer().getFirstName(), reward.getCustomer().getFirstName());
        verify(rewardRepo, times(1)).save(reward);
    }

    @Test
    void save_with_null_reward() {
        when(rewardRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> rewardManager.save(null,
                Optional.ofNullable(customerList().get(0))));
    }

    @Test
    void save_with_null_customer() {
        Reward reward = new Reward((long) 5, "Discount -40%", LocalDate.parse("2021-06-27"), customerList().get(0),
                RewardStatus.Unrealized);
        when(rewardRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> rewardManager.save(reward,
                null));
    }

    @Test
    void save_with_null_parameters() {
        when(rewardRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> rewardManager.save(null,
                null));
    }

    @Test
    void deleteById() {
        when(rewardRepo.findById(1L)).thenReturn(Optional.ofNullable(rewardsList().get(0)));

        Optional<Reward> reward = rewardManager.deleteById(1L);

        assertEquals(rewardsList().get(0).getId(), reward.get().getId());
        assertEquals(rewardsList().get(0).getDate(), reward.get().getDate());
        assertEquals(rewardsList().get(0).getCustomer().getFirstName(), reward.get().getCustomer().getFirstName());

        verify(rewardRepo, times(1)).findById(1L);
        verify(rewardRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_not_found_id() {
        when(rewardRepo.findById(1L)).thenReturn(isNull());

        Optional<Reward> reward = rewardManager.deleteById(1L);

        assertNull(reward);
        verify(rewardRepo, times(1)).findById(1L);
        verify(rewardRepo, times(1)).deleteById(1L);
    }
}