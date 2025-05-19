package com.example.ebanking_backend.services;


import com.example.ebanking_backend.dtos.*;
import com.example.ebanking_backend.entities.*;
import com.example.ebanking_backend.exceptions.*;
import com.example.ebanking_backend.mappers.CreditMapperImpl;
import com.example.ebanking_backend.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CustomerRepository customerRepository;
    private final CreditRepository creditRepository;
    private final RemboursementRepository remboursementRepository;
    private final CreditMapperImpl mapper;

    // -------------------- Customers --------------------

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.toCustomer(customerDTO);
        Customer saved = customerRepository.save(customer);
        return mapper.toCustomerDTO(saved);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.toCustomer(customerDTO);
        return mapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findByNameContainsIgnoreCase(keyword)
                .stream()
                .map(mapper::toCustomerDTO)
                .collect(Collectors.toList());
    }

    // -------------------- Credits --------------------

    @Override
    public CreditImmobilierDTO saveCreditImmobilier(CreditImmobilierDTO dto) throws CustomerNotFoundException {
        Customer customer = getCustomerEntity(dto.getClientId());
        CreditImmobilier credit = (CreditImmobilier) mapper.toCredit(dto, customer);
        return (CreditImmobilierDTO) mapper.toCreditDTO(creditRepository.save(credit));
    }

    @Override
    public CreditPersonnelDTO saveCreditPersonnel(CreditPersonnelDTO dto) throws CustomerNotFoundException {
        Customer customer = getCustomerEntity(dto.getClientId());
        CreditPersonnel credit = (CreditPersonnel) mapper.toCredit(dto, customer);
        return (CreditPersonnelDTO) mapper.toCreditDTO(creditRepository.save(credit));
    }

    @Override
    public CreditProfessionnelDTO saveCreditProfessionnel(CreditProfessionnelDTO dto) throws CustomerNotFoundException {
        Customer customer = getCustomerEntity(dto.getClientId());
        CreditProfessionnel credit = (CreditProfessionnel) mapper.toCredit(dto, customer);
        return (CreditProfessionnelDTO) mapper.toCreditDTO(creditRepository.save(credit));
    }

    @Override
    public CreditDTO getCredit(Long creditId) throws CreditNotFoundException {
        return creditRepository.findById(String.valueOf(creditId))
                .map(mapper::toCreditDTO)
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));
    }

    @Override
    public List<CreditDTO> listCredits() {
        return creditRepository.findAll()
                .stream()
                .map(mapper::toCreditDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreditDTO updateCredit(CreditDTO dto) throws CreditNotFoundException {
        Credit existing = creditRepository.findById(String.valueOf(dto.getId()))
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));
        Customer customer = getCustomerEntity(dto.getClientId());
        Credit updated = mapper.toCredit(dto, customer);
        return mapper.toCreditDTO(creditRepository.save(updated));
    }

    @Override
    public void deleteCredit(Long creditId) throws CreditNotFoundException {
        if (!creditRepository.existsById(String.valueOf(creditId))) throw new CreditNotFoundException("Credit not found");
        creditRepository.deleteById(String.valueOf(creditId));
    }

    // -------------------- Financial --------------------

    @Override
    public void debitCredit(Long creditId, double amount, String description)
            throws CreditNotFoundException, BalanceNotSufficientException {
        Credit credit = creditRepository.findById(String.valueOf(creditId))
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));

        if (credit.getMontant() < amount) {
            throw new BalanceNotSufficientException("Insufficient balance in credit");
        }

        credit.setMontant(credit.getMontant() - amount);
        creditRepository.save(credit);

        Remboursement remboursement = new Remboursement();
        remboursement.setCredit(credit);
        remboursement.setMontant(amount);
        remboursement.setType(description);
        remboursement.setDateRemboursement(new java.util.Date());

        remboursementRepository.save(remboursement);
    }

    @Override
    public void creditCredit(Long creditId, double amount, String description) throws CreditNotFoundException {
        Credit credit = creditRepository.findById(String.valueOf(creditId))
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));

        credit.setMontant(credit.getMontant() + amount);
        creditRepository.save(credit);

        Remboursement remboursement = new Remboursement();
        remboursement.setCredit(credit);
        remboursement.setMontant(amount);
        remboursement.setType(description);
        remboursement.setDateRemboursement(new java.util.Date());

        remboursementRepository.save(remboursement);
    }

    @Override
    public void transferBetweenCredits(Long sourceId, Long destinationId, double amount)
            throws CreditNotFoundException, BalanceNotSufficientException {
        debitCredit(sourceId, amount, "Transfer to credit " + destinationId);
        creditCredit(destinationId, amount, "Transfer from credit " + sourceId);
    }

    // -------------------- Remboursements --------------------

    @Override
    public List<RemboursementDTO> listRemboursements(Long creditId) throws CreditNotFoundException {
        Credit credit = creditRepository.findById(String.valueOf(creditId))
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));

        return remboursementRepository.findByCredit(credit)
                .stream()
                .map(mapper::toRemboursementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RemboursementDTO addRemboursement(RemboursementDTO dto) throws CreditNotFoundException {
        Credit credit = creditRepository.findById(String.valueOf(dto.getCreditId()))
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));

        Remboursement remboursement = mapper.toRemboursement(dto);
        remboursement.setCredit(credit);

        return mapper.toRemboursementDTO(remboursementRepository.save(remboursement));
    }

    @Override
    public void deleteRemboursement(Long remboursementId) {
        remboursementRepository.deleteById(remboursementId);
    }

    // -------------------- Helpers --------------------

    private Customer getCustomerEntity(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }
}
