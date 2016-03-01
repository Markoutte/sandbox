package me.markoutte.sandbox.stupid;

import java.io.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-01
 */
public class DoubleObjects {

    private static class Singleton implements java.lang.Cloneable, Serializable {

        private static Singleton instance;

        public static synchronized Singleton getInstance() {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }

        public Singleton() {
            System.out.println("New instance created: " + this);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        Singleton instance = Singleton.getInstance();
        Singleton clone = (Singleton) instance.clone();
        System.out.println(clone);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream stream = new ObjectOutputStream(baos)) {
            stream.writeObject(instance);
        };

        try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            Singleton deserialized = (Singleton) stream.readObject();
            System.out.println(deserialized);
        }
    }

}
