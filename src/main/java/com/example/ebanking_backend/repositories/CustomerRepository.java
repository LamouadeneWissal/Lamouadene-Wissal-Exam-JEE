package com.example.ebanking_backend.repositories;
import com.example.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer, Long>{
    @Query("select c from Customer c where c.name like :kw")
    List<Customer> findByNameContainsIgnoreCase(String keyword);
    boolean existsByEmail(String email);
}
