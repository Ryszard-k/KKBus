package com.pz.KKBus.Model;

import com.pz.KKBus.Model.Entites.Employees;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepo extends CrudRepository<Employees, Long> {
}
