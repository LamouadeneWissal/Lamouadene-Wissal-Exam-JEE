package com.example.ebanking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PERSONNEL")

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreditProfessionnel extends Credit  {
    private String motif;
    private String raisonSociale;
}
