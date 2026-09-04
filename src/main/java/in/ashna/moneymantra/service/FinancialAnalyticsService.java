package in.ashna.moneymantra.service;
import in.ashna.moneymantra.dto.FinancialSummaryDTO;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.ExpenseRepository;
import in.ashna.moneymantra.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FinancialAnalyticsService {

    private final ProfileService profileService;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public FinancialSummaryDTO getCurrentMonthSummary() {

        ProfileEntity profile =
                profileService.getCurrentProfile();

        LocalDate now = LocalDate.now();

        LocalDate startDate =
                now.withDayOfMonth(1);

        LocalDate endDate =
                now.withDayOfMonth(
                        now.lengthOfMonth()
                );

        // Calculate income
        // Calculate expenses
        // Calculate savings
        // Calculate top category

        return FinancialSummaryDTO.builder()
                .build();
    }
}