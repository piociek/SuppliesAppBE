package piociek.supply.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import piociek.supply.app.repository.SupplyAppMongoRepository;
import piociek.supply.app.rest.Response;

@Profile("local")
@Controller
@RequiredArgsConstructor
public class ItemServiceLocal {

    private final SupplyAppMongoRepository supplyAppMongoRepository;

    @RequestMapping(value = "/deleteAllItems", method = RequestMethod.DELETE)
    @ResponseBody
    public Response deleteAllItems() {
        supplyAppMongoRepository.deleteAll();
        return Response.builder()
                .success(true)
                .build();
    }
}
