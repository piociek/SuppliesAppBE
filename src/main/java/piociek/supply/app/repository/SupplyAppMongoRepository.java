package piociek.supply.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import piociek.supply.app.model.Item;

public interface SupplyAppMongoRepository extends MongoRepository<Item, String> {

}
