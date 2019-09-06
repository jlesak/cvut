package model.models;

import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.entities.company.Company;
import model.entities.company.Post;
import model.entities.user.User;


import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class CompanyModel extends DatabaseModel {
    private static Logger LOGGER = Logger.getLogger(CompanyModel.class.getName());


    public CompanyModel(Connection connection) {
        super(connection);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()) {
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(stdout);
        LOGGER.setLevel(Level.ALL);
        stdout.setLevel(Level.ALL);
    }

    /**
     * Saves new company to database.
     * @param company company to save to database.
     * @return true if insert is successful. false if insert failed.
     */
    public boolean newCompany(Company company){
        if(company.getUser() == null) {
            LOGGER.severe("Insert company with ico" + company.ico + " failed. Cannot insert company without user being set.");
            return false;
        }



        try {
            PreparedStatement s = connection.prepareStatement("SELECT ico FROM company WHERE ico = ?");
            s.setString(1, company.ico);
            ResultSet rs = s.executeQuery();

            // Company with ico already exists
            if(rs.next()) {
                LOGGER.severe("Insert company failed. Company with ico " + company.ico + " already exists.");
                return false;
            }

            s = connection.prepareStatement("INSERT INTO company (ico, description, website, name, user_email) " +
                    "VALUES (?, ?, ?, ?, ?)");
            s.setString(1, company.ico);
            s.setString(2, company.description);
            s.setString(3, company.website);
            s.setString(4, company.name);
            s.setString(5, company.getUser().email);
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert company with ico" + company.ico + " failed in database.");
            return false;
        }
        LOGGER.fine("Insert company with ico "+company.ico+" passed.");
        return true;
    }

    /**
     * Saves company and its post to database view.
     * @param company company to save to database
     * @param post companies post
     * @return true if insert is successful. false if insert failed.
     */
    public boolean newCompanyToView(Company company, Post post){

        try {
            String query = "INSERT INTO company_post_view (ico, name, description, website, user_email, post_title, post_description, post_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            Company company1 = getCompany(company.getUser());
            if(company1 != null) {
                query = "UPDATE company_post_view SET ico = ?, name = ?, description = ?, website = ?, user_email = ?, post_title = ?, post_description = ?, post_date = ?";
            }

            PreparedStatement s = connection.prepareStatement(query);
            s.setString(1, company.ico);
            s.setString(2, company.name);
            s.setString(3, company.description);
            s.setString(4, company.website);
            s.setString(5, company.getUser().email);
            s.setString(6, post.title);
            s.setString(7, post.description);
            s.setDate(8, new java.sql.Date(new java.util.Date().getTime()));
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert company with ico" + company.ico + " and its posts failed in database.");
            return false;
        }
        LOGGER.fine("Insert company with ico "+company.ico+" and its post passed.");
        return true;
    }

    /**
     * Loads company from database based on its user account.
     * @param user User whose company should be loaded.
     * @return instance of Company. Null if there is no company for user.
     */
    public Company getCompany(User user){
        Company company = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM company WHERE user_email = ?");
            preparedStatement.setString(1, user.email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                company = new Company();
                company.name = resultSet.getString("name");
                company.ico = (resultSet.getString("ico"));
                company.setUser(user);
                company.description = resultSet.getString("description");
                company.website = resultSet.getString("website");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select company with user email " + user.email + " failed in database.");
            return null;
        }
        LOGGER.fine( company == null ? "Select company with user email " + user.email + " failed" : "Select company with user email " + user.email + " succeed.");
        return company;
    }

    /**
     * Loads all companies from database
     * @return ArrayList of companies
     */
    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM company");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Company company = new Company();
                company = (Company) company.createFromResultSet(resultSet);
                User user = getUserByEmail(resultSet.getString("user_email"));
                company.setUser(user);
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select all companies failed in database.");
            return null;
        }
        LOGGER.fine("Select all companies succeed.");
        return companies;
    }

    /**
     * Deletes company based on his users email
     * @param email email of company user
     * @return true if delete was successful. False if wasn't.
     */
    public boolean deleteCompany(String email) {
        if(getUserByEmail(email) == null) {
            return false;
        }

        try {
            CallableStatement deleteCandidateProc = connection.prepareCall("{ call remove_company( ? ) }");
            deleteCandidateProc.setString(1, email);
            deleteCandidateProc.execute();
            deleteCandidateProc.close();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Delete company with user email " + email + " failed in database.");
            return false;
        }
        LOGGER.fine("Delete company with user email " + email + " succeed.");
        return true;
    }

    /**
     * Updates company row in database
     * @param company Company to update
     * @return true if update was successful. False if wasn't.
     */
    public boolean editCompany(Company company){
        PreparedStatement s;
        try {

            s = connection.prepareStatement("UPDATE company SET name = ?, website = ?, description = ? WHERE ico = ?");
            s.setString(1, company.name);
            s.setString(2, company.website);
            s.setString(3, company.description);
            s.setString(4, company.ico);
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Update company with ico " + company.ico + " failed in database.");
            return false;
        }
        LOGGER.fine("Update company with ico " + company.ico + " succeed.");
        return true;
    }
}
