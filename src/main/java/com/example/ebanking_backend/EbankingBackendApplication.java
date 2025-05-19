package com.example.ebanking_backend;

import com.example.ebanking_backend.dtos.CustomerDTO;
import com.example.ebanking_backend.dtos.CreditDTO;
import com.example.ebanking_backend.dtos.CreditImmobilierDTO;
import com.example.ebanking_backend.dtos.CreditPersonnelDTO;
import com.example.ebanking_backend.exceptions.CustomerNotFoundException;
import com.example.ebanking_backend.repositories.CustomerRepository;
import com.example.ebanking_backend.services.CreditService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CreditService creditService, CustomerRepository customerRepository) {
        return args -> {
            // Crée quelques clients sexy si ils existent pas encore
            Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
                String email = name.toLowerCase() + "@gmail.com";
                if (!customerRepository.existsByEmail(email)) {
                    CustomerDTO customer = new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(email);
                    creditService.saveCustomer(customer);
                    System.out.println("Created customer: " + name);
                }
            });

            // Pour chaque client, crée des crédits de types différents
            creditService.listCustomers().forEach(customer -> {
                try {
                    CreditImmobilierDTO immobilier = new CreditImmobilierDTO();
                    immobilier.setMontant(Math.random() * 100000);
                    immobilier.setDuree(240); // ex: 20 ans
                    immobilier.setClientId(customer.getId());
                    creditService.saveCreditImmobilier(immobilier);

                    CreditPersonnelDTO personnel = new CreditPersonnelDTO();
                    personnel.setMontant(Math.random() * 50000);
                    personnel.setDuree(60); // ex: 5 ans
                    personnel.setClientId(customer.getId());
                    creditService.saveCreditPersonnel(personnel);

                    System.out.println("Created credits for: " + customer.getName());

                } catch (CustomerNotFoundException e) {
                    System.err.println("Error creating credits for customer " + customer.getName() + ": " + e.getMessage());
                }
            });

            // Liste tous les crédits pour voir
            System.out.println("List of all credits:");
            creditService.listCredits().forEach(credit -> {
                System.out.println("Credit ID: " + credit.getId() + ", Montant: " + credit.getMontant() + ", Client ID: " + credit.getClientId());
            });
        };
    }
}
