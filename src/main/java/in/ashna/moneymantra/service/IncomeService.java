package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.IncomeDTO;
import in.ashna.moneymantra.entity.CategoryEntity;
import in.ashna.moneymantra.entity.IncomeEntity;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.CategoryRepository;
import in.ashna.moneymantra.repository.IncomeRepository;
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

public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    private final EmailService emailService;

    //Adds a new expense to the database
    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category   = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        IncomeEntity newExpense = toEntity(incomeDTO, profile, category);
        newExpense = incomeRepository.save(newExpense);
        return toDTO(newExpense);
    }

    //Retrieves all incomes for the current month/based on the start and end date
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate=  currentDate.withDayOfMonth(1);
        LocalDate endDate= currentDate.withDayOfMonth(currentDate.getDayOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }

    //delete expense by id for current user
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepository.delete(entity);
    }

    //Get latest 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return  list.stream().map(this::toDTO).toList();
    }

    //Get total incomes for current user
    public BigDecimal getTotalIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfile(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    //Filter incomes
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    //Download Incomes excel sheet
    public byte[] generateIncomeExcel() {
        ProfileEntity profile = profileService.getCurrentProfile();

        List<IncomeEntity> incomes =
                incomeRepository.findByProfileIdAndDateBetween(
                        profile.getId(),
                        LocalDate.now().withDayOfMonth(1),
                        LocalDate.now()
                );

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Income Details");

            // Header row
            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Category");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Date");

            // Data rows
            int rowIndex = 1;

            for (IncomeEntity income : incomes) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(income.getName());

                row.createCell(1).setCellValue(
                        income.getCategory() != null
                                ? income.getCategory().getName()
                                : "N/A"
                );

                row.createCell(2).setCellValue(
                        income.getAmount().doubleValue()
                );

                row.createCell(3).setCellValue(
                        income.getDate().toString()
                );
            }

            // Adjust column widths
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate income Excel file", e);
        }
    }

    //Email Income details
    public void emailIncomeDetails() {

        ProfileEntity profile = profileService.getCurrentProfile();

        byte[] excelFile = generateIncomeExcel();
        emailService.sendIncomeExcel(
                profile.getEmail(),
                excelFile
        );
    }

    //Helper methods
    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profile, CategoryEntity category) {
        return in.ashna.moneymantra.entity.IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .category(category)
                .profile(profile)
                .build();

        //createdAt, updatedAt, id will be generated in the table automatically
    }

    private IncomeDTO toDTO(IncomeEntity incomeEntity) {
        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
                .categoryName(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getName() : "N/A")
                .amount(incomeEntity.getAmount())
                .date(incomeEntity.getDate())
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}
