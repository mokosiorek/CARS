PROJECT CARS

This project was created with IntelliJ IDEA IDE.

WARNING:
Due to an existing error with gson and java modules to run this project it is necessary to build project manually with maven and then use a console to compile and run the project.
Steps:
1. Build the project using "mvn install" command - this should generate seperate jars from modules as result.
2. To combine them use command: "mvn clean compile assembly:single" in terminal in directory of pom.xml from menu module.
3. In target directory there should be a jar file generated which combines all modules' jars.
4. To run the generated jar use command: "java -cp <jar_file_name> kosiorek.michal.menu.App". The file testcars.json must be in the same directory as the generated jar.


