package com.example.controller;

import com.example.model.Commande;
import com.example.service.CommandeQLService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CommandeQLController {
    private final CommandeQLService commandeService;

    public CommandeQLController(CommandeQLService commandeService) {
        this.commandeService = commandeService;
    }

    @QueryMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @QueryMapping
    public Commande getCommandeById(@Argument Long id) {
        return commandeService.getCommandeById(id);
    }

    @QueryMapping
    public List<Commande> getCommandesByStatus(@Argument String status) {
        return commandeService.getCommandesByStatus(status);
    }

    @MutationMapping
    public Commande createCommande(@Argument List<Commande.CommandeItem> items) {
        return commandeService.createCommande(items);
    }

    @MutationMapping
    public Commande updateCommandeStatus(@Argument Long id, @Argument String status) {
        return commandeService.updateCommandeStatus(id, status);
    }

    @QueryMapping
    public CommandeStats getCommandeStats(
            @Argument String startDate,
            @Argument String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = startDate != null ? format.parse(startDate) : null;
        Date end = endDate != null ? format.parse(endDate) : null;
        return commandeService.getCommandeStats(start, end);
    }

    @MutationMapping
    public Commande cancelCommande(@Argument Long id) {
        return commandeService.cancelCommande(id);
    }

    @MutationMapping
    public Commande addItemToCommande(
            @Argument Long commandeId,
            @Argument Commande.CommandeItem item) {
        return commandeService.addItemToCommande(commandeId, item);
    }

    @MutationMapping
    public List<Commande> bulkUpdateStatus(
            @Argument List<Long> commandeIds,
            @Argument String status) {
        return commandeService.bulkUpdateStatus(commandeIds, status);
    }
}
