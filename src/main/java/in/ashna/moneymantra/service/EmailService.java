package in.ashna.moneymantra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage message= new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendIncomeExcel(
            String recipient,
            byte[] excelFile
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(recipient);
        helper.setSubject("Your Income Details");
        helper.setText(
                "Hi,\n\nPlease find your income details attached.\n\n" +
                        "Regards,\nMoneyMantra"
        );

        helper.addAttachment(
                "Income_details.xlsx",
                new ByteArrayResource(excelFile)
        );

        mailSender.send(message);
    }

    public void sendExpenseExcel(
            String recipient,
            byte[] excelFile
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(recipient);
        helper.setSubject("Your Expense Details");
        helper.setText(
                "Hi,\n\nPlease find your expense details attached.\n\n" +
                        "Regards,\nMoneyMantra"
        );

        helper.addAttachment(
                "Expense_details.xlsx",
                new ByteArrayResource(excelFile)
        );

        mailSender.send(message);
    }

}
