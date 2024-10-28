package gd.testtask.golovanova.bankAPI.controllers;

import gd.testtask.golovanova.bankAPI.dto.DepositDTO;
import gd.testtask.golovanova.bankAPI.services.DepositService;
import gd.testtask.golovanova.bankAPI.util.DepositNotCreatedException;
import gd.testtask.golovanova.bankAPI.util.DepositNotFoundException;
import gd.testtask.golovanova.bankAPI.util.DepositNotUpdatedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    public List<DepositDTO> getDeposits(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                        @RequestParam(value = "sort_by_percentage", required = false, defaultValue = "false") boolean sortByPercentage,
                                        @RequestParam(value = "sort_by_period", required = false, defaultValue = "false") boolean sortByPeriod,
                                        @RequestParam(value = "sort_by_create_date", required = false, defaultValue = "false") boolean sortByCreateDate,
                                        @RequestParam(value = "filter_by_percentage", required = false) Integer filterByPercentage,
                                        @RequestParam(value = "filter_by_period", required = false) Integer filterByPeriod) {
        return depositService.findAll(filterByPercentage, filterByPeriod, sortById, sortByPercentage, sortByPeriod, sortByCreateDate);
    }

    @GetMapping("/{id}")
    public DepositDTO getDeposit(@PathVariable int id) {
        return depositService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createDeposit(@RequestBody @Valid DepositDTO depositDTO, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, DepositNotCreatedException.class);
        depositService.save(depositDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDeposit(@PathVariable int id, @RequestBody @Valid DepositDTO depositDTO, BindingResult bindingResult) {
        GlobalExceptionHandler.handleValidationErrors(bindingResult, DepositNotUpdatedException.class);
        depositService.update(id, depositDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDeposit(@PathVariable int id) {
        try {
            depositService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DepositNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
