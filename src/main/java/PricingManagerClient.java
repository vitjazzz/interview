import assets.ClientResponse;

import java.io.IOException;
import java.util.Date;

public class ClientController {

    public ClientResponse use() {
        PricingManager pricingManager = new PricingManager();
        pricingManager.setpromoDate(new Date());

        try {
            pricingManager.apply(new ClientResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pricingManager.getResponse();
    }

}
