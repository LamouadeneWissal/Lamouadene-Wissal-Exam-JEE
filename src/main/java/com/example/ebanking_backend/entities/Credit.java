package com.example.ebanking_backend.entities;

import com.example.ebanking_backend.enums.StatusCredit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE-CREDIT", length=4)


public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDemande;
    private Date dateAcceptation;

    private double montant;
    private int dureeRemboursement;
    private double tauxInteret;
    private String type;

    @Enumerated(EnumType.STRING)
    private StatusCredit status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "credit")
    private List<Remboursement> remboursements;


    }


