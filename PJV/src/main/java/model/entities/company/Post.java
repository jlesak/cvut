package model.entities.company;

import model.entities.BaseEntity;

import java.util.Date;

/**
 * Created by lesy on 18.4.17.
 */
public class Post extends BaseEntity {
    public int id;
    public String title;
    public Date date;
    public String description;
    public String company_ico;
    private Company company;

    public Post(String title, String description, String company_ico) {
        this.title = title;
        this.company_ico = company_ico;
        this.description = description;
    }

    public Post() { }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
