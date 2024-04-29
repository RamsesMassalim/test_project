package org.example.testproject.domain.dto.api_response;

import lombok.*;
import org.example.testproject.domain.dto.AbstractDto;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class CurrencyRateDto extends AbstractDto {
    private String symbol;
    private BigDecimal rate;
    private long timestamp;
}
