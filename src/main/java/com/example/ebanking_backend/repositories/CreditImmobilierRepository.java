package com.example.ebanking_backend.repositories;

import com.example.ebanking_backend.entities.CreditImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditImmobilierRepository extends JpaRepository<CreditImmobilier, Long> {
}
