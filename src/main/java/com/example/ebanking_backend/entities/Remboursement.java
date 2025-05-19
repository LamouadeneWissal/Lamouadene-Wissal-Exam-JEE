package com.example.ebanking_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Remboursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateRemboursement;
    private Double montant;
    private String type; // Mensualité ou Remboursement anticipé

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;
}
