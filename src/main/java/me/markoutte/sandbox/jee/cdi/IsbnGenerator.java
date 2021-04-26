package me.markoutte.sandbox.jee.cdi;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.Random;
import java.util.logging.Logger;

@ThirteenDigits @Default
public class IsbnGenerator implements NumberGenerator {

    @Inject
    private Logger logger;

    @Override
    public String generateNumber() {
        String isbn = "13-84356-" + Math.abs(new Random().nextInt());
        logger.info("Generated ISBN : " + isbn);
        return isbn;
    }
}
