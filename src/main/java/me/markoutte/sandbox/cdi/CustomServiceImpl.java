package me.markoutte.sandbox.cdi;

import com.google.inject.Singleton;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-26
 */
@Singleton
public class CustomServiceImpl implements CustomService {
    @Override
    public void welcome(String name) {
        System.out.println("Hello, " + (name == null ? "World" : name));
    }
}
