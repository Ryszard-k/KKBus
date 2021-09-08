package com.pz.KKBus.Customer.Controller;

import com.pz.KKBus.Customer.Manager.RewardManager;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Customer.Manager.CustomerManager;
import com.pz.KKBus.Customer.Model.Entites.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/reward")
public class RewardController {

    private final RewardManager rewardManager;
    private final CustomerManager customerManager;

    @Autowired
    public RewardController(RewardManager rewardManager, CustomerManager customerManager) {
        this.rewardManager = rewardManager;
        this.customerManager = customerManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Reward> foundRewards = rewardManager.findAll();
        if (foundRewards.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundRewards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Reward> foundById = rewardManager.findById(id);
        if(foundById.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundById, HttpStatus.OK);
    }

    @PostMapping(path = "/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReward(@RequestBody Reward reward, @PathVariable String username){
        Optional<Customer> customer = customerManager.findByUsername(username);
        if (reward != null && customer.isPresent() && reward.getPoints() < customer.get().getPoints()) {
            reward.setRewardStatus(RewardStatus.Realized);
            rewardManager.save(reward, customer);
            customer.get().setPoints(customer.get().getPoints() - reward.getPoints());
            customerManager.update(customer.get());
            return new ResponseEntity<>(reward, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReward(@PathVariable Long id) {
        Optional<Reward> foundReward = rewardManager.findById(id);
        if (foundReward.isPresent() && foundReward.get().getRewardStatus().equals(RewardStatus.Unrealized)) {
            Customer customer = foundReward.get().getCustomer();
            int points = customer.getPoints() + foundReward.get().getPoints();
            customer.setPoints(points);
            customerManager.update(customer);
            rewardManager.deleteById(id);
            return new ResponseEntity<>(foundReward,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found reward to delete!", HttpStatus.NOT_FOUND);
    }
}
