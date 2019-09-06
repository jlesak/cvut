package model.models;

import model.entities.user.User;
import model.models.factories.ModelFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by fifa on 4.6.17.
 */
public class TestHelpers {

    /* Utils
    ===================
    */
    private static String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static User insertDummyUser() {
        Connection connection = ModelFactory.getDatabaseConnection();
        User user = new User();
        user.email = TestHelpers.randomString(9) + "@seznam.cz";
        user.firstname = "Jon";
        user.lastname = "Doe";
        user.active = true;
        user.birth = getTodayWithZeroTime();
        String password = "123456";

        PreparedStatement s;
        try {
            s = connection.prepareStatement("INSERT INTO _user (email, password, firstname, lastname, birth) " +
                    "VALUES (?, ?, ?, ?, ?)");
            s.setString(1, user.email);
            s.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            s.setString(3, user.firstname);
            s.setString(4, user.lastname);
            s.setDate(5, new java.sql.Date(user.birth.getTime()));

            int affectedRows = s.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = s.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.id = generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void cleanDummyUser(User user) {
        Connection connection = ModelFactory.getDatabaseConnection();
        try {
            PreparedStatement s = connection.prepareStatement("DELETE FROM _user WHERE email = ?");
            s.setString(1, user.email);
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static java.util.Date getTodayWithZeroTime() {
        java.util.Date todayWithZeroTime = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            todayWithZeroTime = formatter.parse(formatter.format(new java.util.Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if(todayWithZeroTime == null) {
                todayWithZeroTime = new java.util.Date();
            }
        }

        return todayWithZeroTime;
    }

}
