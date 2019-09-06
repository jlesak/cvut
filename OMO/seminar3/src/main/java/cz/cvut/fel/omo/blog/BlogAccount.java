package cz.cvut.fel.omo.blog;

import java.util.List;

public abstract class BlogAccount {
    private String username;
    private String password;
    private boolean admin;

    private List<Comment> comments;
    private boolean blocked;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BlogAccount(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
    }

    public boolean getAccountBlockage() {
        return blocked;
    }
}


