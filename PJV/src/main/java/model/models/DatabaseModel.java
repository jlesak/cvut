package model.models; /**
 * Created by lesy on 18.4.17.
 */

import model.entities.company.Company;
import model.entities.user.User;

import java.sql.*;
import java.util.logging.*;

public abstract class DatabaseModel {

    protected Connection connection;
    private static Logger LOG = Logger.getLogger(DatabaseModel.class.getName());


    public DatabaseModel(Connection connection) {
        this.connection = connection;
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()) {
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        LOG.setUseParentHandlers(false);
        LOG.addHandler(stdout);
        LOG.setLevel(Level.ALL);
        stdout.setLevel(Level.ALL);
    }

    /**
     * Loads user from database based on its email
     * @param email String user email
     * @return Instance of User or null if there is no User with this email.
     */
    protected User getUserByEmail(String email){
        User user = null;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM _user WHERE email = ? ");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user = (User) user.createFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOG.severe("Select user with email " + email + " failed in database.");
            return null;
        }
        LOG.fine( user == null ? "Select user with email " + email + " failed" : "Select user with email " + email + " succeed.");
        return user;
    }

    /**
     * Loads company from database based on its ICO
     * @param ico String ICO
     * @return Instance of Company or Null if there is no Company with this ICO
     */
    protected Company getCompany(String ico){
        Company company = null;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM company WHERE ico = ?");
            preparedStatement.setString(1, ico);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                company = new Company();
                company.createFromResultSet(resultSet);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            LOG.severe("Select company with ico " + ico + " failed in database.");
            return null;
        }
        LOG.fine( company == null ? "Select company with ico " + ico + " failed" : "Select company with ico " + ico + " succeed.");
        return company;
    }

}
