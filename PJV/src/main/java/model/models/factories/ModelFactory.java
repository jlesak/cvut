package model.models.factories;

import model.models.DatabaseModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.*;

/**
 * Created by fifa on 24.4.17.
 */
public class ModelFactory {

    private static final String CONNECTION = "jdbc:postgresql://slon.felk.cvut.cz:5432/db17_lesakjan";
    private static final String USERNAME = "db17_lesakjan";
    private static final String PASSWORD = "dbspjv17";

    protected static Logger LOGGER = Logger.getLogger(ModelFactory.class.getName());


    public ModelFactory() {
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

    private static Connection connection;

    public static Connection getDatabaseConnection() {
        try {

            if(connection == null) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
                LOGGER.fine("Connection to database " + CONNECTION + " with username " + USERNAME + " and password " + PASSWORD + " created.");
            }

            return connection;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.severe("Connection to database " + CONNECTION + " with username " + USERNAME + " and password " + PASSWORD + " failed.");
        }

        return null;
    }
}
