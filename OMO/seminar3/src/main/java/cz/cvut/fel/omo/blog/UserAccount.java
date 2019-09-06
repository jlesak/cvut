package cz.cvut.fel.omo.blog;

public class UserAccount extends BlogAccount {
    public UserAccount(String username, String password) {
        super(username, password, false);
    }
}
