package me.markoutte.sandbox.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-10
 */
public class SimpleConnectToDatabase {

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Properties properties = new Properties();
        properties.put("user", "postgres");
        properties.put("password", "postgres");
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/test", properties)) {
            Statement st = conn.createStatement();
            st.execute("drop table owner;");
            st.execute("create table owner(id serial, name varchar, age integer);");
            st.close();
        }
    }

}
