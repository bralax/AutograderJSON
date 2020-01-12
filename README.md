# Json to java main converter for this [Autograder](https://github.com/bralax/gradescope_autograder)


# Json File Format

At the top most level, there should be a parameter named "tests". This should an array type of objects so in this format:

```json
{
   "tests": [{...}, ... {...}]
}
```
   
Each object in the array should have the following parameters:
   - ### type: The type of test to run. See below for the mapping between the Java test and the type name. For more information about the test, see the autograder library.
   - **parameters**: This is an object containing each parameter that a test needs and the values for them.
     - *classname*
     - *count*
     - *input*
     - *first*
     - *second*
     - *classtype*
     - *field*
     - *method*
     - *arguments*
     - *logname*
     - *samplename*
     - *inputname*
     - *viscount*
     - *sample*
     - *pointsper*
   - **score**: This is how many points the test is worth
   - **visibility**: A string that represents the visibility of the test to students. See below for the acceptable values.
   - **conditiontests (optional)**: This should be an array of the same test objects. These are tests that should be run only if this test returns true. Only the tests listed below with a yes in the condition tests column can handle this parameter. If you try to provide this parameter to a test that can not handle it, all the conditional tests will be ignored.


## Visibility options

Gradescope allows for four basic visibility options for a test. All tests will allways be available for course staff but when visibility determines when do students get to see the result. The options are:
   - visible
   - hidden
   - after_due_date
   - after_published 

## Converting tests to names
| Java Method                 | JSON Type Name    | Condition Tests? | Potential Parameters                                          |
|-----------------------------|-------------------| ---------------- | ------------------------------------------------------------- |
| classDoesNotUseArrayLists   | noarraylist       |   Yes            |   classname                                                   |
| classDoesNotUsePackages     | nopackage         |   Yes            |   classname                                                   |
| comparisonTests             | comptests         |   No             |   classname, count                                            |
| comparisonTest              | comptest          |   No             |   classname, input                                            |
| diffFiles                   | compfiles         |   No             |   first, second                                               |
| hasFieldTest                | hasfield          |   Yes            |   classname, classtype, field                                 |  
| hasMethodTest               | hasmethod         |   No             |   classname, method, arguments                                |
| junitTests                  | junittests        |   No             |   classname                                                   |
| logFileDiffTest             | logdifftest       |   No             |   classname, logname, samplename, inputname                   |
| logFileDiffTests            | logdifftests      |   No             |   classname, logname, samplename, count, viscount(optional)   |
| stdOutDiffTest              | stddifftests      |   No             |   classname, count, sample                                    |
| stdOutDiffTests             | stddifftests      |   No             |   classname, count, sample, viscount (optional)               |
| testCheckstyle              | checkstylepf      |   No             |   classname                                                   |
| testCompiles                | compiles          |   Yes            |   classname                                                   |
| testConstructorCount        | constructcount    |   Yes            |   classname, count                                            |
| testMethodCount             | methodcount       |   Yes            |   classname, count                                            |
| testPublicInstanceVariables | nopiv             |   No             |   classname                                                   |
| testSourceExists            | fileexists        |   Yes            |   classname                                                   |
| testSortedCheckstyle        | checkstylepererr  |   No             |   classname, pointsper                                        |
| testSortedCheckstyle        | checkstylepertype |   No             |   classname, pointsper                                        |
