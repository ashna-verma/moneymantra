package in.ashna.moneymantra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        return headers;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            Map<String, Object> payload = Map.of(
                    "sender", Map.of("email", fromEmail, "name", "MoneyMantra"),
                    "to", List.of(Map.of("email", to)),
                    "subject", subject,
                    "textContent", body
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, buildHeaders());
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendIncomeExcel(String recipient, byte[] excelFile) {
        sendExcelAttachment(
                recipient,
                "Your Income Details",
                "Hi,\n\nPlease find your income details attached.\n\nRegards,\nMoneyMantra",
                "Income_details.xlsx",
                excelFile
        );
    }

    public void sendExpenseExcel(String recipient, byte[] excelFile) {
        sendExcelAttachment(
                recipient,
                "Your Expense Details",
                "Hi,\n\nPlease find your expense details attached.\n\nRegards,\nMoneyMantra",
                "Expense_details.xlsx",
                excelFile
        );
    }

    private void sendExcelAttachment(String recipient, String subject, String textBody, String fileName, byte[] fileBytes) {
        try {
            String base64Content = Base64.getEncoder().encodeToString(fileBytes);

            Map<String, Object> payload = Map.of(
                    "sender", Map.of("email", fromEmail, "name", "MoneyMantra"),
                    "to", List.of(Map.of("email", recipient)),
                    "subject", subject,
                    "textContent", textBody,
                    "attachment", List.of(Map.of(
                            "content", base64Content,
                            "name", fileName
                    ))
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, buildHeaders());
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with attachment: " + e.getMessage());
        }
    }
}