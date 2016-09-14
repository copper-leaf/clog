# Clog

A drop-in replacement for android.util.Log. Features an extensible API and a simple yet powerful formatting languge, Parseltongue.

### Download
Clog is distrubuted through JitPack.io.
[![](https://jitpack.io/v/cjbrooks12/Clog.svg)](https://jitpack.io/#cjbrooks12/Clog)

View the [Documentation](https://jitpack.io/com/github/cjbrooks12/Clog/v0.1.0/javadoc/)

In your project-level `build.gradle`:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

In your module's `build.gradle` (replace 'v0.1.0' with the most recent release):
```groovy
dependencies {
    ...
    compile 'com.github.cjbrooks12:Clog:v0.1.0@aar'
}
```


### Overview
Clog is designed to be a drop-in replacement of the basic Android Log class. It provides a more generic interface so that a call like `Clog.i()` does what you want it to do, which isn't strictly logging a message. You might want to view logs in your terminal during development, but send them all to your server during production. That is exactly the problem Clog is designed to solve.

It also attempts to make logging easier overall with more meaningful log messages. That's why Clog can use the caller's simple class name as the tag (thus, you don't need to give a tag to every log), and also contains a powerful String-formatting language, so you can more easily print and format your objects in your logs. But even the Clog formatting language is implemented as a delegate, so if you don't like my string language, you can use your own, such as the standard Java `String.format()`.

### Basic Usage
Since Clog implements the full public interface of Log, you can replace all your `Log`s with `Clog`s.

```Java
Log.i(tag, message)
```
becomes
```Java
Clog.i(tag, message)
```

You can also get rid of those pesky tags, which automatically finds the calling class and uses its simple class name as the tag. So you can then do:

```Java
Clog.i(message)
```

You can also add any number of Objects as varargs to the logger to be formatted into the final log message. See section below for the Clog language spec.

```Java
int a = 0;
String b = "hello world";
YourCustomObject c = new YourCustomObject();

Clog.i(message, a, b, c);
```

By supplying Clog with an instance of any object implementing the `ClogLogger` interface, you can call into your custom logger with:

```Java
Clog.addLogger("yourLogger", new YourCustomLogger());
Clog.logger("yourLogger", message);

```

Don't like having to call into your specific logger every time? Set it as the default and use `Clog.log()` instead of `Clog.i()`:

```Java
Clog.setDefaultLogger(new YourCustomLogger());
Clog.log(message);

```

### Clog Formatting Language (Parseltongue)
Building Strings to be put into logs (or anything else for that matter), is painful. One of the greatest things in many modern languages is string interpolation, which is the idea that I attempt to capture with this formatting language. It's best shown with a basic example of the language's syntax:

```Java
String bob = "Bob";
String larry = "Larry";
Clog.i("Hello #{ $1 }, my name is #{ $2 }", bob, larry);
// prints "Hello Bob, my name is Larry"
```

This should feel familiar to anyone who has worked with Ruby or Coffeescript, because that is where I drew inspiration. I also really liked the implementation of AngularJs Filters, but always disliked its syntax, so I improved it with what I call Formatters (since they take an object and format it to be printed as a String):

```Java
String bob = "Bob";
String larry = "Larry";
Clog.i("Hello #{ $1 | lowercase }, my name is #{ $2 | uppercase }", bob, larry);
// prints "Hello bob, my name is LARRY"
```

So let's break this down. A basic 'clog' looks like `#{ ... }`, and like a clog in a pipe, must be removed (hence the library name). The parser will do what it can to print a meaningful result from whatever is in the clog, but if it can't it will simply print nothing.

The first thing in the clog is an indexer, and it must be present. A dollarsign followed by a number will refer to that Object (1-indexed) in the varargs passed to the formatter, while a atsign followed by a number will refer to the result of a previously formatted clog (1-indexed). Please note that that, to prevent the problem of circular dependencies, the hashsign indexers can only refer to clogs strictly before themselves. The indexers start at 1 because `$0` is reserved to always refer to `null`, because it might make sense to create a formatter that needs no input and returns an object, such as getting the current Date.

```Java
"#{ $1 } and #{ @1 } are both valid indexers"
BUT
"#{ @1 } and #{ $1 } is not, because @1 is already the first clog"

```

After the indexer is an optional list of formatters, which are separated by pipes `|`, and like a standard Unix pipe, the output of the first is fed as the input to the second, and so on, like so:

```Java
"#{ $1 | formatter1 | formatter2 | formatter3 }"
```

Formatters are not required to turn the input into a printable output String, so you can make logical pipelines transforming data until the end, at which point the resulting object will always be printed as `result.toString()`.

You can also pass additional arguments formatters in the standard function-parameter syntax. You can pass literal numbers, literal strings (text surrounded by single-quotes), literal booleans, or object indexers.

So a full call to our logger using the Clog Formatting Language might look like the snippet below. The formatters used are standard formatters and included by default:

```Java
ArrayList<String> names = new ArrayList<>();
names.add("Bob");
names.add("Larry");
names.add("Junior");
names.add("French Peas");

Clog.i("#{ $1 | repeat(3) | uppercase }", names.get(0));
// becomes "BOBBOBBOB";

Clog.i("#{ $1 | join('; ') | lowercase }", names)
// becomes "bob; larry; junior; french peas"

Clog.i("#{ $1 | join('; ') | lowercase } references back to #{ @1 | uppercase }", names);
// becomes "bob; larry; junior; french peas references back to BOB; LARRY; JUNIOR; FRENCH PEAS"

```









