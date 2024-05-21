# SoPra Risk Game FS24

With this project we wanted to make it possible for people to play the board game Risk with each other even if they can't meet in person. We all like strategy games but luck also having a deciding factor makes the game more random and fun which is why we chose to implement risk. In the end we want to have a functioning version of risk which is simple to play and can be extended on. We also want the ui to be simple but understandable.

## Getting started with Spring Boot
-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup this Project with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
If you consider to use IntelliJ as your IDE of choice, you can make use of your free educational license [here](https://www.jetbrains.com/community/education/#students).
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs24` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
Have a look here: https://www.baeldung.com/spring-boot-testing

## Technologies
We used two main technologies in the server:
-   Spring Boot
-   Gradle

## High-level Components
We have three main Components in our project:
-   [User] [user]: It saves the data after registration and is the personifaction of the user
-   [Lobby] [lobby]: It is created before entering the game so many different users can join one Lobby and then start the game
-   [Game] [game]: It is where the game Risk takes place. All territories, players, cards, etc. are saved in it.
They each are interlinked. The userIds of users who joined a lobby are saved in said lobby. The id of a game is saved in its respective lobby. 


## Contributing
Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests to us.

## Roadmap
Features which still need to be implemented:
-   At the moment it is not possible for the user to change their avatars after they have chosen one at registration. You could implement one in the userService and userController.
-   At the moment there is only one map to play on. The board game Risk has more. You could implement them in server.
-   Refactoring. Lots of things are hard coded in the gameServer file which is why it is our biggest file. You could try to refactor it into smaller packages or even into a different file.

## Authors and Acknowledgement
-   SylverSezari - Founder
-   roburkUZH - Founder
-   Cross55 - Founder
-   Eposs111 - Founder
-   RPKosch - Founder

-   We thank mirovv, royru, marion-an, isicu, clennys for sharing their template which we used

## License
This project is licensed under the GNU License - see the LICENSE.md file for details

[user]: https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/User.java

[lobby]:https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Lobby.java

[game]: https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Game.java
