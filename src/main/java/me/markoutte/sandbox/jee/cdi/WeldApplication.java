package me.markoutte.sandbox.jee.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldApplication {

    public static void main(String[] args) {
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        BookService service = container.instance().select(BookService.class).get();
        Book book = service.createBook("Beginning Java EE 7", "Antonio Goncalves", 1200f, "About Java EE7");
        System.out.println(book);
        weld.shutdown();
    }

}
