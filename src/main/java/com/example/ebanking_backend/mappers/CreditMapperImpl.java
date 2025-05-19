package com.example.ebanking_backend.mappers;

import com.example.ebanking_backend.dtos.*;
import com.example.ebanking_backend.entities.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CreditMapperImpl {

    // ----------- Customer -------------

    public CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public Customer toCustomer(CustomerDTO dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        return customer;
    }

    // ----------- Credit Base -------------

    public CreditDTO toCreditDTO(Credit credit) {
        if (credit == null) return null;

        CreditDTO dto;

        // Gérer les sous-classes
        if (credit instanceof CreditImmobilier) {
            CreditImmobilier c = (CreditImmobilier) credit;
            CreditImmobilierDTO dtoImmobilier = new CreditImmobilierDTO();
            copyBaseCreditToDTO(c, dtoImmobilier);
            dtoImmobilier.setTypeBien(c.getTypeBien());
            dto = dtoImmobilier;
        } else if (credit instanceof CreditPersonnel) {
            CreditPersonnel c = (CreditPersonnel) credit;
            CreditPersonnelDTO dtoPersonnel = new CreditPersonnelDTO();
            copyBaseCreditToDTO(c, dtoPersonnel);
            dtoPersonnel.setMotif(c.getMotif());
            dto = dtoPersonnel;
        } else if (credit instanceof CreditProfessionnel) {
            CreditProfessionnel c = (CreditProfessionnel) credit;
            CreditProfessionnelDTO dtoPro = new CreditProfessionnelDTO();
            copyBaseCreditToDTO(c, dtoPro);
            dtoPro.setMotif(c.getMotif());
            dtoPro.setRaisonSociale(c.getRaisonSociale());
            dto = dtoPro;
        } else {
            dto = new CreditDTO();
            copyBaseCreditToDTO(credit, dto);
        }

        return dto;
    }

    public Credit toCredit(CreditDTO dto, Customer customer) {
        if (dto == null) return null;

        Credit credit;

        if (dto instanceof CreditImmobilierDTO) {
            CreditImmobilierDTO d = (CreditImmobilierDTO) dto;
            CreditImmobilier c = new CreditImmobilier();
            copyBaseCreditFromDTO(d, c, customer);
            c.setTypeBien(d.getTypeBien());
            credit = c;
        } else if (dto instanceof CreditPersonnelDTO) {
            CreditPersonnelDTO d = (CreditPersonnelDTO) dto;
            CreditPersonnel c = new CreditPersonnel();
            copyBaseCreditFromDTO(d, c, customer);
            c.setMotif(d.getMotif());
            credit = c;
        } else if (dto instanceof CreditProfessionnelDTO) {
            CreditProfessionnelDTO d = (CreditProfessionnelDTO) dto;
            CreditProfessionnel c = new CreditProfessionnel();
            copyBaseCreditFromDTO(d, c, customer);
            c.setMotif(d.getMotif());
            c.setRaisonSociale(d.getRaisonSociale());
            credit = c;
        } else {
            Credit c = new Credit();
            copyBaseCreditFromDTO(dto, c, customer);
            credit = c;
        }

        return credit;
    }

    // ----------- Remboursement -------------

    public RemboursementDTO toRemboursementDTO(Remboursement remboursement) {
        if (remboursement == null) return null;
        RemboursementDTO dto = new RemboursementDTO();
        dto.setId(remboursement.getId());
        dto.setDateRemboursement(convertDateToLocalDate(remboursement.getDateRemboursement()));
        dto.setMontant(remboursement.getMontant());
        dto.setType(remboursement.getType());
        if (remboursement.getCredit() != null)
            dto.setCreditId(remboursement.getCredit().getId());
        return dto;
    }

    public Remboursement toRemboursement(RemboursementDTO dto) {
        if (dto == null) return null;
        Remboursement remboursement = new Remboursement();
        remboursement.setId(dto.getId());
        remboursement.setDateRemboursement(convertLocalDateToDate(dto.getDateRemboursement()));
        remboursement.setMontant(dto.getMontant());
        remboursement.setType(dto.getType());
        // Credit doit être défini dans le service
        return remboursement;
    }

    // ----------- Helpers -------------

    private void copyBaseCreditToDTO(Credit credit, CreditDTO dto) {
        if (credit == null || dto == null) return;

        dto.setId(credit.getId());
        dto.setDateDemande(convertDateToLocalDate(credit.getDateDemande()));
        dto.setStatut(credit.getStatus());
        dto.setMontant(credit.getMontant());
        dto.setDuree(credit.getDureeRemboursement());
        dto.setTauxInteret(credit.getTauxInteret());

        if (credit.getCustomer() != null)
            dto.setClientId(credit.getCustomer().getId());
    }

    private void copyBaseCreditFromDTO(CreditDTO dto, Credit credit, Customer customer) {
        if (credit == null || dto == null) return;

        if (dto.getTauxInteret() != null) {
            credit.setTauxInteret(dto.getTauxInteret().doubleValue());
        } else {
            credit.setTauxInteret(0.0); // your default value, mommy
        }

        credit.setId(dto.getId());
        credit.setDateDemande(convertLocalDateToDate(dto.getDateDemande()));
        credit.setStatus(dto.getStatut());
        credit.setMontant(dto.getMontant());
        credit.setDureeRemboursement(dto.getDuree());
        // Remove this next line, because tauxInteret is already set safely above
        // credit.setTauxInteret(dto.getTauxInteret());
        credit.setCustomer(customer);
    }


    private LocalDate convertDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
