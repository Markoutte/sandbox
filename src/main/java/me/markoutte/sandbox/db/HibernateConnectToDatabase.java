package me.markoutte.sandbox.db;

import me.markoutte.sandbox.db.dao.Owner2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnectToDatabase {

    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        Owner2 owner2 = new Owner2();
        owner2.setName("Hello");
        owner2.setAge(12);

        session.save(owner2);
        session.getTransaction().commit();
        
        session.close();
        factory.close();
    }

}
