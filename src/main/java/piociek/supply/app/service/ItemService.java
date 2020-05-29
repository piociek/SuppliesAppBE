package piociek.supply.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import piociek.supply.app.model.Item;
import piociek.supply.app.model.LocationDetails;
import piociek.supply.app.repository.SupplyAppMongoRepository;
import piociek.supply.app.rest.Response;

import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemService {
    private final static String ITEM_NOT_FOUND_MSG = "Item with id %s not found in the database";

    private final SupplyAppMongoRepository supplyAppMongoRepository;

    @RequestMapping(value = "/getAllItems", method = RequestMethod.GET)
    @ResponseBody
    public Response get() {
        return Response.builder()
                .success(true)
                .items(supplyAppMongoRepository.findAll())
                .build();
    }

    @RequestMapping(value = "/getItem", method = RequestMethod.GET)
    @ResponseBody
    public Response getItemById(@PathParam(value = "itemId") String itemId) {
        Optional<Item> optionalItem = supplyAppMongoRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            return Response.builder()
                    .success(true)
                    .items(Collections.singletonList(optionalItem.get()))
                    .build();
        } else {
            return Response.builder()
                    .success(false)
                    .message(String.format(ITEM_NOT_FOUND_MSG, itemId))
                    .build();
        }
    }

    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    @ResponseBody
    public Response saveItem(@RequestBody Item item) {
        supplyAppMongoRepository.save(item);
        return Response.builder().success(true).build();
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE)
    @ResponseBody
    public Response deleteItem(@PathParam(value = "itemId") String itemId) {
        if (supplyAppMongoRepository.findById(itemId).isPresent()) {
            supplyAppMongoRepository.deleteById(itemId);
            return Response.builder().success(true).build();
        }
        return Response.builder()
                .success(false)
                .message(String.format(ITEM_NOT_FOUND_MSG, itemId))
                .build();
    }

    @RequestMapping(value = "/saveLocationDetails", method = RequestMethod.POST)
    @ResponseBody
    public Response saveLocationDetails(@PathParam(value = "itemId") String itemId,
                                        @RequestBody LocationDetails locationDetails) {
        Optional<Item> optionalItem = supplyAppMongoRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (Objects.isNull(item.getLocationDetails())) {
                item.setLocationDetails(Collections.singletonList(locationDetails));
            } else {
                Optional<LocationDetails> locationDetailsByIdOptional =
                        item.getLocationDetails().stream()
                                .filter(l -> l.getId().equals(locationDetails.getId()))
                                .findFirst();
                locationDetailsByIdOptional.ifPresent(details -> item.getLocationDetails().remove(details));

                Optional<LocationDetails> locationDetailsByNameAndExpDateOptional =
                        item.getLocationDetails().stream()
                                .filter(l -> l.getName().equals(locationDetails.getName()))
                                .filter(l -> l.getExpDate().equals(locationDetails.getExpDate()))
                                .findFirst();
                if (locationDetailsByNameAndExpDateOptional.isPresent()) {
                    locationDetails.setCount(locationDetails.getCount() + locationDetailsByNameAndExpDateOptional.get().getCount());
                    item.getLocationDetails().remove(locationDetailsByNameAndExpDateOptional.get());
                }
                item.getLocationDetails().add(locationDetails);
            }
            supplyAppMongoRepository.save(item);
            return Response.builder().success(true).items(Collections.singletonList(item)).build();
        } else {
            return Response.builder()
                    .success(false)
                    .message(String.format(ITEM_NOT_FOUND_MSG, itemId))
                    .build();
        }
    }

    @RequestMapping(value = "/deleteLocationDetails", method = RequestMethod.DELETE)
    @ResponseBody
    public Response deleteLocationDetails(@PathParam(value = "itemId") String itemId,
                                          @PathParam(value = "locationDetailsId") String locationDetailsId) {
        String responseMessage;
        Optional<Item> optionalItem = supplyAppMongoRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            Optional<LocationDetails> optionalLocationDetails =
                    item.getLocationDetails().stream()
                            .filter(l -> l.getId().equals(locationDetailsId))
                            .findFirst();
            if (optionalLocationDetails.isPresent()) {
                item.getLocationDetails().remove(optionalLocationDetails.get());
                supplyAppMongoRepository.save(item);
                return Response.builder().success(true).items(Collections.singletonList(item)).build();
            } else {
                responseMessage =
                        String.format("Location details with id %s for Item with id %s not found in the database",
                                locationDetailsId, itemId);
            }
        } else {
            responseMessage = String.format(ITEM_NOT_FOUND_MSG, itemId);
        }
        return Response.builder()
                .success(false)
                .message(responseMessage)
                .build();
    }
}
