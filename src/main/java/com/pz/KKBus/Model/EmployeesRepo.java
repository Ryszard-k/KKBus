package com.pz.KKBus.Model;

import com.pz.KKBus.Model.Entites.Employees;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesRepo extends CrudRepository<Employees, Long> {

    List<Employees> findByLastName(String lastName);
}
