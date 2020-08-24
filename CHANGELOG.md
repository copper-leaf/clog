## 3.5.0 - 2020-08-21

- Adds exception tracking

## 3.4.2 - 2020-08-21

- Fix regex patterns causing crash on Android

## 3.4.1 - 2020-08-21

- Rename groupId to fix Bintray publishing 

## 3.3.0 - 2020-08-21

- Adds helper methods to disable logging in production, and to configure multiple logging targets

## 3.2.0 - 2020-08-20

- Enables SLF4J support on Android platform
- Publishes artifacts to Bintray

## 3.1.1 - 2020-07-08

- Fixes issue of `v`, `d`, and `i` logs not working on JS
 
## 3.1.0 - 2020-07-06

- Adds support for simple slf4j MDC binding

## 3.0.0 - 2020-06-10

- Complete rewrite in Kotlin Multiplatform. Currently supported plain JVM, Android, iOS, and JS platforms.
- Parseltongue and rich message formatting has been removed in favor of only simple `{}` formatting tags.
- Public API has been cleaned up and streamlined for the 80% use-case.
    - Log methods no longer have a dedicated `Throwable` parameter overload. Just add the throwable messages to the 
        message formatting.
    - Removes multi-profile support. Instead, the need for multiple profiles can be accomplished simply be creating a 
        new profile at that location.
    - Removes the tag stack, and just supports a single tag. Manage tag stacks yourself or use dedicated Profiles at the
        needed location.

## 2.0.7 - 2019-02-02

- Fixes issue of literal # crashing parser. Closes #1

## 2.0.6 - 2018-12-29

- Fixes issue of noTag() not working

## 2.0.5 - 2018-12-19

- Fixes broken builds

## 2.0.4 - 2018-08-29

- Don't print empty tags in DefaultLogger messages

## 2.0.3 - 2018-08-23

- Allows both static and non-static methods to be called from Clog Incantations

## 2.0.2 - 2018-05-12

- Lazily instantiates Clog profile to avoid issues with Jansi on Android

## 2.0.1 - 2018-14-06

- Allows Clog spells to be set from any interface, not just static methods

## 2.0.0 - 2018-03-27

- Brings Android Clog into main repo

## 1.6.3 - 2018-01-23

- Updates documentation, fixes bug with SLF4J style formatting when mixed with Parseltongue

## 1.6.2 - 2018-01-23

- Sets bintray repo to Eden

## 1.6.1 - 2017-08-16

- Bump to v1.6.1

## 1.6.0 - 2017-08-16

- Improves priority handling by using Enum constants

## 1.5.0 - 2017-07-21

- Adds SLF4J-style string interpolation to Parseltongue, removes tags from logging statements because they suck and 
    prevent us from using vararg params as I'd like to in most cases.

## 1.3.0 - 2017-01-02

- Bump version 1.3.0

## 1.2.4 - 2016-12-22

- Bump patch version

## 1.2.3 - 2016-12-21

- Bump version and enable travis-ci

## 1.2.2 - 2016-12-21

- Hacks in IntelliJ support for Jansi

## 1.2.1 - 2016-12-21

- Minor tweak to Default Logger

## 1.2.0 - 2016-12-21

- Adds ANSI colors

## 1.1.0 - 2016-12-21

- Adds filtering capabilities

## 1.0.0 - 2016-09-26

- Initial public release. Both the Clog logger and Parseltongue formatting language are feature-complete and fully 
    tested and ready for initial public release. More Spells will be added soon.

## 0.3.0 - 2016-09-26

- Parseltongue is feature-complete and fully tested. Clog is still lacking some functionality, which prevents this from 
    being the first public release.

## 0.2.0 - 2016-09-22

- Parseltongue language is nearly complete. There are still some minor features to add, and I will be removing the 
    Android dependency,making this a pure Java library. Separate libraries with Clog implementations for Android and 
    Log4j will be coming soon.

## 0.1.0 - 2016-09-10

- This library is only slightly tested and not thread-safe. Basic functionality works, Parseltongue language spec is 
    mostly defined, but parser still has some issues related to parsing literal strings as formatter params.
