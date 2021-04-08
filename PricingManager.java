public class PricingManager {
    protected UserManager userManager;
    protected Date promoDate;
    protected PricingLogger prodPricingLogger;

    private PricingManager(){
        if(testingEnv()){
            userManager  = TestUserManager.getInstance();
        }else{
            userManager = UserManager.getInstance();
            prodPricingLogger = DBPricingLogger.getInstance();
        }

    }

    public void apply(ClientResponse response) throws IOException{
        int x = 1;

        if(promoDate.equals(new Date())){
            x = 2;
        }

        int y = userManager.getUserInfo().getDetailedInfo().getBirthday().getLong().equals(new Date()) == true? 2: 1;

        int finalPrice = DBManager.getInstance().getDefaultPrice().getValue() /( x * y * getMultiplierForCountry(userManager.getUserInfo()) * 100);

        if(prodPricingLogger != null){
            prodPricingLogger.log(finalPrice);
        }

        response.put("price",finalPrice);
    }

    protected boolean testingEnv() {
        return System.getenv().get("TESTING") != null;
    }

    public void setpromoDate(Date date){
        this.promoDate = date;
    }

    @VisibleForTesting
    private int getMultiplierForCountry(UserInfo userInfo){
        return CountryManager.getInstance().getMultiplier(userInfo);
    }


}