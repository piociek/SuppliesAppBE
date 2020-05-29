package piociek.supply.app.rest;

import lombok.Builder;
import lombok.Data;
import piociek.supply.app.model.Item;

import java.util.List;

@Builder
@Data
public class Response {
    private boolean success;
    private String message;
    private List<Item> items;
}
