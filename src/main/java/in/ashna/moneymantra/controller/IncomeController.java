package in.ashna.moneymantra.controller;

import in.ashna.moneymantra.dto.IncomeDTO;
import in.ashna.moneymantra.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addExpense(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO saved = incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncomes() {
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(incomes);
    }
}
