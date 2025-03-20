# JTidy
[![Build Status](https://img.shields.io/travis/jtidy/jtidy/master.svg?colorA=9977bb&style=plastic)](https://travis-ci.org/jtidy/jtidy)
[![Build Status](https://img.shields.io/shippable/5a39c2859b0aca0700da9a9c/master.svg?colorA=9977bb&style=plastic)](https://app.shippable.com/projects/5a39c2859b0aca0700da9a9c/)
[![Code Quality](https://sonarcloud.io/api/project_badges/measure?project=jtidy%3Ajtidy&metric=alert_status)](https://sonarcloud.io/dashboard?id=jtidy%3Ajtidy)

# About
Revival of the JTidy Project updated to work with HTML5 new tags. Along 
with new option to not remove unknown tags:

```java
Tidy tidy = new Tidy();
tidy.setDropProprietaryTags(false);
```

JTidy is a Java port of [HTML Tidy](http://www.w3.org/People/Raggett/tidy/), a HTML syntax checker and pretty printer.
Like its non-Java cousin, JTidy can be used as a tool for cleaning up malformed and faulty HTML.
In addition, JTidy provides a DOM interface to the document that is being processed, which effectively makes you able
to use JTidy as a DOM parser for real-world HTML.

# HOW TO
You can use JTidy as an html checker/pretty-printer or as a DOM parser.

First of all, you will need to download a JTidy distribution. Inside it you will find a `jtidy.jar` containing all 
classes, no other libraries are needed.

Now that you have JTidy you can use it in different ways.

## JTidy Executable
Run `java -jar jtidy.jar {options}` to access the JTidy command line interface.

`java -jar jtidy.jar -h` will output a short help on JTidy command line with a few examples.

`java -jar jtidy.jar -help-config` outputs all the available configuration options and 
`java -jar jtidy.jar -show-config` the current (default) values.

## Ant Task
Detailed instructions on how to use the JTidy ant task can be found in `JTidyTask` JavaDoc.

## JTidy API

To use JTidy embedded in your program, you best set up a Maven dependency to the official release at maven-central:

```
<dependency>
    <groupId>com.github.jtidy</groupId>
    <artifactId>jtidy</artifactId>
    <version>1.0.5</version>
</dependency>
```

If you require a Java-6-compatible version, you can use the back-ported artifact:

```
<dependency>
    <groupId>com.github.jtidy</groupId>
    <artifactId>jtidy-java6</artifactId>
    <version>1.0.4</version>
</dependency>
```

The entry point for accessing JTidy functionalities is the `org.w3c.tidy.Tidy` class. This is a simple usage example:

```
Tidy tidy = new Tidy(); // obtain a new Tidy instance
tidy.setXHTML(boolean xhtml); // set desired config options using tidy setters 
...                           // (equivalent to command line options)

tidy.parse(inputStream, System.out); // run tidy, providing an input and output stream
```
                
Using `parseDOM(java.io.InputStream in, java.io.OutputStream out)` instead of `parse()` you will also obtain a DOM 
document you can parse and print out later using `pprint(org.w3c.dom.Document doc, java.io.OutputStream out)` 
(note that the JTidy DOM implementation is not fully-featured, and many DOM methods are not supported).

JTidy also provides a `MessageListener` interface you can implement to be notified about warnings and errors in your 
HTML code. For details on advanced uses refer to the JTidy JavaDoc.

# History
JTidy was initially written by Andy Quick. The project has been maintained at sourceforge.net by Fabrizio Giustina from 
2004 to 2010. Since the [JTidy project on SourceForge.net](https://sourceforge.net/projects/jtidy/)
seemed to fall into disrepair years ago and had not been updated for years. 
A few had forked it on Github.
[William L. Thomson Jr.](https://github.com/wltjr) came along and 
created a fork of others forks with a tag for his packaging needs as a 
dependency for JMeter. Then another came along,
[Dell Green](https://github.com/dellgreen) who noticed some issues, 
tests failing, and undertook fixing both.

Since the code belonged to neither, William decided to create a JTidy 
organization and revive the project via community support. Which you 
are welcome to join in. Eventually this should become the official new 
home for JTidy.

Thanks to all past authors and developers. Those of which who could be 
found on Github have been invited to join this project. Along with those 
that this repository was forked from.

# Contributing

You are welcome to contribute issues and pull-request. Please have a look at the 
[coding conventions](docs/CodingConventions.md) and [test cases](docs/TestCases.md).

# License

This project is licensed under the "Java HTML Tidy License" which is compatible with the [zlib/libpng license](https://en.wikipedia.org/wiki/Zlib_License). More information is available in the [LICENSE.txt](https://github.com/jtidy/jtidy/edit/master/LICENSE.txt) file

# Future

The project is looking for new contributors and project maintainers. 

Checkout [v.Nu](https://github.com/validator) validator for a possible modern replacement.
