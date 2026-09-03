package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.ExpenseDTO;
import in.ashna.moneymantra.entity.CategoryEntity;
import in.ashna.moneymantra.entity.ExpenseEntity;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.CategoryRepository;
import in.ashna.moneymantra.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;
    private final EmailService emailService;

    //Adds a new expense to the database
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category   = categoryRepository.findById(expenseDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));
        ExpenseEntity newExpense = toEntity(expenseDTO, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }

    //Retrieves all expenses for the current month/based on the start and end date
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate=  currentDate.withDayOfMonth(1);
        LocalDate endDate= currentDate.withDayOfMonth(currentDate.getDayOfMonth());
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }

    //delete expense by id for current user
    public void deleteExpense(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(entity);
    }

    //Get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return  list.stream().map(this::toDTO).toList();
    }

    //Get total expenses for current user
    public BigDecimal getTotalExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfile(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    //Filter expenses
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    //Notifications
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date) {
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId, date);
        return list.stream().map(this::toDTO).toList();
    }

    //Download Expense excel sheet
    public byte[] generateExpenseExcel() {
        ProfileEntity profile = profileService.getCurrentProfile();

        List<ExpenseEntity> expenses =
                expenseRepository.findByProfileIdAndDateBetween(
                        profile.getId(),
                        LocalDate.now().withDayOfMonth(1),
                        LocalDate.now()
                );

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Expense Details");

            // Header row
            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Category");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Date");

            // Data rows
            int rowIndex = 1;

            for (ExpenseEntity expense : expenses) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(expense.getName());

                row.createCell(1).setCellValue(
                        expense.getCategory() != null
                                ? expense.getCategory().getName()
                                : "N/A"
                );

                row.createCell(2).setCellValue(
                        expense.getAmount().doubleValue()
                );

                row.createCell(3).setCellValue(
                        expense.getDate().toString()
                );
            }

            // Adjust column widths
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate expense Excel file", e);
        }
    }

    //Email Expense details
    public void emailExpenseDetails() {

        ProfileEntity profile = profileService.getCurrentProfile();

        byte[] excelFile = generateExpenseExcel();
        emailService.sendExpenseExcel(
                profile.getEmail(),
                excelFile
        );
    }

    //Helper methods
    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profile, CategoryEntity category) {
        return in.ashna.moneymantra.entity.ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .category(category)
                .profile(profile)
                .build();

        //createdAt, updatedAt, id will be generated in the table automatically
    }

    private ExpenseDTO toDTO(ExpenseEntity expenseEntity) {
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .categoryId(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId() : null)
                .categoryName(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getName() : "N/A")
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }
}
