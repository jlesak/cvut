package cz.cvut.fel.omo.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Blog {
    private List<BlogAccount> accounts;
    private List<Topic> topics;
    private Dashboard dashboard;
    private Editor editor;

    public Blog() {
        accounts = new ArrayList<>();
    }

    public void createNewAccount(String username, String password, boolean admin) {
        if (accounts.stream().anyMatch(acount -> Objects.equals(acount.getUsername(), username))) return;
        if (username == null || username.length() == 0
                || password == null || password.length() == 0)
            return;


        if (admin){
            AdminAccount newAdmin = new AdminAccount(username, password);
            accounts.add(newAdmin);
        }
        else{
            UserAccount newUser = new UserAccount(username, password);
            accounts.add(newUser);
        }
    }

    public BlogAccount login(String username, String password) {
        return accounts.stream()
                .filter(acount -> Objects.equals(acount.getUsername(), username))
                .filter(acount -> Objects.equals(acount.getPassword(), password))
                .filter(acount -> acount.getAccountBlockage() == false)
                .findFirst()
                .orElse(null);

    }
}
