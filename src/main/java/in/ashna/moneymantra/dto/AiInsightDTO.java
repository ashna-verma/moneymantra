package in.ashna.moneymantra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiInsightDTO {

    private String summary;

    private List<String> insights;

    private String recommendation;
}
