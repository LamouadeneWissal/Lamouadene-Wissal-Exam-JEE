package com.example.ebanking_backend.entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("IMMOBILIER")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditImmobilier extends Credit {
    private String typeBien;}
