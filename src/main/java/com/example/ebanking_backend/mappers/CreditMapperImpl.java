package com.example.ebanking_backend.mappers;

import com.example.ebanking_backend.dtos.CreditImmobilierDTO;
import com.example.ebanking_backend.dtos.CustomerDTO;
import com.example.ebanking_backend.dtos.CreditPersonnelDTO;
import com.example.ebanking_backend.entities.CreditImmobilier;
import com.example.ebanking_backend.entities.Customer;
import com.example.ebanking_backend.entities.CreditPersonnel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class CreditMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return  customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }

    public CreditPersonnelDTO fromSavingBankAccount(CreditPersonnel savingAccount){
        CreditPersonnelDTO savingBankAccountDTO=new CreditPersonnelDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public CreditPersonnel fromSavingBankAccountDTO(CreditPersonnelDTO savingBankAccountDTO){
        CreditPersonnel savingAccount=new CreditPersonnel();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CreditImmobilierDTO fromCurrentBankAccount(CreditImmobilier currentAccount){
        CreditImmobilierDTO currentBankAccountDTO=new CreditImmobilierDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CreditImmobilier fromCurrentBankAccountDTO(CreditImmobilierDTO currentBankAccountDTO){
        CreditImmobilier currentAccount=new CreditImmobilier();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

}
