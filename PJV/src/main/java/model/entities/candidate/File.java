package model.entities.candidate;

/**
 * Created by lesy on 18.4.17.
 */

import model.entities.BaseEntity;
import model.entities.company.Message;
import model.entities.user.IUser;
import model.entities.user.User;

import java.util.ArrayList;

public class File extends BaseEntity implements IUser {


    public int id;
    public String path;
    public String name;
    public String filename;
    public int _user;

}



