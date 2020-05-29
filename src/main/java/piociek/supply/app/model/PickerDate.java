package piociek.supply.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickerDate {
    private Integer day;
    private Integer month;
    private Integer year;
}
