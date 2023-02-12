# Testing JTidy

JTidy provides some helper classes to test its functionalities. Having a wide test coverage will help JTidy developers 
to assure code quality along releases. If you want to write a test case for JTidy (please do that if you are a developer 
and you find a JTidy bug) you can follow few simple guidelines.

See (JTidyBugsTest.java](src/test/java/org/w3c/tidy/JTidyBugsTest.java) for an example.

## Writing test cases

### Basics
To write a JTidy test case your test class must extends `org.w3c.tidy.TidyTestCase` (you can find in the `src/test/java`) 
directory. This class offers different utility methods to load files and evaluate results.

The input file for testing should be placed in the `src/test/resources` directory. An optional configuration file can 
also be supplied: The file name must be the same name of the input file with the `.cfg` extension.

### Test For Crashs or Loops
For testing an input file which causes a NPE or an infinite loop, simply calls `executeTidyTest(String fileName)` where 
filename is the simple (without path) file name for your input file. This method will take care of loading the 
configuration (if a `.cfg` file with the same name of the input file exists) and call `tidy.parse()`.

### Testing Output
Place a file with the same name of your input file but with the ".out" extension containing the expected result. 
Call `executeTidyTest(String fileName)` and the result will be automatically compared with the supplied file. 
An `AssertionException` will be thrown if files are different (the exception will include the different lines from 
both files).

### Testing Parser
Call `parseDomTest(String fileName)`: this will return a `Document` object so you can make assertions on its content.

### Testing Errors/Warnings
Call `executeTidyTest(String fileName)` and provide a file with `.msg` extension besides your input file containing 
the expected errors and messages in XML format. If no message file exists, the first test execution creates a template 
for you.
