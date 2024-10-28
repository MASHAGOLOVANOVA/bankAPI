package gd.testtask.golovanova.bankAPI.controllers;


import gd.testtask.golovanova.bankAPI.dto.ClientDTO;
import gd.testtask.golovanova.bankAPI.services.ClientService;
import gd.testtask.golovanova.bankAPI.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientDTO> getClients(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                      @RequestParam(value = "sort_by_name", required = false, defaultValue = "false") boolean sortByName,
                                      @RequestParam(value = "sort_by_short_name", required = false, defaultValue = "false") boolean sortByShortName,
                                      @RequestParam(value = "sort_by_address", required = false, defaultValue = "false") boolean sortByAddress,
                                      @RequestParam(value = "filter_by_name", required = false, defaultValue = "") String filterByName,
                                      @RequestParam(value = "filter_by_short_name", required = false, defaultValue = "") String filterByShortName,
                                      @RequestParam(value = "filter_by_address", required = false, defaultValue = "") String filterByAddress) {
        return clientService.findAll(filterByName, filterByShortName, filterByAddress, sortById, sortByName, sortByShortName, sortByAddress);
    }

    @GetMapping("/{id}")
    public ClientDTO getClient(@PathVariable int id) {
        return clientService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createClient(@RequestBody @Valid ClientDTO client, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, ClientNotCreatedException.class);
        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateClient(@PathVariable int id, @RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, ClientNotUpdatedException.class);
        clientService.update(id, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable int id) {
        try {
            clientService.delete(id);
            return ResponseEntity.ok().build();
        } catch (ClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

