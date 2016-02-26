package me.markoutte.sandbox.cdi;

import com.google.inject.AbstractModule;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-26
 */
public class CustomBindings extends AbstractModule {
    @Override
    protected void configure() {
        bind(CustomService.class).to(CustomServiceImpl.class);
    }
}
