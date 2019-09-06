package model.models;

import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.entities.company.Company;
import model.entities.company.Message;
import model.entities.user.User;
import model.models.factories.ModelFactory;
import model.models.factories.ModelFactory;
import model.models.factories.ModelFactory;
import org.junit.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


/**
 * Created by lesy on 28.4.17.
 */
public class MessageModelTest {
    private Connection connection;
    private Candidate candidate;
    private Company company;
    private User user;
    private MessageModel messageModel;
    private Message message, message1;



    @Before
    public void setUp() throws Exception {
        messageModel = new MessageModel(ModelFactory.getDatabaseConnection());
        user = new User();
        user.email = "test";
        candidate = new Candidate();
        company = new Company();
        connection = messageModel.connection;
        candidate.id = 11;
        candidate.setUser(user);
        company.ico = "11144477";

        message = new Message();
        message.candidate_id = candidate.id;
        message.company_ico = company.ico;
        message.content = "Content";
        message.date = new Date();
        message.name = "Prvni zprava";


        message1 = new Message();
        message1.candidate_id = 26;
        message1.company_ico = company.ico;
        message1.content = "Jina zprava";
        message1.date = new Date(System.currentTimeMillis()+60000);
        message1.name = "Zprava jinyu kandidatovi";
    }

    @After
    public void tearDown() throws Exception {
        try {
            PreparedStatement s = connection.prepareStatement("DELETE FROM message");
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMessages() throws Exception {
        //set up
        sendMessageForTest(message);
        sendMessageForTest(message1);
        ArrayList<String> expectedMessages = new ArrayList<String>();
        ArrayList<String> dbMessages = new ArrayList<String>();
        expectedMessages.add(message.toString());

        //candidate
        for (Message message :
                messageModel.getMessages(candidate)) {
            dbMessages.add(message.toString());
        }
        assertArrayEquals("Get Candidates messages failed", expectedMessages.toArray(), dbMessages.toArray());

        //company
        dbMessages.clear();
        expectedMessages.add(message1.toString());
        for (Message message :
                messageModel.getMessages(company)) {
            dbMessages.add(message.toString());
        }
        assertArrayEquals("Get Companies messages failed", expectedMessages.toArray(), dbMessages.toArray());
    }


    @Test
    public void sendMessage() throws Exception {
        ArrayList<String> expectedMessages = new ArrayList<String>();

        //company
        expectedMessages.add(message.toString());
        expectedMessages.add(message1.toString());
        messageModel.sendMessage(message);
        messageModel.sendMessage(message1);
        ArrayList<String> dbMessages = getComapniesMessages(company);

        assertArrayEquals("Send messages failed", expectedMessages.toArray(), dbMessages.toArray());

        //candidate
        expectedMessages.clear();
        expectedMessages.add(message.toString());
        dbMessages = getCandidatesMessages(candidate);

        assertArrayEquals("Send messages failed", expectedMessages.toArray(), dbMessages.toArray());

        //empty message
        assertEquals("Insert null message passed.", false, messageModel.sendMessage(null));
        message.name = "";
        message.content = "";
        assertEquals("Insert empty content or name message passed.", false, messageModel.sendMessage(message));
    }

    private void sendMessageForTest(Message message){
        PreparedStatement s;
        try {
            s = connection.prepareStatement("INSERT INTO message (name, content, date, company_ico, candidate_id) " +
                    "VALUES (?, ?, ?, ?, ?)");
            s.setString(1, message.name);
            s.setString(2, message.content);
            s.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            s.setString(4, message.company_ico);
            s.setInt(5, message.candidate_id);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getCandidatesMessages(Candidate candidate){
        ArrayList<String> result = new ArrayList<String>();
        try {
            PreparedStatement s = connection.prepareStatement("SELECT * " +
                    "FROM message AS s " +
                    "WHERE candidate_id = ?");
            s.setInt(1 ,candidate.id);
            ResultSet set = s.executeQuery();

            while (set.next()) {
                Message message = new Message();
                message = (Message) message.createFromResultSet(set);
                result.add(message.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String> getComapniesMessages(Company company){
        ArrayList<String> result = new ArrayList<String>();
        try {
            PreparedStatement s = connection.prepareStatement("SELECT * " +
                    "FROM message AS s " +
                    "WHERE company_ico = ?");
            s.setString(1 ,company.ico);
            ResultSet set = s.executeQuery();

            while (set.next()) {
                Message message = new Message();
                message = (Message) message.createFromResultSet(set);
                result.add(message.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}