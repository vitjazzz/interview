package assets;

public class CountryManager {
    private static final CountryManager instance = new CountryManager();

    public static CountryManager getInstance() {
        return instance;
    }
}
