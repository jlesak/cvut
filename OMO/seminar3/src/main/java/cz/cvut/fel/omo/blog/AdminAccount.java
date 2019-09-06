package cz.cvut.fel.omo.blog;

public class AdminAccount extends BlogAccount {

    public AdminAccount(String username, String password) {
        super(username, password, true);
    }
}
