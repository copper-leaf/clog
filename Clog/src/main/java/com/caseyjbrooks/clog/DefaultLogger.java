package com.caseyjbrooks.clog;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * The standard Clog Logger implementation, used if no other loggers have been registered. Displays messages in the
 * following format:
 *
 * [LEVEL] Tag: Message
 */
public class DefaultLogger implements ClogLogger {
    static {
        // hack to get IntelliJ to show output codes. Jansi doesn't determine the IntelliJ console to be ansi-compatible
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
    }

    private final Clog.Priority priority;

    public DefaultLogger() {
        this(Clog.Priority.DEFAULT);
    }

    public DefaultLogger(Clog.Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int log(String tag, String message) {
        AnsiConsole.out.println(getAnsiLevelString().a(getTagString(tag)).a(message));
        return 0;
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        AnsiConsole.out.println(getAnsiLevelString().a(getTagString(tag)).a(message).a(getThrowableString(throwable)));
        return 0;
    }

    @Override
    public Clog.Priority priority() {
        return priority;
    }

    Ansi getAnsiLevelString() {
        if(priority != null) {
            switch (priority) {
                case VERBOSE:
                    return ansi().fg(GREEN).a("[VERBOSE] ").reset();
                case DEBUG:
                    return ansi().fg(BLUE).a("[DEBUG] ").reset();
                case INFO:
                    return ansi().fg(CYAN).a("[INFO] ").reset();
                case WARNING:
                    return ansi().fg(YELLOW).a("[WARN] ").reset();
                case ERROR:
                    return ansi().fg(RED).a("[ERROR] ").reset();
                case FATAL:
                    return ansi().fg(MAGENTA).a("[FATAL] ").reset();
                case DEFAULT:
                default:
                    return ansi().a("");
            }
        }

        return Ansi.ansi();
    }

    private String getTagString(String tag) {
        if(tag != null && tag.length() > 0) {
            return tag + ": ";
        }
        else {
            return "";
        }
    }

    private String getThrowableString(Throwable throwable) {
        return " (" + throwable.getMessage() + ")";
    }
}
