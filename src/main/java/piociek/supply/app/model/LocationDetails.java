package piociek.supply.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDetails {
    private String id;
    private String name;
    private Integer count;
    private PickerDate expDate;
}
