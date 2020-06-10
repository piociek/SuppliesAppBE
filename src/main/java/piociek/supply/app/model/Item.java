package piociek.supply.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    private String id;
    private String lastModified;
    private String barCode;
    private String category;
    private String name;
    private String packaging;
    private List<LocationDetails> locationDetails;
}
