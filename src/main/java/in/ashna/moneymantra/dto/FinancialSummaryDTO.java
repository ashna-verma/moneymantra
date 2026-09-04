package in.ashna.moneymantra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialSummaryDTO {

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal savings;

    private BigDecimal savingsRate;

    private String topExpenseCategory;

    private BigDecimal topCategoryAmount;

    private BigDecimal previousMonthExpense;

    private BigDecimal expenseChangePercentage;
}