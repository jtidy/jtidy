# Coding conventions

## Forewords
This document describes a list of coding conventions that are required for code submissions to the project. By default, 
the coding conventions for most Open Source Projects should follow the existing coding conventions in the code that you
are working on. For example, if the bracket is on the line after the if statement, then you should write all your code 
to have that convention.

Below is a list of coding conventions that are specific to this project. Anything else not specifically mentioned here 
should follow the official Sun Java Coding Conventions .

## Eclipse settings
If you use Eclipse you can download and import [the code formatter preference file](eclipse_formatter.xml). Import it from 
`Window-> Preferences -> Java -> Code Style -> Code Formatter -> import`.

## Project specific coding conventions

### Brackets
All brackets (class, method, if, try, etc) must begin and end on a new line. Example :

```                    
public class SomeClass
{
    public void someMethod()
    {
        if (xxx)
        {
        }
    }
}
```
                
Brackets are mandatory, even for single line statements !

```
// Incorrect
if (expression)
    // some code

// Correct
if (expression)
{
    // some code
}
```
                
### Blank Spaces
keywords followed by a parenthesis should be separated by a space. Example :

```
while (true)
{
    // some code
}
```
                
Blank space should appear after commas in argument lists. Binary operators should be separated from their operands by spaces :

```
a += c + d;
a = (a + b) / (c * d);

while (d++ = s++)
{
    n++;
}

printSize("size is " + foo + "\n");
```
                
### Indentations
4 spaces or tabs.

### Comments
JavaDoc SHOULD exist on all your class members (methods + class variables), including the private ones. Also, if you 
are working on existing code and there currently isn't a javadoc for that method/class/variable or whatever, then you 
should contribute and add it. This will improve the project as a whole.

Also add code comments when you think it's necessary (like assumptions), especially when the code is not obvious.

### Author References
If you contribute to a file (code or documentation), add yourself to the top of the file (below the existing authors). 
For java files the preferred Javadoc format is:

```                    
@author devnickname
```
                
### Class Variables
Class variables should not have any prefix and must be referenced using the this object. Example :

```
public class SomeClass
{
    private String someString;

    public void someMethod()
    {
        logger.debug("Value = " + this.someString);
    }
}
```

### Parameter Names
Method parameters should not have any prefix. For example :

```
public void someMethod(String className)
{
}
```
                
### Line Length
Avoid lines longer than 120 characters for Code, comments, ...
                
### Qualified Imports
All import statements should containing the full class name of classes to import and should not use the "*" notation :

An example :

```
// Correct
import java.util.Date;
import java.net.HttpURLConnection;

// Not correct
import java.util.*;
import java.net.*;
```
