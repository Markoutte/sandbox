package me.markoutte.sandbox.stupid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Once upon a time men and women live together.
 *
 * But they wanted to split for community and do they work, as they wish...
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-09
 */
public class PECS {

    public static class Community<T extends Human> {

        private final List<T> people = new ArrayList<>();

        /**
         * You can invite people to this community
         */
        public void invite(Collection<? extends T> people) {
            this.people.addAll(people);
        }

        /**
         * You can call it!
         */
        public void call(HumanCaller<? super T> caller) {
            System.out.println("Start calling");
            for (T t : people) {
                caller.call(t);
            }
            System.out.println("And ends\n");
        }

        public interface HumanCaller<T extends Human> {
            void call(T human);
        }
    }

    public static void main(String[] args) {

        Community.HumanCaller<Human> generalCaller = human -> System.out.println(human.getName());
        Community.HumanCaller<Man> manCaller = man -> System.out.println(man.getName() + man.repairTable());
        Community.HumanCaller<Woman> womanCaller = woman -> System.out.println(woman.getName() + woman.buyProducts());

        Man george = new Man("George");
        Man bob = new Man("Bob");
        Man robby = new Man("Robby");
        Woman helen = new Woman("Helen");
        Woman kate = new Woman("Kate");

        // Men

        Community<Man> manCommunity = new Community<>();
        manCommunity.invite(Arrays.asList(george, bob, robby));

        manCommunity.call(generalCaller);
        manCommunity.call(manCaller);
//        manCommunity.call(womanCaller); <-- error

        // Women

        Community<Woman> womanCommunity = new Community<>();
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

}
