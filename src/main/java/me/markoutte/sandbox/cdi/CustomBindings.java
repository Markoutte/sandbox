package me.markoutte.sandbox.cdi;

import com.google.inject.AbstractModule;

public class CustomBindings extends AbstractModule {
    @Override
    protected void configure() {
        bind(CustomService.class).to(CustomServiceImpl.class);
    }
}
