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

    private String logger;
    private int priority;

    public DefaultLogger(String logger, int priority) {
        this.logger = logger;
        this.priority = priority;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int log(String tag, String message) {
        AnsiConsole.out.println(getAnsiLevelString().a(tag + ": ").a(message));
        return 0;
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        AnsiConsole.out.println(getAnsiLevelString().a(tag + ": ").a(message).a(" (" + throwable.getMessage() + ")"));
        return 0;
    }

    @Override
    public int priority() {
        return priority;
    }

    private Ansi getAnsiLevelString() {
        if(logger != null) {
            switch (logger) {
                case Clog.KEY_V:
                    return ansi().fg(GREEN).a("[VERBOSE] ").reset();
                case Clog.KEY_D:
                    return ansi().fg(BLUE).a("[DEBUG] ").reset();
                case Clog.KEY_I:
                    return ansi().fg(CYAN).a("[INFO] ").reset();
                case Clog.KEY_W:
                    return ansi().fg(YELLOW).a("[WARN] ").reset();
                case Clog.KEY_E:
                    return ansi().fg(RED).a("[ERROR] ").reset();
                case Clog.KEY_WTF:
                    return ansi().fg(MAGENTA).a("[FATAL] ").reset();
                default:
                    break;
            }
        }

        return Ansi.ansi();
    }
}
