package assets;

public class TestUserManager extends UserManager {
    private static final TestUserManager instance = new TestUserManager();

    public static TestUserManager getInstance() {
        return instance;
    }
}
