package org.example.testproject.domain.model.cassandra;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value = "holidays")
public class HolidayEntity {
    private String country;
    private int month;
    private int day;
    private String name;

    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    public String getCountry() {
        return country;
    }

    @PrimaryKeyColumn(name = "month", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    public int getMonth() {
        return month;
    }

    @PrimaryKeyColumn(name = "day", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    public int getDay() {
        return day;
    }

    @Column("name")
    public String getName() {
        return name;
    }
}
