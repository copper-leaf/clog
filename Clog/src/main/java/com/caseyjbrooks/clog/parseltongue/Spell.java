package com.caseyjbrooks.clog.parseltongue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a method to be a Spell which can be called from within Parseltongue. Spells must be registered like
 * <code>new Parseltongue().findSpells(SpellClass.class)</code>. If a name is given to the annotation, that is the name
 * by which it will be accessible from Parseltongue, otherwise the name of the method will be used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Spell {
    String name() default "";
}
