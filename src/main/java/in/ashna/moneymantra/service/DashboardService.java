package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.ExpenseDTO;
import in.ashna.moneymantra.dto.IncomeDTO;
import in.ashna.moneymantra.dto.RecentTransactionDTO;
import in.ashna.moneymantra.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = concat(
                latestIncomes.stream().map(incomeDTO ->
                        RecentTransactionDTO.builder()
                                .id(incomeDTO.getId())
                                .profileId(profile.getId())
                                .icon(incomeDTO.getIcon())
                                .name(incomeDTO.getName())
                                .amount(incomeDTO.getAmount())
                                .date(incomeDTO.getDate())
                                .createdAt(incomeDTO.getCreatedAt())
                                .updatedAt(incomeDTO.getUpdatedAt())
                                .type("income")
                                .build()),
                latestExpenses.stream().map(expenseDTO ->
                        RecentTransactionDTO.builder()
                                .id(expenseDTO.getId())
                                .profileId(profile.getId())
                                .icon(expenseDTO.getIcon())
                                .name(expenseDTO.getName())
                                .amount(expenseDTO.getAmount())
                                .date(expenseDTO.getDate())
                                .createdAt(expenseDTO.getCreatedAt())
                                .updatedAt(expenseDTO.getUpdatedAt())
                                .type("expense")
                                .build()))
                .sorted((a, b) -> {          //if both dates are same for 2 recent transactions,use fallback and compare createdAt
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());

        returnValue.put("totalBalance",
                incomeService.getTotalIncomesForCurrentUser().
                        subtract(expenseService.getTotalExpensesForCurrentUser()));
        returnValue.put("totalIncome", incomeService.getTotalIncomesForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recent5Incomes", latestIncomes);
        returnValue.put("recentTransactions", recentTransactions);

        return returnValue;
    }
}
