package org.example.testproject.domain.api.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    private String name;
    private LocalDate date;
    private String country;
}
