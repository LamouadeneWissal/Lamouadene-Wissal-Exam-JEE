package com.example.ebanking_backend;

import com.example.ebanking_backend.dtos.*;
import com.example.ebanking_backend.entities.*;
import com.example.ebanking_backend.enums.StatusCredit;
import com.example.ebanking_backend.exceptions.CustomerNotFoundException;
import com.example.ebanking_backend.repositories.AccountOperationRepository;
import com.example.ebanking_backend.repositories.CreditRepository;
import com.example.ebanking_backend.repositories.CustomerRepository;
import com.example.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, CustomerRepository customerRepository) {
        return args -> {
            // Create customers if they don't already exist
            Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
                String email = name + "@gmail.com";
                if (!customerRepository.existsByEmail(email)) {
                    CustomerDTO customer = new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(email);
                    bankAccountService.saveCustomer(customer);
                }
            });

            // Create bank accounts for each customer
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            // Add transactions (credits and debits) to each account
            List<BaseCreditDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BaseCreditDTO bankAccount : bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccount instanceof CreditPersonnelDTO) {
                        accountId = ((CreditPersonnelDTO) bankAccount).getId();
                    } else {
                        accountId = ((CreditImmobilierDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
                    bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
                }
            }
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            CreditRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CreditImmobilier currentAccount=new CreditImmobilier();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(StatusCredit.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                CreditPersonnel savingAccount=new CreditPersonnel();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(StatusCredit.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }

}
