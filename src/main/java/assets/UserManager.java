package assets;

import java.util.Date;

public class UserManager {
    private static final UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    public UserInfo getUserInfo(Long userId) {
        /*
            Imagine some logic of retrieving userInfo by userId here
         */
        return null;
    }

    public static class UserInfo {
        public UserDetailedInfo getDetailedInfo() {
            return null;
        }
    }

    public static class UserDetailedInfo {
        public Object getDetailedInfo() {
            return null;
        }

        public Date getBirthday() {
            return null;
        }
    }
}
