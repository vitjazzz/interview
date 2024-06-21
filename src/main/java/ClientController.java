import assets.ClientResponse;

import java.util.Date;

public class ClientController {
    private static final Date FOURTH_OF_JULY = new Date(1720051200000L);
    public static PricingManager pricingManager = new PricingManager();
    {
        pricingManager.setpromoDate(FOURTH_OF_JULY);
    }

    public ClientResponse use() {
        // imagine this is a call from some User with ID 123
        long userId = 123L;
        pricingManager.apply(userId, new ClientResponse());
        return pricingManager.getResponse();
    }

}
