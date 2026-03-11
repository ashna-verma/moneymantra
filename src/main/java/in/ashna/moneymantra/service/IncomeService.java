package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.ExpenseDTO;
import in.ashna.moneymantra.dto.IncomeDTO;
import in.ashna.moneymantra.entity.CategoryEntity;
import in.ashna.moneymantra.entity.ExpenseEntity;
import in.ashna.moneymantra.entity.IncomeEntity;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.CategoryRepository;
import in.ashna.moneymantra.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

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
