
# Overview

> Clog is a simple-to-use yet powerful and customizable logging utility for Java. It is based on the Android Log class interface, but improves it with features like custom loggers with multiple profiles and powerful String formatting with the Parseltongue formatting language. You'll wonder how you ever got by without it.

Clog is designed to replace and extend the functionality of the basic Android Log class, but is built on pure Java so it can be used anywhere. It provides a generic interface with loggers provided at runtime, so that you can do things like log messages to your console during development, but write them to a file or send to a server in production.

It also attempts to make writing log messages easier overall and more meaningful. By default, each logging call will find the caller's class name and use that as a tag to the message, which helps you in log filtering. Clog also ships with a powerful new String formatting language, Parseltongue, which not only makes it simple to print your objects in strings without manual concatenation, but provides a full data-manupulation pipeline for each object passed in, so that it can be formatted and displayed exactly as you want with minimal effort.

### Features

* Replicated the Android Log API for use in any Java project
* Log to different places in development and production with multiple profiles
* Make your logs more meaningful and easier to write with the Parseltongue formatting language
* Format your objects in Parseltongue using the many built-in spells, or create your own!
* Fully customizable! Replace the loggers, spells, and even String formatter with your own implementations

# Get It

### Include In your Project

Clog can be added to your project from Jitpack through Gradle.

Add this to your project-level build.gradle:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

Add this to your module's dependencies:

    dependencies {
        ...
        compile 'com.github.cjbrooks12:Clog:{{options.v}}'
    }

Alternatively, use the preconfigured Clog profiles for Android or Log4j:

    dependencies {
        ...
        compile('com.github.cjbrooks12:Android-Clog:{{options.v}}@aar') {
            transitive = true;
        }
    }

    dependencies {
        ...
        compile('com.github.cjbrooks12:Clog4j:{{options.v}}') {
            transitive = true;
        }
    }

### Github

Full source can be viewed on Github. Feel free to send me a PR.

