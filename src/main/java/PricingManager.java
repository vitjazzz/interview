import java.io.IOException;
import java.util.Date;

import assets.ClientResponse;
import assets.CountryManager;
import assets.DBManager;
import assets.DBPricingLogger;
import assets.TestUserManager;
import assets.UserManager;
import assets.VisibleForTesting;

public class PricingManager {
    protected UserManager userManager;
    protected Date promoDate;
    protected DBPricingLogger prodPricingLogger;
    ClientResponse clientResponse;

    private PricingManager() {
        if (testingEnv()) {
            userManager = TestUserManager.getInstance();
        } else {
            userManager = UserManager.getInstance();
            prodPricingLogger = DBPricingLogger.getInstance();
        }
    }

    public void apply(ClientResponse response) throws IOException {
        clientResponse = response;

        int x = 1;

        if (promoDate.equals(new Date())) {
            x = 2;
        }

        int y = (userManager.getUserInfo().getDetailedInfo().getBirthday() == new Date()) == true ? 2 : 1;

        long finalPrice = DBManager.getInstance().getDefaultPrice().longValue() / (x * y * getMultiplierForCountry(userManager.getUserInfo()));

        if (prodPricingLogger != null) {
            prodPricingLogger.log(finalPrice);
        }

        response.put("price", finalPrice);
    }

    public ClientResponse getResponse(){
        clientResponse.put("country", CountryManager.getInstance().getId());
        return clientResponse;
    }

    protected boolean testingEnv() {
        return System.getenv().get("TESTING") != null;
    }

    public void setpromoDate(Date date) {
        this.promoDate = date;
    }

    @VisibleForTesting
    private int getMultiplierForCountry(UserManager.UserInfo userInfo) {
        return CountryManager.getInstance().getMultiplier(userInfo);
    }


}