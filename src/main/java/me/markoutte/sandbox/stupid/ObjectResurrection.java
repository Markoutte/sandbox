package me.markoutte.sandbox.stupid;

import java.util.ArrayList;
import java.util.List;

public class ObjectResurrection {

    private static List<ObjectResurrection> zombies = new ArrayList<>();

    private String name = "Mouse";

    public static void main(String[] args) throws Exception {
        ObjectResurrection zombie = new ObjectResurrection();
        zombie = null;
        System.gc();
        Thread.sleep(300);
        System.out.println(zombies.get(0));
        zombies.clear();
        System.gc();
        Thread.sleep(300);
    }

    protected void finalize(){
        System.out.println("finalize");
        zombies.add(this);
    }

    public String toString(){
        return name;
    }

}
