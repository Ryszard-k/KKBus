package com.pz.KKBus.Staff.Controller;

import org.springframework.http.ResponseEntity;

public interface CrudControllerMethods <T>{

    ResponseEntity getAll();

    ResponseEntity getById(Long id);

    ResponseEntity<Object> add(T object);

    ResponseEntity<Object> deleteById(Long id);
}
