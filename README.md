# Fish Street Cemetery Project

Group5: Kairui & Neil

## Files
- `/fishStreet/Tombstone.java` -  burial record (name, date, age, location); Comparable
- `/fishStreet/Cemetery.java` - reads/stores file in a `TreeSet`; dates
- `/fishStreet/FishStreet.java` - main.

## How to run

    javac group5/fishStreet/*.java
    java group5.fishStreet.FishStreet

You could parse args and use other txt:

    java group5.fishStreet.FishStreet path/to/cemetery.txt

## Using the program
Pick a menu option, then follow the prompts. Dates are `mm/dd/yyyy` (e.g. `01/05/1813`).

1. Burials on one date.
2. Burials in a date range at a location (matches any location containing the text, case not sensitive,
   example: `Lambeth Hill` will count as `Crane Court Lambeth Hill`). Prints the count and average age.
3. Same function as #2 except the location function
4. Total number of burials.
5. All burials in ascending date order.
6. Quit.

## Notes
- Ages: `39` = 39 years, `11.5` = 11 years 5 months, `15w` = 15 weeks, `22d` = 22 days.
- Average age uses 30-day months and 360-day years, no leap years
- Duplicates (same name, age, date and location) are removed
- Output : name, date, age, location.

## References

Bing Search AI was used for some of the researches
 
- Compiling and running classes in a package from the command line:
  - https://gurubase.io/g/java/executing-java-program-packages-command-line
  - https://www.webucator.com/article/how-to-compile-packages-in-java/
  - https://howtodoinjava.com/java-programs/could-not-find-or-load-main-class/
- Packages and using classes from other files:
  - https://labuladong.online/en/programming-language/java/packages/
- TreeSet and avoiding duplicate custom objects:
  - https://www.geeksforgeeks.org/?p=527592
  - https://callicoder.com/java-treeset
- compareTo consistent with equals:
  - https://blog.joda.org/2012/11/pitfalls-of-consistent-with-equals.html
  - https://rules.sonarsource.com/java/type/Code%20Smell/RSPEC-1210
- Reading a file line by line (BufferedReader, try-with-resources):
  - https://mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
  - https://stackabuse.com/reading-a-file-line-by-line-in-java/
- Regex (Pattern, Matcher, capture groups):
  - https://dev.java/learn/regex/groups/
  - https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/questions/4589643/identifying-capture-groups-in-a-regex-pattern
- Dates: strict parsing instead of lenient parsing:
  - https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/questions/6028823/simpledateformat-giving-wrong-date-instead-of-error
- String.format column padding:
  - https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/a/391978
  - https://mkyong.com/java/java-string-format-examples/
- Scanner input and console menus:
  - https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/questions/5032356/using-scanner-nextline
  - https://medium.com/@AlexanderObregon/creating-user-menus-in-java-with-loops-and-switch-040149bd9732
