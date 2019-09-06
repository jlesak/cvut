package model.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by fifa on 25.4.17.
 */
public class BaseEntity {
    public BaseEntity createFromResultSet(ResultSet rs) {
        try {

            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if(Modifier.isPublic(field.getModifiers())) {
                    field.set(this, rs.getObject(field.getName()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return this;
    }
}
