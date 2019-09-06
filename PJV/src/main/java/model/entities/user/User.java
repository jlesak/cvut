package model.entities.user;

import model.entities.BaseEntity;

import java.util.Date;

/**
 * Created by lesy on 18.4.17.
 */
public class User extends BaseEntity implements IUser {

    public int id;
    public String email;
    public String firstname;
    public String lastname;
    public Date birth;
    public boolean active;

    public User() { }

}
