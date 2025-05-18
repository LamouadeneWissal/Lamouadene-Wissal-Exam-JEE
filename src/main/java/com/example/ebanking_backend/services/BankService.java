package com.example.ebanking_backend.services;

import com.example.ebanking_backend.entities.Credit;
import com.example.ebanking_backend.entities.CreditImmobilier;
import com.example.ebanking_backend.entities.CreditPersonnel;
import com.example.ebanking_backend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        Credit bankAccount=
                bankAccountRepository.findById("0b36be78-8d5d-446b-9f20-37eadc9d3c3b").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*****************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CreditImmobilier) {
                System.out.println("Over Draft=>" + ((CreditImmobilier) bankAccount).getOverDraft());
            } else if (bankAccount instanceof CreditPersonnel) {
                System.out.println("Rate=>" + ((CreditPersonnel) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }
    }
}
