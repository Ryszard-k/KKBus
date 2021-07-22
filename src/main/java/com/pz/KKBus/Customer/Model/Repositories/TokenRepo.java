package com.pz.KKBus.Customer.Model.Repositories;

import com.pz.KKBus.Customer.Model.Entites.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);
}
