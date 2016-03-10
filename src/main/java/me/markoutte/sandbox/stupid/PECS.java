package me.markoutte.sandbox.stupid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Once upon a time men and women live together.
 *
 * But they wanted to split for community and do they work, as they wish...
 *
 * PECS means «Producer extends, Consumer super» by Joshua Bloch.
 *
 * @see https://habrahabr.ru/post/207360/
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-09
 */
public class PECS {

    /**
     * The community of specific humans
     * @param <T> Maybe men, maybe women, maybe somebody else...
     */
    public static class Community<T extends Human> {

        private final List<T> people = new ArrayList<>();
        private final String communityName;

        public Community(String communityName) {
            this.communityName = communityName;
        }

        /**
         * You can invite people to this community. But only T and subclass of T can be invited.
         */
        public void invite(Collection<? extends T> people) {
            this.people.addAll(people);
        }

        /**
         * You can call anybody with custom caller!
         *
         * The caller can be generic for Human, because T is a human, and can be for T,
         * but not subclass of T can be used, because Community knows nothing about
         * specific subclass of T.
         */
        public void call(HumanCaller<? super T> caller) {
            System.out.println("Call in \"" + this + "\" community:");
            people.forEach(caller);
            System.out.println();
        }

        public interface HumanCaller<T extends Human> extends Consumer<T> {
        }

        @Override
        public String toString() {
            return communityName;
        }
    }

    public static void main(String[] args) {

        Community.HumanCaller<Human> generalCaller = human -> System.out.println(human.getName());
        Community.HumanCaller<Man> manCaller = man -> System.out.println(man.getName() + man.repairTable());
        Community.HumanCaller<Woman> womanCaller = woman -> System.out.println(woman.getName() + woman.buyProducts());
        Community.HumanCaller<Boy> boyCaller = boy -> System.out.println(boy.getName() + boy.playFootball());

        Man george = new Man("George");
        Man bob = new Man("Bob");
        Man robby = new Man("Robby");
        Woman helen = new Woman("Helen");
        Woman kate = new Woman("Kate");
        Boy zakk = new Boy("Zakk");

        // Men

        Community<Man> manCommunity = new Community<>("Men");
        manCommunity.invite(Arrays.asList(george, bob, robby, zakk));

        manCommunity.call(generalCaller); // it's ok, we can work with man, because he's human
        manCommunity.call(manCaller);
//        manCommunity.call(womanCaller); // <-- compile error, because woman is not man
//        manCommunity.call(boyCaller); // <-- compile error, because boy is subclass of man

        // Women

        Community<Woman> womanCommunity = new Community<>("Women");
        womanCommunity.invite(Arrays.asList(helen, kate));

        womanCommunity.call(generalCaller);
//        womanCommunity.call(manCaller); <-- error
        womanCommunity.call(womanCaller);
    }

    public abstract static class Human {

        private final String name;

        public Human(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Woman extends Human {

        public Woman(String name) {
            super(name);
        }

        public String buyProducts() {
            return " buying products";
        }

    }

    public static class Man extends Human {

        public Man(String name) {
            super(name);
        }

        public String repairTable() {
            return  " repairs table";
        }
    }

    public static class Boy extends Man {

        public Boy(String name) {
            super(name);
        }

        public String playFootball() {
            return " playing football";
        }
    }

}