Download the most recent commits, or see instructions for installing with Maven, at Jitpack. [![](https://jitpack.io/v/JavaEden/Clog.svg)](https://jitpack.io/#JavaEden/Clog)

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

# Parseltongue

### Basic Syntax

Building Strings to be put into logs (or anything else for that matter), is painful. One of the greatest things in many modern languages is string interpolation, which is the idea that I attempt to capture with this formatting language. It's best shown with a basic example of the language's syntax:

    Clog.log("Hello, my name is #{ $1 } #{ $2 }", "Harry", "Potter");
    // prints "Hello my name is Harry Potter"

This should feel familiar to anyone who has worked with Ruby or Coffeescript, because that is where I drew inspiration. Anywhere with the message you want to print a variable, add <code>#{ $i }</code>, where <code>i</code> is the index of that object pass in as varargs, starting from 1. Any object passed in gets printed by calling irs <code>toString()</code>.

I also really liked the implementation of AngularJs Filters but always disliked its syntax, so I improved it with what I call Spells, which are little more than Java methods which can be used from within the Parseltongue language. Clog comes preconfigured with the spells listed below, and you can add your own, too. Casting a basic spell looks like this:

    Clog.log("Hello, my name is #{ $1 | uppercase } #{ $2 | lowercase }", "Harry", "Potter");
    // prints "Hello my name is HARRY potter"

Spells can also be cast in sequence, like so:

    Clog.log("Hello, my name is #{ $1 | uppercase | count }", "Harry Potter");
    // prints "Hello my name is 12"

Spells can accept an arbitrary number of parameters, which follows the standard function-calling syntax:

    Clog.log("Hello, my name is #{ $1 | spellWithManyParameters($1, 42, true, 'string') }", "Harry Potter");

The basic clog syntax looks like <code>#{ initialReagent ( | spell )+ }</code>. Spells are what I just described above, and reagents are parameters passed to spells and eventually printed out. The types of reagents are listed below.


<table class="table table-condensed">
    <thead>
    <tr>
        <th>Reagent Type</th>
        <th>Example</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Parameter</td>
        <td><code>$1</code></td>
        <td>Get one of the parameters passed in to the format method. Indices start at 1. If the index is out of bounds, the object returned will be <code>null</code></td>
    </tr>
    <tr>
        <td>Result</td>
        <td><code>@1</code></td>
        <td>Get the object that was the result of a previous clog, before it was turned to a String. Indices start at 1. If the index is out of bounds, the object returned will be <code>null</code></td>
    </tr>
    <tr>
        <td>Literal Integer</td>
        <td><code>1</code>, <code>-1</code></td>
        <td>A signed literal integer</td>
    </tr>
    <tr>
        <td>Literal Double</td>
        <td><code>1.5</code>, <code>-1.5</code></td>
        <td>A signed literal double-precision floating point number</td>
    </tr>
    <tr>
        <td>Literal Boolean</td>
        <td><code>true</code>, <code>false</code></td>
        <td>A literal boolean</td>
    </tr>
    <tr>
        <td>Literal String</td>
        <td><code>'Hello world'</code></td>
        <td>A literal string, surrounded by single-quotes</td>
    </tr>
    <tr>
        <td>Null</td>
        <td><code>null</code>, <code>$0</code></td>
        <td>Null. $0 will always be out of bounds, and can be considered shorthand for null</td>
    </tr>
    </tbody>
</table>

### Advanced syntax

In addition to injecting objects and formatting those objects for output within the clog, Parseltongue provides support for advanced property finding, via indexers. Parselmouth supports three basic types of indexers, array indexers, map indexers, and property indexers, outlined below. These indexers can be applied to any <code>param</code> reagent, <code>result</code> reagent, or spell result. One caveat is that when applying an indexer to a spell result, if the spell expects no additional parameters, you must add empty parentheses to the spell. Examples will follow with the indexer outlines below:

<br><b>Array Indexers</b><br>
Parselmouth allows basic arrays and objects implementing the <code>List</code> interface easy access to individual members of the array-type-structure. The syntax follows the familiar Java array-indexing syntax, and indexing starts at 0, as expected.

    List<String> namesList = new ArrayList<String>();
    namesList.add("Harry");
    namesList.add("Ron");
    namesList.add("Hermione");
    namesList.add("Fred and George");

    String[] namesArray = new String[] {
    "Harry",
    "Ron",
    "Hermione",
    "Fred and George"
    };

    // note that when passing an array to be a parameter, you must pass at least one other object after it so that it does not get expanded into its component objects as varargs
    Clog.log("#{ $2 } and #{ $1[1] }", namesArray, "Harry");
    // prints Harry and Ron

    Clog.log("#{ $2 | lowercase } and #{ $1 | returnListDirectly()[2] | uppercase }", namesList, "Harry");
    // prints harry and HERMIONE

<br><b>Map Indexers</b><br>
Parselmouth provides simple access to map-type objects where the key is a String. This is any object implementing the <code>Map</code> interface (where implicitly, the first type-parameter of that map is of type <code>String</code>), or alternatively, any object that contains a method with the following signature: <code>get(String)</code>. In the case that the object is a <code>Map</code>, Parseltongue will cast it to a map and call its' get method, while it will use reflection to try and find a <code>get(String)</code> method on any other object type. This makes it more efficient to access <code>Map</code> objects, but allows the possibilty of accessing properties of other common objects with a string-lookup <code>get</code> method, such as <code>org.json.JSONObject</code>. Map indexers are called by passing a string literal as the key within brackets:

    Map<String, String> namesMap = new HashMap<String, String>();
    namesMap.put("harry", "Harry");
    namesMap.put("ron", "Ron");
    namesMap.put("hermione", "Hermione");
    namesMap.put("fred and george", "Fred and George");

    Clog.log("#{ $1['ron'] }, #{ $1['fred and george'] } are brothers", namesMap);
    // prints Ron, Fred and George are brothers

    Clog.log("#{ $1['ron'] | lowercase }, #{ $1 | returnMapDirectly['fred and george'] | uppercase} are brothers", namesMap);
    // prints ron, FRED AND GEORGE are brothers

<br><b>Property Indexers</b><br>
Parselmouth can also use reflection to find properties of objects by name. By passing a single word, unquoted, within brackets as an indexer, Parseltongue will attempt to find and access a field matching that name within the specified object, returning the object at that field if it exists. By default, Parseltongue will only search for public members, which can be anywhere in the objects class hierarchy. Private members can only be accessed only if they are declared in the class of the object passed to the indexer and you set private fields to be accessible by calling <code>parseltongueInstance.setPrivateFieldsAccessible(true);</code>.

    public static class ClassWithProperties {
    private String privateHarry = "Harry (Private)";
    public String publicHarry = "Harry (Public)";
    }

    Clog.log("#{ $1[publicHarry] }", new ClassWithProperties());
    // prints "Harry (Public)", because that field is public

    Clog.log("#{ $1[privateHarry] }", new ClassWithProperties());
    // prints nothing, because 'privateHarry' is a private field and cannot be accessed

    Parseltongue pt = new Parseltongue();
    pt.setPrivateFieldsAccessible(true);
    Clog.setFormatter(pt);
    Clog.log("#{ $1[privateHarry] }", new ClassWithProperties());
    // prints "Harry (Private)", because private fields have been made accessible

### Syntax Error Recovery

In the case there is a syntax error, Parseltongue will try to recover from the error and continue on. When there is a syntax error, that clog will be removed and nothing will be output, and a <code>null</code> will be added to the results list so results indexing is not affected; all subsequent clogs in the format string will not be affected from previous errors.

# The Spellbook

### Spells

Spells are special methods intended to modify an object so that it can be printed more meaningfully. Spells are defined to be static and accept at least one parameter, but may accept more, and must return an object of any type. The first parameter will always be the initial reagent or the result from the previous spell in the pipeline, and any subsequent parameters must be defined from within the format string.

### Adding Spells

Spells can be added by defining them with the annotation <code>@Spell</code> and telling Parseltongue to find all spells in that class. By default, the method name will be how to call that spell in the formatter, but you can supply the annotation with a 'name' parameter with the desired name. Multiple spells can have the same name, and they will be overloaded in a similar way to normal Java method overloading. An example is shown below:

    public class SpellClass {

    @Spell
    public String getClassName(Object object) {
    return object.getClass().getSimpleName();
    }

    @Spell(name="fullClassName")
    public String getFullClassName(Object object) {
    return object.getClass().getSimpleName();
    }
    }

    Parseltongue pt = new Parseltongue();
    pt.findSpells(SpellClass.class);

To add spells to the Clog formatter, you must setup your Parseltongue with spells and then set it as the current Clog profile's formatter.

    Parseltongue pt = new Parseltongue();
    pt.findSpells(SpellClass.class);

    Clog.setFormatter(pt);

### Included Spells

Parseltongue comes with the numerous formatting functions in The Standard Book Of Spells. View the docs for the
TheStandardBookOfSpells class.

