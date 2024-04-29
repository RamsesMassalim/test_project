package org.example.testproject.domain.model.cassandra;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value = "currency_rates")
public class CurrencyRate {
    private String symbol;
    private boolean close;
    private Instant timeStamp;
    private BigDecimal rate;

    @PrimaryKeyColumn(name = "symbol", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    public String getSymbol() {
        return symbol;
    }

    @Column("close")
    public boolean isClose() {
        return close;
    }

    @PrimaryKeyColumn(name = "time_stamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    public Instant getTimeStamp() {
        return timeStamp;
    }

    @Column("rate")
    public BigDecimal getRate() {
        return rate;
    }
}
