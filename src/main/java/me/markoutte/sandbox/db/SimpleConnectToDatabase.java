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
        Class.forName("org.h2.Driver");
        Properties properties = new Properties();
        properties.put("user", "sa");
        properties.put("password", "");
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", properties)) {
            Statement st = conn.createStatement();
            st.execute("create table owner(id serial, name varchar, age integer);");
            st.execute("drop table owner;");
            st.close();
        }
    }

}
