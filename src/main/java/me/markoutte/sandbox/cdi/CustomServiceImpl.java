package me.markoutte.sandbox.cdi;

import com.google.inject.Singleton;

@Singleton
public class CustomServiceImpl implements CustomService {
    @Override
    public void welcome(String name) {
        System.out.println("Hello, " + (name == null ? "World" : name));
    }
}
