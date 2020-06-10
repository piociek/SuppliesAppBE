package piociek.supply.app.rest;

import lombok.Builder;
import lombok.Data;
import piociek.supply.app.model.Item;

import java.util.Collection;

@Builder
@Data
public class Response {
    private boolean success;
    private String message;
    private Collection<Item> items;
}
