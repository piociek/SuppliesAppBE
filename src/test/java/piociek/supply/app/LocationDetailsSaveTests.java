package piociek.supply.app;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import piociek.supply.app.model.Item;
import piociek.supply.app.model.LocationDetails;
import piociek.supply.app.model.PickerDate;
import piociek.supply.app.rest.Response;
import piociek.supply.app.service.ItemService;
import piociek.supply.app.util.ItemHelper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LocationDetailsSaveTests {

    @Autowired
    private ItemHelper itemHelper;

    @Autowired
    private ItemService service;

    private final String locationId1 = "1";
    private final String locationId2 = "2";
    private final int count1 = 1;
    private final int count2 = 3;
    private final PickerDate date1 = PickerDate.builder().day(1).month(2).year(2020).build();
    private final PickerDate date2 = PickerDate.builder().day(2).month(3).year(2021).build();
    private final String locationDetailsName1 = "Loc-1";
    private final String locationDetailsName2 = "Loc-2";

    @Test
    public void overwriteLocationDetailsIfIdIsTheSameTest() {
        final String itemId = itemHelper.getCurrentTimeStamp() + itemHelper.getRandomSeed();

        service.saveItem(new Item(itemId, "Item-1"), null);
        service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId1, locationDetailsName1, count1, date1));
        Response response = service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId1, locationDetailsName2, count2, date2));

        Item item = assertThatItemIsPresentAndGetIt(response, itemId);
        assertThatThereIsExactlyLocationDetails(item, 1);
        LocationDetails locationDetails = assertThatThereIsLocationDetailsPresentAndGetIt(item, locationId1);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(locationDetails.getCount()).isEqualTo(count2);
            softly.assertThat(locationDetails.getName()).isEqualTo(locationDetailsName2);
            softly.assertThat(locationDetails.getExpDate()).isEqualTo(date2);
        }
    }

    @Test
    public void mergeLocationDetailsIfNameAndDateIsTheSameTest() {
        final String itemId = itemHelper.getCurrentTimeStamp() + itemHelper.getRandomSeed();

        service.saveItem(new Item(itemId, "Item-2"), null);
        service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId1, locationDetailsName1, count1, date1));
        Response response = service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId2, locationDetailsName1, count2, date1));

        Item item = assertThatItemIsPresentAndGetIt(response, itemId);
        assertThatThereIsExactlyLocationDetails(item, 1);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(item.getLocationDetails().get(0).getCount()).isEqualTo(count1 + count2);
        }
    }

    @Test
    public void dontMergeLocationDetailsIfDateMismatchTest() {
        final String itemId = itemHelper.getCurrentTimeStamp() + itemHelper.getRandomSeed();

        service.saveItem(new Item(itemId, "Item-3"), null);
        service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId1, locationDetailsName1, count1, date1));
        Response response = service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId2, locationDetailsName1, count2, date2));

        Item item = assertThatItemIsPresentAndGetIt(response, itemId);
        assertThatThereIsExactlyLocationDetails(item, 2);
    }

    @Test
    public void dontMergeLocationDetailsIfLocationNameMismatchTest() {
        final String itemId = itemHelper.getCurrentTimeStamp() + itemHelper.getRandomSeed();

        service.saveItem(new Item(itemId, "Item-4"), null);
        service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId1, locationDetailsName1, count1, date1));
        Response response = service.saveLocationDetails(itemId, null,
                new LocationDetails(locationId2, locationDetailsName2, count2, date1));

        Item item = assertThatItemIsPresentAndGetIt(response, itemId);
        assertThatThereIsExactlyLocationDetails(item, 2);
    }

    private Item assertThatItemIsPresentAndGetIt(Response response, String itemId) {
        assertThat(response.getItems()).isNotNull();
        assertThat(response.getItems()).isNotEmpty();

        Optional<Item> itemOptional = response.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
        assertThat(itemOptional).isPresent();
        return itemOptional.get();
    }

    private LocationDetails assertThatThereIsLocationDetailsPresentAndGetIt(Item item, String locationDetailsId) {
        Optional<LocationDetails> locationDetailsOptional = item.getLocationDetails().stream()
                .filter(ld -> ld.getId().equals(locationDetailsId))
                .findFirst();
        assertThat(locationDetailsOptional).isPresent();
        return locationDetailsOptional.get();
    }

    private void assertThatThereIsExactlyLocationDetails(Item item, int expectedCount) {
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(item.getLocationDetails()).isNotNull();
            softly.assertThat(item.getLocationDetails().size()).isEqualTo(expectedCount);
        }
    }
}
