package model.entities.company;

import model.entities.BaseEntity;
import model.entities.candidate.Candidate;
import model.entities.user.IUser;
import model.entities.user.User;

import java.util.ArrayList;

/**
 * Created by lesy on 18.4.17.
 */
public class Company extends BaseEntity implements IUser {
    public int id;
    public String ico;
    public String name;
    public String description;
    public String website;
    private User user;
    private ArrayList<Message> Messages;
    public Company() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Message> getMessages() {
        return Messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        Messages = messages;
    }
}
