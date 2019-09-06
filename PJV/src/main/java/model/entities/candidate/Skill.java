package model.entities.candidate;

import model.entities.BaseEntity;


/**
 * Created by lesakjan on 19.4.17.
 */
public class Skill extends BaseEntity {
    public String name;
    public String s_group;

    public Skill(String name, String skillGroup) {
        this.name = name;
        this.s_group = skillGroup;
    }

    public Skill() {
    }

    @Override
    public String toString() {
        return name + " (" + s_group + ')';
    }
}
