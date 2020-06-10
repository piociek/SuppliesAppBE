package piociek.supply.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import piociek.supply.app.model.Item;

import java.util.List;

public interface SupplyAppMongoRepository extends MongoRepository<Item, String> {

    List<Item> findAllByLastModifiedGreaterThan(String date);

}
