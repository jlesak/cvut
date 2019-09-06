package model.models;

import model.entities.user.User;

import java.sql.*;
import java.util.logging.*;


/**
 * Created by fifa on 24.4.17.
 */
public class UserModel extends DatabaseModel {

    private static Logger LOGGER = Logger.getLogger(UserModel.class.getName());
    public UserModel(Connection connection) {
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

    public boolean registerUser(User user, String password){

        java.sql.Date date = new java.sql.Date(user.birth.getTime());
//        String salt = BCrypt.gensalt();
//        password = BCrypt.hashpw(password, salt);
        PreparedStatement s;
        try {
            s = connection.prepareStatement("INSERT INTO _user (email, password, firstname, lastname, birth) " +
                    "VALUES (?, ?, ?, ?, ?)");
            s.setString(1, user.email);
            s.setString(2, password);
            s.setString(3, user.firstname);
            s.setString(4, user.lastname);
            s.setDate(5,  date);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Register user with email " + user.email + " and password " + password + " failed in database.");
            return false;
        }
        LOGGER.fine("Register user with email " + user.email + " and password " + password + " succeed.");
        return true;
    }

    public User loginUser(String email, String password){

        User user = null;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM _user WHERE email = ? ");
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() /*&& BCrypt.checkpw(password, resultSet.getString("password"))*/){
                user = new User();
                user.id = (resultSet.getInt("id"));
                user.email = email;
                user.firstname = (resultSet.getString("firstname"));
                user.lastname = (resultSet.getString("lastname"));
                user.birth = new Date((resultSet.getDate("birth")).getTime());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Login user with email " + email + " failed in database.");
            return null;
        }
        LOGGER.fine( user == null ? "Login user with email " + email + " failed" : "Login user with email " + email + " succeed.");
        return user;
    }

    public User getUserByEmail(String email) {
        User user = null;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM _user WHERE email = ? ");
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.id = (resultSet.getInt("id"));
                user.email = resultSet.getString("email");
                user.firstname = (resultSet.getString("firstname"));
                user.lastname = (resultSet.getString("lastname"));
                user.birth = new Date(resultSet.getDate("birth").getTime());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

}
