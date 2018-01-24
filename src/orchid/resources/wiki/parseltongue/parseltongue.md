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

