package com.example.ebanking_backend.repositories;

import com.example.ebanking_backend.entities.Credit;
import com.example.ebanking_backend.entities.Remboursement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemboursementRepository extends JpaRepository<Remboursement, Long> {
    List<Remboursement> findByCredit(Credit credit);
}
