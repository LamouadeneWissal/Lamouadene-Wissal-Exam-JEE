package com.example.ebanking_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemboursementDTO {
    private Long id;
    private LocalDate date;
    private Double montant;
    private String type; // Mensualité ou Remboursement anticipé
    private Long creditId;
}
