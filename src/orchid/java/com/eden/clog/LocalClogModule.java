package com.eden.clog;

import com.eden.orchid.api.registration.OrchidModule;
import com.vladsch.flexmark.util.options.MutableDataSet;

public class LocalClogModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(MutableDataSet.class);
    }

}
