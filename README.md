# Json to java main converter for this [Autograder](https://github.com/bralax/gradescope_autograder)


# Json File Format

At the top most level, there should be a parameter named "tests". This should an array type of objects so in this format:

```json
{
   "tests": [{...}, ... {...}]
}
```
   
Each object in the array should have the following parameters:
   - ### type:
   - **parameters**:
     - *classname*
   - **score**: This is how many points the test is worth
   - **visibility**:
   - **conditiontests (optional)**: This should be an array of the same test objects. These are tests that should be run only if this test returns true. Only the tests listed below with a yes in the condition tests column can handle this parameter. If you try to provide this parameter to a test that can not handle it, all the conditional tests will be ignored.



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
