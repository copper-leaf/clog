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