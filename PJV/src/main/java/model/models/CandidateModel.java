package model.models;

import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.entities.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class CandidateModel extends DatabaseModel {
    private static Logger LOGGER = Logger.getLogger(CandidateModel.class.getName());


    public CandidateModel(Connection connection) {
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
     * Saves new candidate to database.
     * @param candidate candidate to save to database.
     * @return true if insert is valid. false if insert failed.
     */
    public boolean newCandidate(Candidate candidate){

        PreparedStatement s;
        try {
            connection.setAutoCommit(false);

            s = connection.prepareStatement("INSERT INTO candidate (user_email, job_title, description, looking_for_job) " +
                    "VALUES (?, ?, ?, ?)");
            s.setString(1, candidate.getUser().email);
            s.setString(2, candidate.job_title);
            s.setString(3, candidate.description);
            s.setBoolean(4, candidate.looking_for_job);

            s.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert candidate with user email "+candidate.getUser().email+" failed.");
            return false;
        }
        LOGGER.fine("Insert candidate with user email "+candidate.getUser().email+" passed.");
        return true;
    }

    /**
     * Loads candidate from database based on its user account.
     * @param user User whose candidate should be loaded.
     * @return instance of Candidate. Null if there is no candidate for user.
     */
    public Candidate getCandidate(User user){
        Candidate candidate = null;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM candidate WHERE user_email = ?");
            preparedStatement.setString(1, user.email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                candidate = new Candidate();
                candidate.id = resultSet.getInt("id");
                candidate.job_title = (resultSet.getString("job_title"));
                candidate.setUser(user);
                candidate.description = resultSet.getString("description");
                candidate.looking_for_job = resultSet.getBoolean("looking_for_job");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select candidate with user email " + user.email + " failed in database.");
            return null;
        }
        LOGGER.fine( candidate == null ? "Select candidate with user email " + user.email + " failed" : "Select candidate with user email " + user.email + " succeed.");
        return candidate;
    }

    /***
     * FIlters candidates by their skills
     * @param skills ArrayList of skills
     * @return ArrayList of candidates
     */
    public ArrayList<Candidate> getCandidatesBySkills(ArrayList<Skill> skills){
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        PreparedStatement preparedStatement;
        String sql = "SELECT can.id, can.user_email, can.job_title, can.description, can.looking_for_job " +
                "FROM candidate AS can " +
                "JOIN candidate_has_skill AS chs ON (can.id = chs.candidate_id) " +
                "WHERE chs.skill_name IN (?";
        for (int i = 1; i < skills.size(); i++){ sql += ",?"; }
        sql += ") GROUP BY can.id, can.user_email, can.job_title, can.description, can.looking_for_job";
        try {
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < skills.size(); i++){
                preparedStatement.setString(i+1, skills.get(i).name);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Candidate candidate = new Candidate();
                candidate = (Candidate) candidate.createFromResultSet(resultSet);
                User user = getUserByEmail(resultSet.getString("user_email"));
                candidate.setUser(user);
                candidates.add(candidate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select candidates by skills failed in database.");
            return null;
        }
        LOGGER.fine("Select candidates by skill succeed.");
        return candidates;
    }

    /**
     * Deletes candidate based on his users email
     * @param email email of candidates user
     * @return true if delete was successful. False if wasn't.
     */
    public boolean deleteCandidate(String email) {
        try {
            CallableStatement deleteCandidateProc = connection.prepareCall("{ call remove_candidate( ? ) }");
            deleteCandidateProc.setString(1, email);
            deleteCandidateProc.execute();
            deleteCandidateProc.close();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Delete candidate with user email " + email + " failed in database.");
            return false;
        }
        LOGGER.fine("Delete candidate with user email " + email + " succeed.");
        return true;
    }

    /**
     * Updates candidate row in database
     * @param candidate Candidate to update
     * @return true if update was successful. False if wasn't.
     */
    public boolean editCandidate(Candidate candidate){
        PreparedStatement s;
        try {

            s = connection.prepareStatement("UPDATE candidate SET job_title = ?, looking_for_job = ?, description = ?" +
                    "WHERE candidate.id = ?");
            s.setString(1, candidate.job_title);
            s.setBoolean(2, candidate.looking_for_job);
            s.setString(3, candidate.description);
            s.setInt(4, candidate.id);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Update candidate with user email "+candidate.getUser().email+" failed.");
            return false;
        }
        LOGGER.fine("Update candidate with user email "+candidate.getUser().email+" passed.");
        return true;
    }





}
