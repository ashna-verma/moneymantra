package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.AiInsightDTO;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.ExpenseRepository;
import in.ashna.moneymantra.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient.Builder chatClientBuilder;

    private final ProfileService profileService;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    private BigDecimal calculatePercentageChange(
            BigDecimal currentValue,
            BigDecimal previousValue
    ) {
        if (previousValue == null || previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentValue
                .subtract(previousValue)
                .divide(previousValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String testAi() {

        ChatClient chatClient = chatClientBuilder.build();

        return chatClient
                .prompt()
                .user("Say hello from Google Gemini in one short sentence.")
                .call()
                .content();
    }


    public AiInsightDTO generateFinancialInsights() {

        // Get logged-in user
        ProfileEntity profile = profileService.getCurrentProfile();

        LocalDate today = LocalDate.now();

        // First day of current month
        LocalDate startDate = today.withDayOfMonth(1);

        // Today's date
        LocalDate endDate = today;

        //Last month data
        LocalDate previousMonth = today.minusMonths(1);

        LocalDate previousStartDate =
                previousMonth.withDayOfMonth(1);

        LocalDate previousEndDate =
                previousMonth.withDayOfMonth(
                        previousMonth.lengthOfMonth()
                );


        // Get monthly income
        BigDecimal totalIncome =
                incomeRepository.findTotalIncomeByProfileAndDateBetween(
                        profile.getId(),
                        startDate,
                        endDate
                );


        // Get monthly expenses
        BigDecimal totalExpense =
                expenseRepository.findTotalExpenseByProfileAndDateBetween(
                        profile.getId(),
                        startDate,
                        endDate
                );

        List<Object[]> categoryExpenses =
                expenseRepository.findExpenseTotalsByCategory(
                        profile.getId(),
                        startDate,
                        endDate
                );

        String categorySummary = categoryExpenses.stream()
                .map(row -> row[0] + ": ₹" + row[1])
                .collect(Collectors.joining("\n"));

        BigDecimal previousIncome =
                incomeRepository.findTotalIncomeByProfileAndDateBetween(
                        profile.getId(),
                        previousStartDate,
                        previousEndDate
                );

        BigDecimal previousExpense =
                expenseRepository.findTotalExpenseByProfileAndDateBetween(
                        profile.getId(),
                        previousStartDate,
                        previousEndDate
                );

        // Prevent null values
        totalIncome = totalIncome != null
                ? totalIncome
                : BigDecimal.ZERO;

        totalExpense = totalExpense != null
                ? totalExpense
                : BigDecimal.ZERO;

        previousIncome = previousIncome != null
                ? previousIncome
                : BigDecimal.ZERO;

        previousExpense = previousExpense != null
                ? previousExpense
                : BigDecimal.ZERO;

        // Calculate savings
        BigDecimal savings = totalIncome.subtract(totalExpense);


        // Calculate savings rate
        BigDecimal savingsRate = BigDecimal.ZERO;

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {

            savingsRate = savings
                    .divide(totalIncome, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        //Calculate changes
        BigDecimal incomeChange =
                calculatePercentageChange(
                        totalIncome,
                        previousIncome
                );

        BigDecimal expenseChange =
                calculatePercentageChange(
                        totalExpense,
                        previousExpense
                );

        incomeChange = incomeChange.setScale(2, RoundingMode.HALF_UP);
        expenseChange = expenseChange.setScale(2, RoundingMode.HALF_UP);


        // Create prompt
        String prompt = """
        You are an AI financial insights assistant.
    
        Analyze the following financial data for the current month:
    
        Total Income: ₹%s
        Total Expenses: ₹%s
        Savings: ₹%s
        Savings Rate: %s%%
    
        Category-wise expenses:
        %s
    
        Previous Month Comparison:
    
        Previous Month Income: ₹%s
        Previous Month Expenses: ₹%s
    
        Income Change: %s%%
        Expense Change: %s%%
    
        Generate a financial analysis in the following format:
    
        Summary:
        Write one short overall summary of the user's financial situation.
        Keep it under 40 words.
    
        Insights:
        Give exactly 3 short, practical insights.
    
        Recommendation:
        Give one practical budgeting recommendation.
    
        Rules:
        - Keep each insight under 40 words.
        - Be friendly and encouraging.
        - Identify the largest spending category if category data is available.
        - Mention unusual spending patterns only if clearly supported by the data.
        - Compare current spending with the previous month if previous month data is available.
        - Clearly indicate whether expenses increased or decreased.
        - Do not give investment advice.
        - Focus on spending habits, savings, and budgeting.
        - Do not make assumptions about financial information not provided.
        
        Return the response as valid JSON in exactly this structure:
        
        {
          "summary": "A short overall financial summary",
          "insights": [
            "Insight 1",
            "Insight 2",
            "Insight 3"
          ],
          "recommendation": "One practical recommendation"
        }
        """.formatted(
                    totalIncome,
                    totalExpense,
                    savings,
                    savingsRate.setScale(2, RoundingMode.HALF_UP),
                    categorySummary.isBlank()
                            ? "No category-wise expense data available."
                            : categorySummary,
                    previousIncome,
                    previousExpense,
                    incomeChange,
                    expenseChange
            );


        // Call Gemini
        ChatClient chatClient = chatClientBuilder.build();

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .entity(AiInsightDTO.class);

    }
}