package com.pz.KKBus.Controller;

import com.pz.KKBus.Manager.CustomerManager;
import com.pz.KKBus.Manager.RewardManager;
import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reward;
import com.pz.KKBus.Model.Enums.RewardStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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

    @GetMapping("/{name}")
    public ResponseEntity getByName(@PathVariable String name){
        Optional<Reward> foundByName = rewardManager.findByName(name);
        if(foundByName.isEmpty()){
            return new ResponseEntity<>("Bad brand", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundByName, HttpStatus.OK);
    }

    @PostMapping(path = "/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReward(@RequestBody Reward reward, @PathVariable String username){
        var customer = customerManager.findByUsername(username);
        if (reward != null && customer.isPresent()) {
            rewardManager.save(reward, customer);
            return new ResponseEntity<>(reward, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReward(@PathVariable Long id) {
        Optional<Reward> foundReward = rewardManager.findById(id);
        if (foundReward.isPresent() && foundReward.get().getRewardStatus().equals(RewardStatus.Unrealized)) {
            Optional<Reward> reward = rewardManager.deleteById(id);
            Customer customer = reward.get().getCustomer();
            int points = customer.getPoints() + reward.get().getPoints();
            customer.setPoints(points);
            customerManager.save(customer);
            return new ResponseEntity<>(foundReward,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found reward to delete!", HttpStatus.NOT_FOUND);
    }
}
