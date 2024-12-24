package com.example.service;

import com.example.model.Commande;
import com.example.repository.CommandeRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class CommandeQLService {
    private final CommandeRepository commandeRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CommandeQLService(CommandeRepository commandeRepository, 
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.commandeRepository = commandeRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    public List<Commande> getCommandesByStatus(String status) {
        return commandeRepository.findByStatus(status);
    }

    @Transactional
    public Commande createCommande(List<Commande.CommandeItem> items) {
        Commande commande = new Commande();
        commande.setDateCommande(new Date());
        commande.setStatus("PENDING");
        commande.setItems(items);
        commande.setTotal(calculateTotal(items));
        
        Commande savedCommande = commandeRepository.save(commande);
        kafkaTemplate.send("commande-topic", "CREATE", savedCommande.getId().toString());
        return savedCommande;
    }

    @Transactional
    public Commande updateCommandeStatus(Long id, String status) {
        Commande commande = commandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Commande not found"));
        commande.setStatus(status);
        kafkaTemplate.send("commande-status-topic", 
                          String.format("STATUS_UPDATE:%s:%s", id, status));
        return commandeRepository.save(commande);
    }

    @Transactional
    public Commande cancelCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Commande not found"));
        commande.setStatus("CANCELLED");
        kafkaTemplate.send("commande-status-topic", "CANCELLED:" + id);
        return commandeRepository.save(commande);
    }

    public CommandeStats getCommandeStats(Date startDate, Date endDate) {
        List<Commande> commandes = commandeRepository.findByDateCommandeBetween(startDate, endDate);
        
        double totalAmount = commandes.stream()
            .mapToDouble(Commande::getTotal)
            .sum();
            
        Map<Long, Integer> productQuantities = new HashMap<>();
        commandes.stream()
            .flatMap(c -> c.getItems().stream())
            .forEach(item -> productQuantities.merge(
                item.getProductId(), 
                item.getQuantity(), 
                Integer::sum));

        return CommandeStats.builder()
            .totalCommandes(commandes.size())
            .totalAmount(totalAmount)
            .averageOrderValue(totalAmount / commandes.size())
            .mostPopularProducts(getTopProducts(productQuantities))
            .build();
    }

    @Transactional
    public Commande addItemToCommande(Long commandeId, CommandeItem item) {
        Commande commande = commandeRepository.findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande not found"));
        
        commande.getItems().add(item);
        commande.setTotal(calculateTotal(commande.getItems()));
        
        kafkaTemplate.send("commande-update-topic", 
            String.format("ITEM_ADDED:%d:%d", commandeId, item.getProductId()));
            
        return commandeRepository.save(commande);
    }

    @Transactional
    public List<Commande> bulkUpdateStatus(List<Long> commandeIds, String status) {
        List<Commande> commandes = commandeRepository.findAllById(commandeIds);
        commandes.forEach(c -> c.setStatus(status));
        
        kafkaTemplate.send("commande-batch-topic", 
            String.format("BULK_STATUS_UPDATE:%s:%s", 
                String.join(",", commandeIds.stream().map(String::valueOf).toList()), 
                status));
                
        return commandeRepository.saveAll(commandes);
    }

    private Double calculateTotal(List<Commande.CommandeItem> items) {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}
