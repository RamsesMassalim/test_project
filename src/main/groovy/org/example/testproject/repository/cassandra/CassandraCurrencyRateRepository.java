package org.example.testproject.repository.cassandra;

import org.example.testproject.domain.model.cassandra.CurrencyRate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface CassandraCurrencyRateRepository extends CassandraRepository<CurrencyRate, String> {

    @AllowFiltering
    Optional<CurrencyRate> findFirstBySymbolAndCloseIsFalseOrderByTimeStampDesc(String symbol);


    @AllowFiltering
    List<CurrencyRate> findAllBySymbolAndCloseIsFalse(String symbol);

    @Query(value = """
            SELECT *
              FROM currency_rates
             WHERE symbol = :symbol
               AND time_stamp >= toTimestamp(toDate(now()))
               AND time_stamp <= toTimestamp(now())
               AND close = false
             ALLOW FILTERING""",
            allowFiltering = true)
    @AllowFiltering
    Optional<CurrencyRate> findBySymbolAndTimeStamp(@Param("symbol") String symbol);

    @Modifying
    @Query("UPDATE currency_rates SET close = true WHERE symbol = :symbol AND time_stamp = :timeStamp")
    void updateCurrencyRatesBySymbol(@Param("symbol") String symbol, @Param("timeStamp") Instant timeStamp);
}
