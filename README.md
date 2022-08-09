# Husky Maps

An educational web app for mapping the world, searching for places, and navigating around Seattle. The app is designed to highlight 3 components: [**Autocomplete**](https://github.com/kevinlin1/huskymaps/wiki/Autocomplete), [**Priority Queues**](https://github.com/kevinlin1/huskymaps/wiki/Priority-Queues), and [**Shortest Paths**](https://github.com/kevinlin1/huskymaps/wiki/Shortest-Paths). Each of these components not only implement features in Husky Maps, but also implement 3 other socially-motivated applications of computing: social genomics, content moderation, and seam carving for content-aware image resizing. Check out the [**Wiki**](https://github.com/kevinlin1/huskymaps/wiki) for instructions about each project component.

The app is designed to support [**Critical Comparative Data Structures and Algorithms**](https://kevinl.info/cs-education-for-the-socially-just-worlds-we-need/), a justice-centered approach to teaching undergraduate data structures and algorithms. The latest courses to use this repository can be found in the [**Releases**](https://github.com/kevinlin1/huskymaps/releases).

## Install IntelliJ IDEA Community Edition

Most of the time when we're writing code in a computer, we're using a specially-designed software called a **code editor**. IntelliJ is the most recommended code editor for Java programmers. It's also the editor that we'll be using in this course.

Download and install the latest version of IntelliJ IDEA Community Edition (2022.2) according to your computer's operating system. The default options for everything should work fine.

* [**Windows**](https://download.jetbrains.com/idea/ideaIC-2022.2.exe)
* [**macOS Intel**](https://download.jetbrains.com/idea/ideaIC-2022.2.dmg) or [**macOS Apple Silicon**](https://download.jetbrains.com/idea/ideaIC-2022.2-aarch64.dmg) ([Mac computers with Apple silicon](https://support.apple.com/en-us/HT211814))
* [**Linux and Chrome OS**](https://download.jetbrains.com/idea/ideaIC-2022.2.tar.gz) ([Linux setup for Chrome OS](https://chromeos.dev/en/linux/setup))

> ✨ Once the download finishes, follow the steps for [Standalone installation](https://www.jetbrains.com/help/idea/installation-guide.html#standalone) according to your computer's operating system.

Once IntelliJ is installed, [run IntelliJ IDEA](https://www.jetbrains.com/help/idea/run-for-the-first-time.html) and select **Skip Remaining and Set Defaults**. You should see the **Welcome to IntelliJ IDEA** screen.

![Welcome to IntelliJ IDEA screen](https://resources.jetbrains.com/help/img/idea/2022.2/ij_welcome_window.png)

Now that you've installed IntelliJ, look toward the bottom of the screen and **Take a quick onboarding tour** by clicking **Start Tour**. This short tour will help familiarize yourself with some of the most frequently-used buttons and features. In this class, we'll mostly focus only on the basics of editing, running, and debugging code that are taught in this short tour—we're here to learn data structures and algorithms, not IntelliJ. You're welcome to search online or ask us about how to do something in IntelliJ. Chances are, if there's something tedious or repetitive, there's a feature in IntelliJ to make the experience less frustrating.

Once you finish the onboarding tour, return to the **Welcome to IntelliJ IDEA** screen.

## Download, extract, and open the project scaffold

Download the [huskymaps-main.zip](https://github.com/kevinlin1/huskymaps/archive/refs/heads/main.zip) with the code and resources for all the projects. Then, extract (unzip) the contents anywhere on your computer. Take a look inside the extracted folder and make sure you have the following folders and files.

| 📂 huskymaps-main |
| ----------------- |
| data              |
| src               |
| LICENSE           |
| README.md         |
| project.iml       |

> ⚠️ Many computers automatically extract the "huskymaps-main" into another folder also called "huskymaps-main" (the name of the zip file). We won't use the outer folder, so move the inner folder wherever you want and then remove the (now empty) outer folder.

You can rename the top-level folder (**huskymaps-main**) however you like.

From the **Welcome to IntelliJ IDEA** screen, click **Open** and select the "huskymaps-main" folder. The first time you open the project, IntelliJ will ask you whether to [trust the project](https://www.jetbrains.com/help/idea/project-security.html). You'll need to trust the project so that you can run Husky Maps later.

After a few seconds, IntelliJ will open a new screen for working on the Husky Maps code. As you learned in the onboarding tour, IntelliJ will optimize things in the background when you start a new project, so it's normal if your computer feels a bit slow at first.

## Finalize the setup and run Husky Maps

[Execute the run configuration](https://resources.jetbrains.com/help/img/idea/2022.2/jt-run-jar.animated.gif) by clicking the green ▶️ Run button by the right side of the navigation bar or using the keyboard combination <kbd>Shift + F10</kbd>. The **run tool window** will appear at the bottom. Ideally, we'd like to see the following output.

```
[main] INFO io.javalin.Javalin -
       __                      __ _            __ __
      / /____ _ _   __ ____ _ / /(_)____      / // /
 __  / // __ `/| | / // __ `// // // __ \    / // /_
/ /_/ // /_/ / | |/ // /_/ // // // / / /   /__  __/
\____/ \__,_/  |___/ \__,_//_//_//_/ /_/      /_/

          https://javalin.io/documentation

[main] INFO org.eclipse.jetty.util.log - Logging initialized @5374ms to org.eclipse.jetty.util.log.Slf4jLog
[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO io.javalin.Javalin - You are running Javalin 4.3.0 (released January 13, 2022).
[main] INFO io.javalin.Javalin - Listening on http://localhost:8080/
[main] INFO io.javalin.Javalin - Javalin started in 306ms \o/
```

> ✅ If you see this in the run tool window, you're done! Your computer happened to already have Java installed and IntelliJ was able to find it automatically. You can visit [localhost:8080](http://localhost:8080) to use Husky Maps, but the map images won't load without following the optional steps at the bottom.

But it's very likely you won't see this because your computer probably doesn't already have the exact version of Java installed. Instead, you might see a "Cannot start compiler" notification with a suggestion to configure the Project SDK. Follow the link in the notification or select **File | Project Structure** from the main menu. In the **Project Structure** window, open the **SDK** dropdown.

![Project SDK dropdown in the Project Structure window](https://resources.jetbrains.com/help/img/idea/2022.2/sdks_project_structure_project.png)

If IntelliJ detected an existing Java SDK, it will be listed under **Detected SDKs**.

* If an SDK version 11 or greater is available, select it.
* If there are no SDKs or the SDKs are below version 11, select **Add SDK | Download JDK** and choose the latest from any vendor. We like _Eclipse Temurin (AdoptOpenJDK HotSpot)_.

Click **OK** and try running Husky Maps again. At this point, Husky Maps should run and print the expected output in the run tool window.

> Optionally, if you want to see the map images in Husky Maps, [sign up for a free MapBox account](https://account.mapbox.com/auth/signup/?route-to=%22https://account.mapbox.com/access-tokens/%22) to get an access token. MapBox is a company that offers maps and location for developers. Access tokens are the way that they track and bill developers for their map usage, but MapBox offers a generous free tier—you don't even need to provide payment information. Once you have your access token, in the IntelliJ toolbar, select the "MapServer" dropdown, **Edit Configurations...**, and under **Environment variables** paste your token after the `TOKEN=` text. Click **OK**, re-run Husky Maps, and your map should now load images from MapBox.

## Deploying Husky Maps to the web

An easy way to deploy apps to the web is by distributing them as a **JAR**: a file that bundles all of your code so that it can run on anyone else's machine even without installing IntelliJ. The project is already configured to make it easy for you to create a JAR that runs on Heroku.

### Bundling your program so that it can run anywhere

Open IntelliJ. From the **Build** menu, select **Build Artifacts** and build the **huskymaps**. This will create a `huskymaps.jar` file in the `out/artifacts/huskymaps` directory containing all the code needed to run the Husky Maps web app.

Test your JAR by running it from the terminal. In IntelliJ, [open the terminal](https://www.jetbrains.com/help/idea/terminal-emulator.html#open-terminal), and run the following command. If everything works, you should see the Javalin welcome message.

```
PORT=8080 java -jar out/artifacts/project_jar/project.jar
```

Once you have a runnable JAR file, we need to configure Heroku so it's able to accept your JAR.

### Telling Heroku how to run your app

1. Create a free [Heroku account](https://signup.heroku.com/dc).
1. Set up [Heroku Command Line Interface](https://devcenter.heroku.com/articles/getting-started-with-java#set-up) and open the terminal.
1. In the terminal, run `heroku login` and sign into Heroku.
1. In the terminal, run `heroku create huskymaps-...` where `...` is your name. This will create a Heroku app with the name `huskymaps-...` visible in your [Heroku Dashboard](https://dashboard.heroku.com/apps).
1. Set the [config variable](https://devcenter.heroku.com/articles/config-vars#managing-config-vars) for your MapBox token by running `heroku config:set TOKEN=...` in the terminal, where `...` is your MapBox access token. Alternatively, you can set `TOKEN` in the Heroku dashboard settings for your app.

Then, install the Heroku Java plugin: run `heroku plugins:install java` in the terminal and deploy your JAR to your `huskymaps-...` app.

```
heroku deploy:jar out/artifacts/project_jar/project.jar --app huskymaps-... --jdk 11
```

Finally, you can visit the link in the terminal to try out the app in your browser. Your app is running on Heroku's servers and can be reached by anyone on the internet! In the future, if you want to update the code for the app, make your changes in IntelliJ, rebuild the JAR, and then re-deploy it to Heroku.
