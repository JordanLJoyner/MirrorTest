package jordan.com.mirrorcodetest;

/**
 * Created by Jordan on 2/25/2018.
 */

public class UserManager {
    private User currentUser = null;
    private static UserManager instance = null;

    protected UserManager() {
        // Exists only to defeat instantiation.
    }

    public static UserManager getInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User newUser){
        currentUser = newUser;
    }

    public User getUser(){
        return currentUser;
    }
}
