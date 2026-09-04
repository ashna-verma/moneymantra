package in.ashna.moneymantra.controller;

import in.ashna.moneymantra.dto.AiInsightDTO;
import in.ashna.moneymantra.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/test")
    public ResponseEntity<String> testAi() {

        return ResponseEntity.ok(
                aiService.testAi()
        );
    }

    @GetMapping("/insights")
    public ResponseEntity<AiInsightDTO> getFinancialInsights() {

        return ResponseEntity.ok(
                aiService.generateFinancialInsights()
        );
    }
}