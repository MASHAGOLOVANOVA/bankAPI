package gd.testtask.golovanova.bankAPI.controllers;

import gd.testtask.golovanova.bankAPI.dto.BankDTO;
import gd.testtask.golovanova.bankAPI.services.BankService;
import gd.testtask.golovanova.bankAPI.util.BankNotCreatedException;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
import gd.testtask.golovanova.bankAPI.util.BankNotUpdatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banks")
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<BankDTO> getBanks(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                  @RequestParam(value = "sort_by_name", required = false, defaultValue = "false") boolean sortByName,
                                  @RequestParam(value = "sort_by_bank_id_code", required = false, defaultValue = "false") boolean sortByBankIdCode,
                                  @RequestParam(value = "filter_by_name", required = false, defaultValue = "") String filterByName,
                                  @RequestParam(value = "filter_by_bankIdCode", required = false, defaultValue = "") String filterByBankIdCode) {

        return bankService.findAll(filterByName, filterByBankIdCode, sortById, sortByName, sortByBankIdCode);
    }

    @GetMapping("/{id}")
    public BankDTO getBank(@PathVariable("id") int id) {
        return bankService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createBank(@RequestBody @Valid BankDTO bankDTO, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, BankNotCreatedException.class);
        bankService.save(bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBank(@PathVariable("id") int id, @RequestBody @Valid BankDTO bankDTO, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, BankNotUpdatedException.class);
        bankService.update(id, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBank(@PathVariable int id) {
        try {
            bankService.delete(id);
            return ResponseEntity.ok().build();
        } catch (BankNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
