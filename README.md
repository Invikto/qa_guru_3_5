# Task

1. Write a test to create an issue by GUI.
2. Check:
- the issue name;
- whom assignee;
- the issue tag.
3. Should be three versions:
- clean Selenide;
- lambda steps;
- the annotation `@Step`.

# Solution

## Preconditions

You should install Java to check the solution.
Also, you should add your GitHub credentials in the file `src/test/resources/credentials.properties`.

## Launch
1. Go to the project directory.
```shell
cd <project_dir>
```
2. Launch tests.
```shell
./gradlew clean test
```
3. See the results in Allure.
```shell
./gradlew downloadAllure AllureServe
```
