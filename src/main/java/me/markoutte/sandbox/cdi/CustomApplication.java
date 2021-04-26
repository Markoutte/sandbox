package me.markoutte.sandbox.cdi;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class CustomApplication {

    @Inject
    private CustomService service;

    public void test() {
        service.welcome("Maks");
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CustomBindings());
        CustomApplication instance = injector.getInstance(CustomApplication.class);
        instance.test();
    }

}
