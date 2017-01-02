# Logging

### The Basics

Logging with Clog is simple. Simply pick your logging priority and call that static method with the data you want to log. Clog will figure out the rest. The most basic log looks like this:

`Clog.log("Hello World");`

Clog has six built-in priorities, but can support as many or as few as you need. Those six priorities, from lowest to highest, are listed in the table below. In addition to those six priorities, there is a default logger which will be used in the case that a logger for a priority is not defined, and you can define your own custom logging priorities and refere to them by their key.

<table class="table table-condensed">
    <thead>
    <tr>
        <th>Priority</th>
        <th>Key</th>
        <th>Clog method</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><b>Verbose</b></td>
        <td><code>Clog.KEY_V, "v"</code></td>
        <td><code>Clog.v(...)</code></td>
    </tr>
    <tr>
        <td><b>Debug</b></td>
        <td><code>Clog.KEY_D, "d"</code></td>
        <td><code>Clog.d(...)</code></td>
    </tr>
    <tr>
        <td><b>Info</b></td>
        <td><code>Clog.KEY_I, "i"</code></td>
        <td><code>Clog.i(...)</code></td>
    </tr>
    <tr>
        <td><b>Warning</b></td>
        <td><code>Clog.KEY_W, "w"</code></td>
        <td><code>Clog.w(...)</code></td>
    </tr>
    <tr>
        <td><b>Error</b></td>
        <td><code>Clog.KEY_E, "e"</code></td>
        <td><code>Clog.e(...)</code></td>
    </tr>
    <tr>
        <td><b>Fatal (What a Terrible Failure)</b></td>
        <td><code>Clog.KEY_WTF, "wtf"</code></td>
        <td><code>Clog.wtf(...)</code></td>
    </tr>
    <tr>
        <td><b>Default</b></td>
        <td><code>null</code></td>
        <td><code>Clog.log(...)</code></td>
    </tr>
    <tr>
        <td><b>Custom</b></td>
        <td>anything (see below)</td>
        <td><code>Clog.logger(key, ...)</code></td>
    </tr>
    </tbody>
</table>

For each of the above priorities, there are six logging methods that can be used, described below. Here, I use all with the 'log' priority, but all priorities and custom loggers support the same API:

<table class="table table-condensed">
    <thead>
    <tr>
        <th>Method</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><pre>log(Throwable throwable)</pre></td>
        <td>Logs an exception. Uses the default tag (see below).</td>
    </tr>
    <tr>
        <td><pre>log(String tag, Throwable throwable)</pre></td>
        <td>Logs an exception with the given tag.</td>
    </tr>
    <tr>
        <td><pre>log(String message, Object... args)</pre></td>
        <td>Formats the message with arguments and logs the message. Uses the default tag.</td>
    </tr>
    <tr>
        <td><pre>log(String tag, String message, Object... args)</pre></td>
        <td>Formats the message with arguments and logs the message with the given tag.</td>
    </tr>
    <tr>
        <td><pre>log(String formatString, Throwable throwable, Object... args)</pre></td>
        <td>Logs an exception with the formatted message. Uses the default tag.</td>
    </tr>
    <tr>
        <td><pre>log(String tag, String formatString, Throwable throwable, Object... args)</pre></td>
        <td>Logs an exception with the formatted message and the given tag.</td>
    </tr>
    </tbody>
</table>
</p>

### Default Tag

Each message will be logged with a tag. If no tag is supplied to the logging method, then Clog will use a default tag. Any string can be used as a tag, but it should be kept short. The Android Log, which this library is inspired by, puts a strict limit of 26 characters on a tag, but no such limit is used with Clog. The default tag can be set with <code>setDefaultTag(String)</code>, but if the default tag is null, Clog will attempt to find the caller's simple class name and use that as the tag.

### Custom Loggers

By default, Clog has only a single logger implemented, which is a wrapper around <code>System.out.println()</code> which concatenates the tag and message. For any large applications, you will want to replace this basic logger with wrappers around your standard or more powerful logging libraries.

> <i>See <a href="https://github.com/JavaEden/Android-Clog">Clog for Android</a> and <a href="https://github.com/JavaEden/Clog4j">Clog4j</a> for specialized Clog implementations</i>

The simple way to use your custom loggers is to add it directly to Clog. You can also set up multiple profiles, each with their own set of loggers (see below for more on profiles). A custom logger is any class that implements the <code>ClogLogger</code> interface, which has the following methods:

<table class="table table-condensed">
    <thead>
    <tr>
        <th>Method</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><pre>boolean isActive()</pre></td>
        <td>Checks whether this logger actually does anything. As an optimization, messages will not be formatted if they are sent to an inactive logger.</td>
    </tr>
    <tr>
        <td><pre>int log(String tag, String message)</pre></td>
        <td>Logs a message with the given tag. Returns value is mostly unnecessary, but can be used to return metadata such as the number of bytes written.</td>
    </tr>
    <tr>
        <td><pre>int log(String tag, String message, Throwable throwable)</pre></td>
        <td>Logs a message and a throwable with the given tag. Returns value is mostly unnecessary, but can be used to return metadata such as the number of bytes written.</td>
    </tr>
    </tbody>
</table>


To add your custom logger to Clog, call <code>addLogger</code> with your key and an instance of your custom logger, or set it as the default with <code>setDefaultLogger</code>. Loggers can be removed with <code>removeLogger</code>. Below is an example of how to add and use your custom loggers.

    MyLogger myLogger = new MyLogger();

    // set it as a fully custom logger
    Clog.addLogger("loggerKey", myLogger);
    Clog.logger("loggerKey", "Hello world");

    // set it as a priority logger
    Clog.addLogger(KEY_I, myLogger);
    Clog.i("Hello world");

    // set it as the default logger
    Clog.setDefaultLogger(myLogger);
    // OR
    Clog.addLogger(null, myLogger);
    Clog.log("loggerKey", "Hello world");

### Multiple Environments

Under the hood, the String formatter and all loggers are held within instances of Clog objects, which are then access through a static hashmap of all available instances. This means you can set up a profile with all your desired loggers and parsers, and switch between profiles at will. Below is an example of setting up and using a custom profile, which is based on the Android Clog library's implementation.

    // Create your clog profile with its loggers and chosen String formatter (see below)
    ClogFormatter formatter = new Parseltongue();
    HashMap<String, ClogLogger> loggers = new HashMap<String, ClogLogger>();
    loggers.put(null, new ClogI());
    loggers.put("d", new ClogD());
    loggers.put("e", new ClogE());
    loggers.put("i", new ClogI());
    loggers.put("v", new ClogV());
    loggers.put("w", new ClogW());
    loggers.put("wtf", new ClogWTF());

    Clog dev = new Clog(loggers, formatter);

    Clog.addProfile("dev", dev); //add the profile to clog
    Clog.setCurrentProfile("dev"); //sets the current profile to "dev"
    Clog.setCurrentProfile(dev, "dev"); //Adds the profile to clog and switch to use it

### Formatters

Clog is designed to make logging easier by allowing messages to be formatted with various arguments. It comes with my custom formatting language, Parseltongue, but the formatter implementation is separated out as in interface so you can use any formatter you wish. A custom formatter is any class that implements the <code>ClogFormatter</code> interface, which has the following method:

<table class="table table-condensed">
    <thead>
    <tr>
        <th>Method</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><pre>String format(String message, Object... params)</pre></td>
        <td>Formats the message with the given parameters</td>
    </tr>
    </tbody>
</table>

Custom formatters can be supplied to the profile as shown above, or added directly to the current profile with the <code>setFormatter(ClogFormatter)</code> method.
