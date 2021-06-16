package com.pz.KKBus.Model.Repositories;

import com.pz.KKBus.Model.Entites.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);
}
