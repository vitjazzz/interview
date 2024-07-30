import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import assets.ClientResponse;
import assets.CountryManager;
import assets.DBManager;
import assets.DBPricingLogger;
import assets.TestUserManager;
import assets.UserManager;
import assets.VisibleForTesting;
import org.apache.commons.lang3.tuple.Pair;

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

        if (findInCache(userId).isPresent()) {
            response.put("price", findInCache(userId).get());
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
        cache.add(Pair.of(userId, finalPrice));
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

    private Optional<Long> findInCache(long userId) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getKey() == userId) {
                return Optional.of(cache.get(i).getValue());
            }
        }
        return Optional.empty();
    }
}