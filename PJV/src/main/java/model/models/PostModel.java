package model.models;

import model.entities.candidate.Candidate;
import model.entities.company.Company;
import model.entities.company.Post;
import model.entities.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class PostModel extends DatabaseModel {
    private static Logger LOGGER = Logger.getLogger(PostModel.class.getName());

    public PostModel(Connection connection) {
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
     * Loads all posts from database
     * @return ArrayList of all Posts
     */
    public ArrayList<Post> getPosts(){
        ArrayList<Post> posts = new ArrayList<Post>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM post");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Post post = new Post();
                post = (Post) post.createFromResultSet(resultSet);
                post.setCompany(getCompany(post.company_ico));
                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select all posts from database failed.");
            return null;
        }
        LOGGER.fine("Select all posts from database succeed.");
        return posts;
    }

    /**
     * Saves new post to database
     * @param post Post to save
     * @return true if insert was successful false if it wasn't
     */
    public boolean newPost(Post post){
        PreparedStatement s = null;
        try {
            s = connection.prepareStatement("INSERT INTO post (title, company_ico, description) " +
                    "VALUES (?, ?, ?)");
            s.setString(1, post.title);
            s.setString(2, post.company_ico);
            s.setString(3, post.description);

            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Insert new post to database failed.");
            return false;
        }
        LOGGER.fine("Insert new post to database succeed.");
        return true;
    }

    public Post getCompanyPost(Company company) {
        Post post = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE company_ico = ?");
            preparedStatement.setString(1, company.ico);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                post = new Post();
                post = (Post) post.createFromResultSet(resultSet);
                post.setCompany(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.severe("Select post with company ico " + company.ico + " failed in database.");
            return null;
        }
        LOGGER.fine( post == null ? "Select post with company ico " + company.ico + " failed" : "Select post with company ico " + company.ico + " succeed.");
        return post;
    }
}
