package org.example.testproject.domain.api.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayResponse {
    private int status;
    private List<Holiday> holidays;
}
