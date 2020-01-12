# Json to java main converter for this [Autograder](https://github.com/bralax/gradescope_autograder)


# Json File Format

At the top most level, there should be a parameter named "tests". This should an array type of objects so in this format:

```json
{
   "tests": [{...}, ... {...}]
}
```
   
Each object in the array should have the following parameters:
   - **type**:
   - **parameters**:
     - **classname**
   - **score**:
   - **visibility**:
   - **conditiontests (optional)**: 

classDoesNotUseArrayLists - noarraylist*
classDoesNotUsePackages - nopackage*
comparisonTests - comptests
diffFiles - compfiles
hasFieldTest - hasfield*
hasMethodTest - hasmethod
junitTests - junittests
logFileDiffTest - logdifftest
logFileDiffTests - logdifftests
stdOutDiffTest - stddifftests
stdOutDiffTests - stddifftests
testCheckstyle - checkstylepf
testCompiles - compiles*
testConstructorCount - constructcount*
testMethodCount - methodcount*
testPublicInstanceVariables - nopiv
testSourceExists - fileexists*
testSortedCheckstyle - checkstylepererr  checkstylepertype
