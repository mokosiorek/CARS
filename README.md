PROJECT CARS

This project was created with IntelliJ IDEA IDE. And can be run via this IDE.

To run the project manually with maven and java compiler follow these steps.

Steps:
1. Build the project using "mvn install" command - this should generate seperate jars from modules as result.
2. To combine them use command: "mvn clean compile assembly:single" in terminal in directory of pom.xml from menu module.
3. In target directory there should be a jar file generated which combines all modules' jars.
4. To run the generated jar use command: "java -cp <jar_file_name> kosiorek.michal.menu.App". The file testcars.json must be in the same directory as the generated jar.


