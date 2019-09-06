package model.models;

import model.entities.candidate.Candidate;
import model.entities.user.User;
import model.models.factories.ModelFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import model.entities.candidate.Skill;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by lesy on 28.4.17.
 */
public class SkillModelTest {
    private Connection connection;
    private SkillModel skillModel;
    private Candidate candidate;
    private Skill skill1 = new Skill("JavaScrip", "web");
    private Skill skill2 = new Skill(".NET", "C#");
    private Skill skill3 = new Skill("HTML", "web");
    private Skill skill4 = new Skill("PHP", "web");

    private ArrayList<Skill> skills;

    @Before
    public void setUp() throws Exception {

        skillModel = new SkillModel(ModelFactory.getDatabaseConnection());
        connection = skillModel.connection;

        User user = new User();
        user.email = "email@seznam.cz";
        candidate = new Candidate();
        candidate.id = 31;
        candidate.setUser(user);
        candidate.job_title = "Programator";
        candidate.looking_for_job = true;
        candidate.description = "Typas";

        this.skills = new ArrayList<Skill>();
        //skills.add(skill1);
        //skills.add(skill2);
        skills.add(skill3);
        skills.add(skill4);
    }

    @After
    public void tearDown() throws Exception {
        try {
            PreparedStatement s = connection.prepareStatement("DELETE FROM skill_group WHERE id NOT IN (1)"); //smaze vse krome "web"
            s.executeUpdate();

            s = connection.prepareStatement("DELETE FROM skill WHERE name NOT IN ('PHP', 'HTML')");
            s.executeUpdate();

            s = connection.prepareStatement("DELETE FROM candidate_has_skill WHERE candidate_id NOT IN (11)");
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    //New Skill group
    @Test
    public void newSkillGroup() throws Exception {
        ArrayList<String> expectedGroups = getGroupsFromDatabase();
        expectedGroups.add("OS");
        skillModel.newSkillGroup("OS");
        ArrayList<String> dbGroups = getGroupsFromDatabase();
        assertArrayEquals("Insert new Skill group failed", expectedGroups.toArray(), dbGroups.toArray());

        //duplicate
        assertEquals("Insert duplicate Skill group passed.", false, skillModel.newSkillGroup("web"));

        //empty
        assertEquals("Insert empty Skill group passed.", false, skillModel.newSkillGroup(""));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //New Skill
    @Test
    public void newSkill() throws Exception {
        ArrayList<String> dbSkills = new ArrayList<String>();
        ArrayList<String> expectedSkills = new ArrayList<String>(); //musi byt serazeny podle name
        expectedSkills.add("HTML");
        expectedSkills.add(skill1.name);
        expectedSkills.add("PHP");

        skillModel.newSkill(skill1);

        try {
            PreparedStatement s = connection.prepareStatement("SELECT * FROM skill ORDER BY name");
            ResultSet set = s.executeQuery();

            while (set.next()){
                Skill skill = new Skill();
                skill = (Skill) skill.createFromResultSet(set);
                dbSkills.add(skill.name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertArrayEquals("Insert Skill failed.", expectedSkills.toArray(), dbSkills.toArray());

        //duplicate
        assertEquals("Insert duplicate skill name passed.", false, skillModel.newSkill(skill4));

        //null
        assertEquals("Insert null skill passed.", false, skillModel.newSkill(null));

        //group does not exist
        assertEquals("Insert skill with non existing group passed.", false , skillModel.newSkill(new Skill("SkiLL", "123")));


        //empty name or group
       /* exception.expect(RuntimeException.class);
        exception.expectMessage("Skill name can't be empty");
        assertEquals("Insert empty skill name passed.", exception , skillModel.newSkill(new Skill(null, "")));*/

    }

    //Save Skills
    @Test
    public void saveSkills() throws Exception {
        ArrayList<String> dbSkills;
        ArrayList<String> expectedSkills = new ArrayList<String>();
        for (Skill skill: skills) expectedSkills.add(skill.name);

        skillModel.saveSkills(candidate, skills);

        dbSkills = getCandidateSkills();
        assertArrayEquals("Save candidates skills failed", expectedSkills.toArray(), dbSkills.toArray());

        //duplicate for 1 candidate
        skills.add(skill1);
        skillModel.saveSkills(candidate, skills);
        dbSkills = getCandidateSkills();
        assertArrayEquals("Save duplicate skill for one candidate passed.", expectedSkills.toArray(), dbSkills.toArray());

        //empty Skills
        ArrayList<Skill> empty = new ArrayList<Skill>();
        assertEquals("Save empty skills passed.", false, skillModel.saveSkills(candidate, empty));
    }

    //@Test
    public void getCandidatesSkills() throws Exception {
        ArrayList<String> expectedSkills = new ArrayList<String>();
        ArrayList<String> dbStrings = new ArrayList<String>();
        ArrayList<Skill> dbSkills;
        skillModel.saveSkills(candidate, skills);
        dbSkills = skillModel.getCandidatesSkills(candidate);

        for (Skill skill : dbSkills) dbStrings.add(skill.name);
        for (Skill skill : skills) expectedSkills.add(skill.name);

        assertArrayEquals("Get Candidates Skills failed.", expectedSkills.toArray(), dbStrings.toArray());
    }

    private ArrayList<String> getGroupsFromDatabase(){
        ArrayList<String> dbGroups = new ArrayList<String>();
        try {
            PreparedStatement s = connection.prepareStatement("SELECT * FROM skill_group");
            ResultSet set = s.executeQuery();

            while (set.next()) {
                dbGroups.add(set.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbGroups;
    }

    private ArrayList<String> getCandidateSkills(){
        ArrayList<String> result = new ArrayList<String>();
        try {
            PreparedStatement s = connection.prepareStatement("SELECT * " +
                    "FROM skill AS s " +
                    "JOIN candidate_has_skill AS chs ON (s.name = chs.skill_name) " +
                    "WHERE chs.candidate_id = ? ORDER BY s.name");
            s.setInt(1 ,candidate.id);
            ResultSet set = s.executeQuery();

            while (set.next()) {
                Skill skill = new Skill();
                skill = (Skill) skill.createFromResultSet(set);
                result.add(skill.name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}