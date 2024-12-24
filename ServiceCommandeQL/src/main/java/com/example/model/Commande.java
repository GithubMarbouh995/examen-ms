package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Commande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateCommande;
    private String status;
    private Double total;
    
    @ElementCollection
    private List<CommandeItem> items;
    
    @Data @Embeddable
    public static class CommandeItem {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
