package piociek.supply.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickerDate {
    private Integer day;
    private Integer month;
    private Integer year;
}
