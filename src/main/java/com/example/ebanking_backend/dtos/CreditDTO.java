package com.example.ebanking_backend.dtos;

import com.example.ebanking_backend.enums.StatusCredit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    private Long id;
    private LocalDate dateDemande;
    private StatusCredit statut;
    private Double montant;
    private Integer duree;
    private Double tauxInteret;
    private Long clientId; // Référence au client



}
