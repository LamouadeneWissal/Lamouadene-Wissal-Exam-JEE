package com.example.ebanking_backend.dtos;
import com.example.ebanking_backend.enums.StatusCredit;

import lombok.Data;

import java.util.Date;
@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private StatusCredit status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
