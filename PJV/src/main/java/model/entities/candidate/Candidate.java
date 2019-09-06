package model.entities.candidate;

/**
 * Created by lesy on 18.4.17.
 */

import model.entities.BaseEntity;
import model.entities.company.Message;
import model.entities.user.IUser;
import model.entities.user.User;

import java.util.ArrayList;

public class Candidate extends BaseEntity implements IUser {


    public int id;
    public String job_title;
    public String description;
    public boolean looking_for_job;
    private User user;
    private ArrayList<Message> Messages;
    private ArrayList<Skill> Skills;
    private ArrayList<Experience> Experiences;

    //
    public Candidate() { }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
