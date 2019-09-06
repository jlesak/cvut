package model.models;

import model.entities.candidate.Candidate;
import model.entities.candidate.Experience;
import model.entities.candidate.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class SkillModel extends DatabaseModel {
    private static Logger LOGGER = Logger.getLogger(SkillModel.class.getName());


    public SkillModel(Connection connection) {
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
     * Saves new skillGroup to database
     * @param skillGroupName Name of new skillGroup
     * @return true if insert is successful. false if insert failed.
     */
    public boolean newSkillGroup(String skillGroupName){
        if (skillGroupName.isEmpty()) return false;

        try {
            PreparedStatement s = connection.prepareStatement("INSERT INTO skill_group (name) " +
                    "VALUES (?)");
            s.setString(1, skillGroupName);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert new skill group with name " + skillGroupName + " failed in database.");
            return false;
        }
        LOGGER.fine("Insert new skill group with name " + skillGroupName + " passed.");
        return true;
    }

    /**
     * Saves new skill to database.
     * @param skill Skill to save
     * @return true if insert is successful. false if insert failed.
     */
    public boolean newSkill(Skill skill){
        if (skill == null) return false;
        if (skill.name.isEmpty()) return false;

        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("INSERT INTO skill (name, s_group) " +
                    "VALUES (?, ?)");
            s.setString(1, skill.name);
            s.setString(2, skill.s_group);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert new skill with name" + skill.name + " failed in database.");
            return false;
        }
        LOGGER.fine("Insert new skill with name" + skill.name + " passed.");
        return true;
    }

    /**
     * Saves candidates skills to database.
     * @param candidate Candidate
     * @param skills ArrayList of candidates skills names
     * @return true if insert is successful. false if insert failed.
     */
    public boolean saveSkills(Candidate candidate, ArrayList<Skill> skills){
        if (skills.size() == 0) return false;
        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("INSERT INTO candidate_has_skill (candidate_id, skill_name) " +
                    "VALUES (?, ?)");
            for (Skill skill : skills) {
                s.setInt(1, candidate.id);
                s.setString(2, skill.name);
                s.addBatch();
            }
            s.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Save candidates skills with candidate user email " + candidate.getUser().email + " failed in database.");
            return false;
        }
                LOGGER.fine("Save candidates skills with candidate user email " + candidate.getUser().email + " succeed.");
        return true;
    }

    /**
     * Saves candidates experiences to database.
     * @param candidate Candidate
     * @param experiences ArrayList of candidates experiences
     * @return true if insert is successful. false if insert failed.
     */
    public boolean saveExperiences(Candidate candidate, ArrayList<Experience> experiences){

        try {
            PreparedStatement s = connection.prepareStatement("INSERT INTO experience (candidate_id, title, position, date_from, date_to, description, experience_type, company) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            for (Experience experience : experiences) {

                java.sql.Date from = new java.sql.Date(experience.date_from.getTime());
                java.sql.Date to = new java.sql.Date(experience.date_to.getTime());

                s.setInt(1, candidate.id);
                s.setString(2, experience.title);
                s.setString(3, experience.position);
                s.setDate(4, from);
                s.setDate(5, to);
                s.setString(6, experience.description);
                s.setString(7, experience.experience_type);
                s.setString(8, experience.company);
                s.addBatch();
            }

            s.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Save candidates experiences with candidate user email " + candidate.getUser().email + " failed in database.");
            return false;
        }
        LOGGER.fine("Save candidates experiences with candidate user email " + candidate.getUser().email + " succeed.");
        return true;
    }

    /**
     * Loads all experiences types from database
     * @return ArrayList of all Experiences types
     */
    public ArrayList<String> getAllExperienceTypes(){
        ArrayList<String> types = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM experience_type");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                types.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select all experience types failed in database.");
            return null;
        }
        LOGGER.fine("Select all experience types succeed.");
        return types;
    }

    /**
     * Loads all candidates skills
     * @param candidate Candidate
     * @return ArrayList of candidates skills
     */
    public ArrayList<Skill> getCandidatesSkills(Candidate candidate){
        ArrayList<Skill> skills = new ArrayList<Skill>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM skill AS s " +
                    "JOIN candidate_has_skill AS chs ON (s.name = chs.skill_name) " +
                    "WHERE chs.candidate_id = ? ORDER BY s.name");
            preparedStatement.setInt(1, candidate.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Skill skill = new Skill();
                skill = (Skill) skill.createFromResultSet(resultSet);
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select skills for candidate with user email "+candidate.getUser().email+" failed in database.");
            return null;
        }
        LOGGER.fine("Select skills for candidate with user email "+candidate.getUser().email+" succeed.");
        return skills;
    }

    /**
     * Loads all candidates experiences
     * @param candidate Candidate
     * @return ArrayList of candidates experiences
     */
    public ArrayList<Experience> getCandidatesExperiences(Candidate candidate){
        ArrayList<Experience> experiences = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM experience AS exp " +
                    "WHERE exp.candidate_id = ? ORDER BY exp.date_from");
            preparedStatement.setInt(1, candidate.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Experience experience = new Experience();
                experience = (Experience) experience.createFromResultSet(resultSet);
                experiences.add(experience);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select experiences for candidate with user email "+candidate.getUser().email+" failed in database.");
            return null;
        }
        LOGGER.fine("Select experiences for candidate with user email "+candidate.getUser().email+" succeed.");
        return experiences;
    }

    /**
     * Loads all skills from database
     * @return ArrayList of all skills
     */
    public ArrayList<Skill> getAllSkills(){
        ArrayList<Skill> skills = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM skill");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Skill skill = new Skill();
                skill = (Skill) skill.createFromResultSet(resultSet);
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select all skills failed in database.");
            return null;
        }
        LOGGER.fine("Select all skills succeed.");
        return skills;
    }

    public boolean refreshCandidateSkills(Candidate candidate, ArrayList<Skill> skills){
        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("DELETE FROM candidate_has_skill WHERE candidate_id = ?" );
            s.setInt(1, candidate.id);
            s.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Delete skills fro candidate with user email" + candidate.getUser().email + " failed in database.");
            return false;
        }
        LOGGER.fine("Delete skills fro candidate with user email" + candidate.getUser().email + " passed.");
        return saveSkills(candidate, skills);
    }

}
