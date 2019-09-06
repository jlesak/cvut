package model.models;

import model.entities.candidate.Candidate;
import model.entities.company.Company;
import model.entities.company.Message;
import model.entities.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class MessageModel extends DatabaseModel {
    private static Logger LOGGER = Logger.getLogger(MessageModel.class.getName());

    public MessageModel(Connection connection) {
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
     * Loads all messages for company.
     * @param company Company
     * @return ArrayList of messages
     */
    public ArrayList<Message> getMessages(Company company){
        ArrayList<Message> messages = new ArrayList<Message>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM message WHERE company_ico = ?");
            preparedStatement.setString(1, company.ico);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                                Message message = new Message();
                message.id = resultSet.getInt("id");
                message.name = resultSet.getString("name");
                message.date = new Date((resultSet.getDate("date")).getTime());
                message.candidate_id = resultSet.getInt("candidate_id");
                message.company_ico = resultSet.getString("company_ico");
                message.content = resultSet.getString("content");

                messages.add(message);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select messages for company with ico " + company.ico + " failed in database.");
            return null;
        }
        LOGGER.fine("Select messages for company with ico " + company.ico + " succeed.");
        return messages;
    }

    /**
     * Loads all messages for candidate.
     * @param candidate Candidate
     * @return ArrayList of messages
     */
    public ArrayList<Message> getMessages(Candidate candidate){
        ArrayList<Message> messages = new ArrayList<Message>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM message WHERE candidate_id = ?");
            preparedStatement.setInt(1, candidate.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Message message = new Message();
                message.id = resultSet.getInt("id");
                message.name = resultSet.getString("name");
                message.date = new Date((resultSet.getDate("date")).getTime());
                message.candidate_id = resultSet.getInt("candidate_id");
                message.company_ico = resultSet.getString("company_ico");
                message.content = resultSet.getString("content");

                message.setCompany(getCompany(message.company_ico));
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select messages for candidate with user email " + candidate.getUser().email + " failed in database.");
            return null;
        }
        LOGGER.fine("Select messages for candidate with user email " + candidate.getUser().email + " succeed.");
        return messages;
    }

    /**
     * Saves message from company to candidate to database
     * @param message Message to save
     * @return true if insert was successful false if it wasn't
     */
    public boolean sendMessage(Message message){
        if (message == null) return false;
        if (message.content.isEmpty() || message.name.isEmpty()) return false;
        java.sql.Date date = new java.sql.Date(message.date.getTime());

        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("INSERT INTO message (name, content, date, company_ico, candidate_id) " +
                    "VALUES (?, ?, ?, ?, ?)");
            s.setString(1, message.name);
            s.setString(2, message.content);
            s.setDate(3, date);
            s.setString(4, message.company_ico);
            s.setInt(5, message.candidate_id);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert message failed in database.");
            return false;
        }
        LOGGER.fine("Insert message succeed.");
        return true;
    }
}
