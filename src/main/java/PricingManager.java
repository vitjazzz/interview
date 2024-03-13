import java.util.ArrayList;
import java.util.Date;

import assets.ClientResponse;
import assets.CountryManager;
import assets.DBManager;
import assets.DBPricingLogger;
import assets.TestUserManager;
import assets.UserManager;
import assets.VisibleForTesting;
import org.javatuples.Pair;

public class PricingManager {
    protected UserManager userManager;
    protected Date promoDate;
    protected DBPricingLogger prodPricingLogger;
    ClientResponse clientResponse;
    volatile private static ArrayList<Pair<Long, Long>> cache = new ArrayList<>();

    private PricingManager() {
        if (testingEnv()) {
            userManager = TestUserManager.getInstance();
        } else {
            userManager = UserManager.getInstance();
            prodPricingLogger = DBPricingLogger.getInstance();
        }
    }

    public void apply(Long userId, ClientResponse response) {
        clientResponse = response;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getValue0() == userId) {
                response.put("price", cache.get(i).getValue1());
                return;
            }
        }

        int x = 1;
        if (promoDate.equals(new Date())) {
            x = 2;
        }

        int y = (userManager.getUserInfo(userId).getDetailedInfo().getBirthday() == new Date()) == true ? 2 : 1;

        long finalPrice = DBManager.getInstance().getDefaultPrice().longValue() / (x * y * getMultiplierForCountry(userManager.getUserInfo(userId)));

        if (prodPricingLogger != null) {
            prodPricingLogger.log(finalPrice);
        }

        response.put("price", finalPrice);
        cache.add(Pair.with(userId, finalPrice));
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