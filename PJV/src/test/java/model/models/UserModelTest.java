package model.models;

import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.entities.user.User;
import model.models.factories.ModelFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by lesy on 28.4.17.
 */
public class UserModelTest {
    private Connection connection;
    private UserModel userModel;

    private User user;

    @Before
    public void setUp() throws Exception {
        userModel = new UserModel(ModelFactory.getDatabaseConnection());
        connection = ModelFactory.getDatabaseConnection();
    }

    @After
    public void tearDown() throws Exception {
        if(user != null) {
            TestHelpers.cleanDummyUser(user);
            user = null;
        }
    }

    //Get user by email
    @Test
    public void getUserByEmail() throws Exception {
        // Non existing email
        assertNull("Get non existing user passed.", userModel.getUserByEmail("qwreqwpetasd@ff.cz"));

        user = TestHelpers.insertDummyUser();
        User userFromDb = userModel.getUserByEmail(user.email);

        assertEquals("User data: Emails are different.", user.email, userFromDb.email);
        assertEquals("User data: Firstname is different than expected.", user.firstname, userFromDb.firstname);
        assertEquals("User data: Lastname is different than expected.", user.lastname, userFromDb.lastname);
        assertEquals("User data: Birth is different than expected.", user.birth.getTime(), userFromDb.birth.getTime());

    }

    @Test
    public void registerUser() throws Exception {
        user = new User();
        user.email = TestHelpers.randomString(9) + "@seznam.cz";
        user.firstname = "Tom";
        user.lastname = "Doe";
        user.active = true;
        user.birth = TestHelpers.getTodayWithZeroTime();
        String pwd = "123456789";

        boolean userRegistered = userModel.registerUser(user, pwd);
        assertTrue(userRegistered);

        User userFromDb = userModel.getUserByEmail(user.email);
        assertEquals("User data: Emails are different.", user.email, userFromDb.email);
        assertEquals("User data: Firstname is different than expected.", user.firstname, userFromDb.firstname);
        assertEquals("User data: Lastname is different than expected.", user.lastname, userFromDb.lastname);
        assertEquals("User data: Birth is different than expected.", user.birth.getTime(), userFromDb.birth.getTime());

    }

    @Test
    public void loginUser() throws Exception {
        user = TestHelpers.insertDummyUser();

        // Login with wrong password
        User loginUser = userModel.loginUser(user.email, "WrongPassword");
        assertNull(loginUser);

        // Login with correct password and wrong email
        loginUser = userModel.loginUser("wrongemail@foobar.cz", "123456");
        assertNull(loginUser);

        // Login with correct credentials
        loginUser = userModel.loginUser(user.email, "123456");
        assertEquals(user.email, loginUser.email);

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

}