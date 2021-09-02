package com.pz.KKBus.Staff.Model.Repositories;

import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeesRepo extends CrudRepository<Employees, Long> {

    List<Employees> findByLastName(String lastName);

    List<Employees> findByRole (Role role);

    Optional<Employees> findByUsername(String username);
}
