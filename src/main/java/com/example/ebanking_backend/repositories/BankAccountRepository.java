package com.example.ebanking_backend.repositories;

import com.example.ebanking_backend.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BankAccountRepository extends JpaRepository<Credit, String> {

}
