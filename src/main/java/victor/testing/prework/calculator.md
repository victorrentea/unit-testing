# TDD Exercise
## Purpose
Practice writing unit tests

## Description

**This is a individual assignment.**

Test each individual requirement on the existing code.

### Kata Steps
- The Add method can take 0, 1, or 2 numbers and will return their sum.
- An empty string will return 0.
- Example inputs: `“”, “1”, or “1,2”`
- Start with the simplest test case of an empty string. Then 1 number. Then 2 numbers.
- The Add method can handle an unknown number of arguments/numbers.
- The Add method can handle new lines between numbers (instead of commas).
    1. Example: `“1\n2,3”` should return 6.
    2. Example: `“1,\n”` is invalid, but you don’t need a test for this case.
    3. Only test correct inputs – there is no need to deal with invalid inputs for this kata.
- Calling Add with a negative number will throw an exception “Negatives not allowed: “ listing all negative numbers that were in the list of numbers.
    1. Example `“-1,2”` throws “Negatives not allowed: -1”
    2. Example `“2,-4,3,-5”` throws “Negatives not allowed: -4,-5”
- Numbers bigger than 1000 should be ignored.
    * Example: `“1001,2”` returns 2
- The Add method can handle a different delimiter:
    1. To change the delimiter, the beginning of the string will contain a separate line that looks like this: `“//[delimiter]\n[numbers]”`
    2. Example: `“//;\n1;2”` should return 3 (the delimiter is ;)
    3. This first line is optional; all existing scenarios (using , or \n) should work as before.

