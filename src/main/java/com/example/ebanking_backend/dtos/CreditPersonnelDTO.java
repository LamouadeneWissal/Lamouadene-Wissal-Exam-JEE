package com.example.ebanking_backend.dtos;
import com.example.ebanking_backend.enums.StatusCredit;

import lombok.Data;

import java.util.Date;
@Data
public class CreditPersonnelDTO extends BaseCreditDTO {
    private String motif;
}
