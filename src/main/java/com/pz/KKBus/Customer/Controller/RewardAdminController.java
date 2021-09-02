package com.pz.KKBus.Customer.Controller;

import com.pz.KKBus.Customer.Manager.RewardAdminManager;
import com.pz.KKBus.Customer.Model.Entites.RewardAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rewardAdmin")
public class RewardAdminController {

    private final RewardAdminManager rewardAdminManager;

    @Autowired
    public RewardAdminController(RewardAdminManager rewardAdminManager) {
        this.rewardAdminManager = rewardAdminManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<RewardAdmin> foundRewards = rewardAdminManager.findAll();
        if (foundRewards.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundRewards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<RewardAdmin> foundById = rewardAdminManager.findById(id);
        if(foundById.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(foundById, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addReward(@RequestBody RewardAdmin rewardAdmin){
        if (rewardAdmin == null) {
            return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
        } else
            rewardAdminManager.save(rewardAdmin);
        return new ResponseEntity<>(rewardAdmin, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReward(@PathVariable Long id) {
        Optional<RewardAdmin> foundReward = rewardAdminManager.findById(id);
        if (foundReward.isPresent()) {
            rewardAdminManager.deleteById(id);
            return new ResponseEntity<>(foundReward,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found reward to delete!", HttpStatus.NOT_FOUND);
    }
}
