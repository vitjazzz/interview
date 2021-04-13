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

    private PricingManager() {
        if (testingEnv()) {
            userManager = TestUserManager.getInstance();
        } else {
            userManager = UserManager.getInstance();
            prodPricingLogger = DBPricingLogger.getInstance();
        }
    }

    public void apply(ClientResponse response) throws IOException {
        int x = 1;

        if (promoDate.equals(new Date())) {
            x = 2;
        }

        int y = (userManager.getUserInfo().getDetailedInfo().getBirthday().getTime() == new Date().getTime()) == true ? 2 : 1;

        int finalPrice = DBManager.getInstance().getDefaultPrice().getValue() / (x * y * getMultiplierForCountry(userManager.getUserInfo()) * 100);

        if (prodPricingLogger != null) {
            prodPricingLogger.log(finalPrice);
        }

        response.put("price", finalPrice);
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