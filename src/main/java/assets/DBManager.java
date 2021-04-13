package assets;

public class DBManager {
    private static final DBManager instance = new DBManager();

    public static DBManager getInstance() {
        return instance;
    }

    public Number getDefaultPrice() {
        return 123;
    }
}
