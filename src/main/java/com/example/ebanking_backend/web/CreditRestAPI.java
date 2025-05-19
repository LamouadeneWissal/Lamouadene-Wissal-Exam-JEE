package com.example.ebanking_backend.web;

import com.example.ebanking_backend.dtos.*;
import com.example.ebanking_backend.exceptions.BalanceNotSufficientException;
import com.example.ebanking_backend.exceptions.CreditNotFoundException;
import com.example.ebanking_backend.exceptions.CustomerNotFoundException;
import com.example.ebanking_backend.services.CreditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditRestAPI {

    private final CreditService creditService;

    public CreditRestAPI(CreditService creditService) {
        this.creditService = creditService;
    }

    // List all credits
    @GetMapping
    public List<CreditDTO> listCredits() {
        return creditService.listCredits();
    }

    // Get credit by id
    @GetMapping("/{id}")
    public CreditDTO getCredit(@PathVariable Long id) throws CreditNotFoundException {
        return creditService.getCredit(id);
    }

    // Create new immobilier credit
    @PostMapping("/immobilier")
    public CreditImmobilierDTO createImmobilier(@RequestBody CreditImmobilierDTO dto) throws CustomerNotFoundException {
        return creditService.saveCreditImmobilier(dto);
    }

    // Create new personnel credit
    @PostMapping("/personnel")
    public CreditPersonnelDTO createPersonnel(@RequestBody CreditPersonnelDTO dto) throws CustomerNotFoundException {
        return creditService.saveCreditPersonnel(dto);
    }

    // Create new professionnel credit
    @PostMapping("/professionnel")
    public CreditProfessionnelDTO createProfessionnel(@RequestBody CreditProfessionnelDTO dto) throws CustomerNotFoundException {
        return creditService.saveCreditProfessionnel(dto);
    }

    // Update credit
    @PutMapping("/{id}")
    public CreditDTO updateCredit(@PathVariable Long id, @RequestBody CreditDTO dto) throws CreditNotFoundException {
        dto.setId(id); // ensure id consistency
        return creditService.updateCredit(dto);
    }

    // Delete credit
    @DeleteMapping("/{id}")
    public void deleteCredit(@PathVariable Long id) throws CreditNotFoundException {
        creditService.deleteCredit(id);
    }

    // Debit operation on credit
    @PostMapping("/{id}/debit")
    public void debitCredit(@PathVariable Long id, @RequestParam double amount, @RequestParam String description) throws CreditNotFoundException, BalanceNotSufficientException {
        creditService.debitCredit(id, amount, description);
    }

    // Credit operation on credit
    @PostMapping("/{id}/credit")
    public void creditCredit(@PathVariable Long id, @RequestParam double amount, @RequestParam String description) throws CreditNotFoundException {
        creditService.creditCredit(id, amount, description);
    }

    // Transfer between credits
    @PostMapping("/transfer")
    public void transferBetweenCredits(@RequestParam Long sourceId, @RequestParam Long destinationId, @RequestParam double amount) throws CreditNotFoundException, BalanceNotSufficientException {
        creditService.transferBetweenCredits(sourceId, destinationId, amount);
    }

    // List remboursements for a credit
    @GetMapping("/{creditId}/remboursements")
    public List<RemboursementDTO> listRemboursements(@PathVariable Long creditId) throws CreditNotFoundException {
        return creditService.listRemboursements(creditId);
    }

    // Add remboursement
    @PostMapping("/{creditId}/remboursements")
    public RemboursementDTO addRemboursement(@PathVariable Long creditId, @RequestBody RemboursementDTO dto) throws CreditNotFoundException {
        dto.setCreditId(creditId);
        return creditService.addRemboursement(dto);
    }

    // Delete remboursement
    @DeleteMapping("/remboursements/{id}")
    public void deleteRemboursement(@PathVariable Long id) {
        creditService.deleteRemboursement(id);
    }
}
