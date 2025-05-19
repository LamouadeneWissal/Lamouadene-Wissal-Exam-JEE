package com.example.ebanking_backend.services;

import com.example.ebanking_backend.dtos.*;
import com.example.ebanking_backend.exceptions.BalanceNotSufficientException;
import com.example.ebanking_backend.exceptions.CreditNotFoundException;
import com.example.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface CreditService {

    // Customer operations
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<CustomerDTO> listCustomers();

    List<CustomerDTO> searchCustomers(String keyword);

    // Credit operations: create/save credits of different types
    CreditImmobilierDTO saveCreditImmobilier(CreditImmobilierDTO creditImmobilierDTO) throws CustomerNotFoundException;

    CreditPersonnelDTO saveCreditPersonnel(CreditPersonnelDTO creditPersonnelDTO) throws CustomerNotFoundException;

    CreditProfessionnelDTO saveCreditProfessionnel(CreditProfessionnelDTO creditProfessionnelDTO) throws CustomerNotFoundException;

    // Get credit by id
    CreditDTO getCredit(Long creditId) throws CreditNotFoundException;

    // List all credits (maybe paginated in future)
    List<CreditDTO> listCredits();

    // Update credit details (optional)
    CreditDTO updateCredit(CreditDTO creditDTO) throws CreditNotFoundException;

    // Delete credit
    void deleteCredit(Long creditId) throws CreditNotFoundException;

    // Financial operations on credits (like debit/credit/transfer for bank accounts)
    void debitCredit(Long creditId, double amount, String description) throws CreditNotFoundException, BalanceNotSufficientException;

    void creditCredit(Long creditId, double amount, String description) throws CreditNotFoundException;

    void transferBetweenCredits(Long creditSourceId, Long creditDestinationId, double amount) throws CreditNotFoundException, BalanceNotSufficientException;

    // Remboursement (payment) operations
    List<RemboursementDTO> listRemboursements(Long creditId) throws CreditNotFoundException;

    RemboursementDTO addRemboursement(RemboursementDTO remboursementDTO) throws CreditNotFoundException;

    void deleteRemboursement(Long remboursementId);
}
