package model.entities.company;

import model.entities.BaseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lesy on 18.4.17.
 */
public class Message extends BaseEntity {
    public int id;
    public String name;
    public String content;
    public Date date;
    public String company_ico;
    public int candidate_id;
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Message() {
    }

    @Override
    public String toString() {
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
        return "Message{" +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", date=" + sdfr.format(date) +
                ", company_ico='" + company_ico + '\'' +
                ", candidate_id=" + candidate_id +
                '}';
    }
}
