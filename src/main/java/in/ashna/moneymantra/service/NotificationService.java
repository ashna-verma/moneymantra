package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.ExpenseDTO;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j      //for logs
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.mantra.frontend.url}")
    private String frontendUrl;

    //schedule every minute for testing
    //@Scheduled(cron = "0 * * * * *", zone = "IST")

    //schedule at 10 pm IST everyday
    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            String body = "Hello " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Mantra.<br><br>"
                    + "<a href="+frontendUrl+ " style='display:inline-block; padding:10px 20px; background-color:#4CAF50; color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to MoneyMantra</a>"
                    + "<br><br>Best regards, <br>MoneyMantra team";
            emailService.sendEmail(profile.getEmail(), "Daily reminder: Add your income & expenses", body);
        }
        log.info("Job finished: sendDailyIncomeExpenseReminder()");
    }

    //schedule every minute for testing
    //@Scheduled(cron = "0 * * * * *", zone = "IST")
    //schedule at 11 pm IST every day for report
    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
    public void sendDailyExpenseReport(){
        log.info("Job started: sendDailyExpenseReport()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            List<ExpenseDTO> todaysExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if (!todaysExpenses.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style= 'border-collapse: collapse; width:100%'>");
                table.append("<tr style = 'background-color: #f2f2f2;'><th style = 'border:1px solid #ddd; padding:8px'>S.No</th><th style= 'border:1px solid #ddd; padding:8px'>Name</th><th style= 'border:1px solid #ddd; padding:8px'>Amount</th><th style= 'border:1px solid #ddd; padding:8px'>Category</th></tr>");
                int i = 1;
                for (ExpenseDTO expenseDTO : todaysExpenses) {
                    table.append("<tr>");
                    table.append("<td style= 'border:1px solid #ddd; padding:8px'>").append(i++).append("</td>");
                    table.append("<td style= 'border:1px solid #ddd; padding:8px'>").append(expenseDTO.getName()).append("</td>");
                    table.append("<td style= 'border:1px solid #ddd; padding:8px'>").append(expenseDTO.getAmount()).append("</td>");
                    table.append("<td style= 'border:1px solid #ddd; padding:8px'>").append(expenseDTO.getCategoryId() != null ? expenseDTO.getCategoryName(): "N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi "+ profile.getFullName()
                        + ",<br/><br/> Here is a summary of your expenses for today: <br/><br/>"
                        + table
                        + "<br/><br/>Best regards, <br/>MoneyMantra team";
                emailService.sendEmail(profile.getEmail(), "Your daily Expense report", body);
            }
        }
        log.info("Job finished: sendDailyExpenseReport()");
    }

}
