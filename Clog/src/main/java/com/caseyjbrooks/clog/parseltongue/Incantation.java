package com.caseyjbrooks.clog.parseltongue;

public interface Incantation {

    String getName();

    Object call(Object reagent, Object... reagents);

}
