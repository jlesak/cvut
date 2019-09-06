package model.entities.candidate;

import model.entities.BaseEntity;

import java.util.Date;

/**
 * Created by lesakjan on 19.4.17.
 */
public class Experience extends BaseEntity {
    public int id;
    public String title;
    public String company;
    public String description;
    public String position;
    public Date date_from;
    public Date date_to;
    public String experience_type;

    public Experience() {

    }
}
