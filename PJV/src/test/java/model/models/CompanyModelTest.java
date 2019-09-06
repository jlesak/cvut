package model.models;

import model.entities.company.Company;
import model.entities.user.User;
import model.models.factories.ModelFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by lesy on 28.4.17.
 */
public class CompanyModelTest {
    private Connection connection;
    private CompanyModel companyModel;

    private User user;

    @Override
    protected void finalize () {
        if(user != null) {
            TestHelpers.cleanDummyUser(user);
            user = null;
        }
    }

    @Before
    public void setUp() throws Exception {
        companyModel = new CompanyModel(ModelFactory.getDatabaseConnection());
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
    public void newCompany() throws Exception {
        user = TestHelpers.insertDummyUser();
        Company company = createCompany();

        // Insert company with no user
        assertFalse("Company cannot be inserted with no user assigned", companyModel.newCompany(company));


        // Insert company with user
        company.setUser(user);
        assertTrue(companyModel.newCompany(company));


        // Test if inserted the right data
        PreparedStatement s = connection.prepareStatement("SELECT * FROM company WHERE ico = ?");
        s.setString(1, company.ico);
        ResultSet rs = s.executeQuery();
        rs.next();

        assertEquals(company.ico, rs.getString("ico"));
        assertEquals(company.description, rs.getString("description"));
        assertEquals(company.name, rs.getString("name"));
        assertEquals(company.website, rs.getString("website"));

        // Insert the same company again
        assertFalse("Company cannot be inserted twice.", companyModel.newCompany(company));

        cleanCompany(company);
    }

    // Company for user
    @Test
    public void getCompany() throws Exception {
        user = TestHelpers.insertDummyUser();
        Company company = createCompany();
        company.setUser(user);
        companyModel.newCompany(company);

        User nonExistingUser = new User();
        nonExistingUser.email = "tadaa@fel.cz";

        assertNull(companyModel.getCompany(nonExistingUser));

        Company dbCompany = companyModel.getCompany(company.getUser());
        assertNotNull("Get company from db failed.", dbCompany);

        assertEquals(company.ico, dbCompany.ico);
        assertEquals(company.description, dbCompany.description);
        assertEquals(company.name, dbCompany.name);
        assertEquals(company.website, dbCompany.website);

        cleanCompany(company);
    }

    // Company for user
    @Test
    public void removeCompany() throws Exception {
        user = TestHelpers.insertDummyUser();
        Company company = createCompany();
        company.setUser(user);
        companyModel.newCompany(company);

        // Non existing user_email
        assertFalse("Cannot delete company with non existing user", companyModel.deleteCompany("boromir@spolecenstvo-prstenu.cz"));

        // Actually delete company
        assertTrue(companyModel.deleteCompany(user.email));

        // Check if company exists
        Company dbCompany = companyModel.getCompany(company.getUser());
        assertNull("Company should not exist", dbCompany);
    }



    /** =====================
     *      HELPERS
     *  ===================== **/



    private Company createCompany() {
        Company company = new Company();
        Random rand = new Random();
        company.ico = Integer.toString(rand.nextInt(999999));
        company.description = "Meh";
        company.name = "MehCorporation";
        company.website = "MehCorp.com";

        return company;
    }

    private boolean cleanCompany(Company company) {
        // Test if inserted the right data
        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("DELETE FROM company WHERE ico = ?");
            s.setString(1, company.ico);
            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}