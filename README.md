# Husky Maps

[Husky Maps](https://huskymaps.kevinl.info/) is an educational web app for mapping the world, searching for places, and navigating around Seattle. All these features are powered by data structures and algorithms, programming abstractions designed to represent data and automate processes. In your prior programming experience, you learned how to implement specifications by writing Java programs that used data structures to solve problems. But why was the specification written that way in the first place?

Husky Maps consists of 4 educational programming projects that aim to highlight the relationship between abstractions, implementations, and problems. The purpose of these projects is to help us explore the capabilities and design effort that goes into writing a specification. Under each project are relevant lessons from the [Wiki](https://github.com/kevinlin1/huskymaps/wiki).

1. [**Deques**](src/main/java/deques/)
   1. [Dynamic Arrays](https://github.com/kevinlin1/huskymaps/wiki/Dynamic-Arrays)
   1. [Linked Nodes](https://github.com/kevinlin1/huskymaps/wiki/Linked-Nodes)
   1. [Asymptotic Analysis](https://github.com/kevinlin1/huskymaps/wiki/Asymptotic-Analysis)
   1. [Iterative Sorts](https://github.com/kevinlin1/huskymaps/wiki/Iterative-Sorts)
1. [**Autocomplete**](src/main/java/autocomplete/)
   1. [Binary Search](https://github.com/kevinlin1/huskymaps/wiki/Binary-Search) and [Merge Sort](https://github.com/kevinlin1/huskymaps/wiki/Merge-Sort)
   1. [Binary Search Trees](https://github.com/kevinlin1/huskymaps/wiki/Binary-Search-Trees) and [Tries](https://github.com/kevinlin1/huskymaps/wiki/Tries)
   1. [2–3 Trees](https://github.com/kevinlin1/huskymaps/wiki/2%E2%80%933-Trees)
   1. [Left-Leaning Red-Black Trees](https://github.com/kevinlin1/huskymaps/wiki/Left-Leaning-Red-Black-Trees)
1. [**Priority Queues**](src/main/java/minpq/)
   1. [Quicksort](https://github.com/kevinlin1/huskymaps/wiki/Quicksort) and [Counting Sorts](https://github.com/kevinlin1/huskymaps/wiki/Counting-Sorts)
   1. [Binary Heaps](https://github.com/kevinlin1/huskymaps/wiki/Binary-Heaps)
   1. [Hash Tables](https://github.com/kevinlin1/huskymaps/wiki/Hash-Tables)
   1. [Affordance Analysis](https://github.com/kevinlin1/huskymaps/wiki/Affordance-Analysis)
1. [**Shortest Paths**](src/main/java/seamfinding/)
   1. [Graph Data Type](https://github.com/kevinlin1/huskymaps/wiki/Graph-Data-Type)
   1. [Graph Traversals](https://github.com/kevinlin1/huskymaps/wiki/Graph-Traversals)
   1. [Shortest Paths Trees](https://github.com/kevinlin1/huskymaps/wiki/Shortest-Paths-Trees) and [Problems and Solutions](https://github.com/kevinlin1/huskymaps/wiki/Problems-and-Solutions)
   1. [Topological Sorting](https://github.com/kevinlin1/huskymaps/wiki/Topological-Sorting) and [Dynamic Programming](https://github.com/kevinlin1/huskymaps/wiki/Dynamic-Programming)
   1. [Minimum Spanning Trees](https://github.com/kevinlin1/huskymaps/wiki/Minimum-Spanning-Trees) and [Disjoint Sets](https://github.com/kevinlin1/huskymaps/wiki/Disjoint-Sets)

## Getting started

> [!warning]
> These instructions are designed for students enrolled in a University of Washington course!

### Install Java and configure VS Code

> [!note]
> If you took [CSE 123](https://courses.cs.washington.edu/courses/cse123/) last quarter, skip this step and proceed to [**Additional VS Code configuration**](#additional-vs-code-configuration).

Let's install the Java Development Kit (JDK), install VS Code, and apply the CSE 123 VS Code profile.

1. [Download and install Eclipse Temurin (JDK)](https://adoptium.net/).

1. [Download and install VS Code](https://code.visualstudio.com/).

1. Apply the [CSE 123 VS Code profile](https://courses.cs.washington.edu/courses/cse123/25au/software/cse123-Java.code-profile).

To apply the profile, first, copy the link to the profile. Then, pull up the **Command Palette** (Show All Commands) with the keyboard shortcut <kbd>Ctrl</kbd> <kbd>Shift</kbd> <kbd>P</kbd> and search for **Preferences: Open Profiles (UI)**. Under the dropdown menu next to New Profile, choose **Import Profile...** and paste the link to the CSE 123 VS Code profile. Finally, select **Create Profile** and name it "cse123-Java".

### Additional VS Code configuration

**Open Settings** with the keyboard shortcut <kbd>Ctrl</kbd> <kbd>,</kbd>, search for `debug.console.collapseIdenticalLines`, and uncheck the setting.

### Install Git

Git is a **version control system** (VCS), a tool for distributing, managing, and sharing code with other people. Install Git by [following the instructions for your computer's operating system](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

### Generate SSH keys

Your project code will be stored on CSE GitLab, a service provided by the Allen School to securely and privately store your coursework. CSE GitLab requires **SSH keys**: computer-generated passwords used to securely authenticate your computer to CSE GitLab.

1. From VS Code, open the Terminal with the keyboard shortcut <kbd>Ctrl</kbd> <kbd>`</kbd> (backtick key, which is typically above your tab key).

1. In the terminal, generate an SSH key pair with the command `ssh-keygen -t ed25519`. When asked to enter a file name or a passphrase, just press <kbd>Enter</kbd> to accept the default settings for both questions.

1. Print your public SSH key with the command `cat ~/.ssh/id_ed25519.pub` and copy the contents to your clipboard.

1. In your browser, open the [SSH Keys user settings](https://gitlab.cs.washington.edu/-/user_settings/ssh_keys). Sign into GitLab using your UW NetID. Then, paste the public SSH key in the **Key** field.

1. Give it a title representing your computer (any title of your choice) and press **Add key**.

### Obtain project code

Now that we've installed the required software and configured our computer so that it can securely communicate with CSE GitLab, let's get the project code! This step requires the project repositories to be setup, which will be ready for enrolled students by the end of the first week of the course.

1. Visit [CSE GitLab](https://gitlab.cs.washington.edu/) and you'll be greeted by a repository named after your UW NetID.

1. Visit your private repository, press the blue **Code** button and, under the heading **Open in your IDE**, choose **Visual Studio Code (SSH)**. Be sure to choose the SSH option because we are using SSH keys. If this doesn't work, you can copy the **Clone with SSH** address, find the **Git: Clone** tool in the VS Code command palette, and paste the SSH address. You can choose where you would like to store the project.

1. If you see an **SSH Confirmation** window, check that the fingerprint matches the [CSE GitLab fingerprint](https://gitlab.cs.washington.edu/help/instance_configuration#ssh-host-keys-fingerprints). Compare the value to the table entries, most likely under **SHA256**. If it matches one of the values, accept the connection. If it doesn't match any of the values, do not accept the connection and write an Ed Discussion question with the mismatch.

> [!caution]
> VS Code may suggest installing Gradle for Java. **Do not install Gradle for Java** as it will conflict with Language Support for Java. If it was accidentally installed, go to the **Extensions** icon in the Activity Bar and uninstall or disable Gradle for Java.

The first time you open the project, answer the prompt **Yes, I trust the authors** so that VS Code has your permission to run code. Near the very bottom of VS Code, the status bar shows the current state of the Java environment setup. While we wait for it to report "Java: Ready", right-click the file `README.md` in VS Code and choose **Open Preview** to see exactly these instructions in VS Code.

### Run project code

After the status bar shows "Java: Ready", let's try running the project code.

1. In the **Explorer** (file explorer), navigate to the **src \| main \| java** folder. This folder contains all the Java program logic for this project.

1. Open the **BrowserHistory.java** file and drag these instructions off to the side to split the VS Code window left and right.

1. **Run Java** using the ▷ play button at the top of the tab strip or right above the `main` method.

If all the software was installed correctly, you should be able to see some text indicating that the class is being run. If you can find the following `main` method output somewhere, you're all set!

```
[uw.edu, my.uw.edu, cs.uw.edu, canvas.uw.edu]
[cs.uw.edu, notify.uw.edu]
```

Now that you're able to run the simple `BrowserHistory` class, let's try running the more complicated `MapServer` class. If everything is successful, you'll see this flurry of messages appear indicating that the app has launched.

```
[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - ...
[main] INFO org.eclipse.jetty.server.session. ...
[main] INFO org.eclipse.jetty.server.handler. ...
[main] INFO org.eclipse.jetty.server.Abstract ...
[main] INFO org.eclipse.jetty.server.Server - ...
[main] INFO io.javalin.Javalin - 
       __                  ___           _____
      / /___ __   ______ _/ (_)___      / ___/
 __  / / __ `/ | / / __ `/ / / __ \    / __ \
/ /_/ / /_/ /| |/ / /_/ / / / / / /   / /_/ /
\____/\__,_/ |___/\__,_/_/_/_/ /_/    \____/

       https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Javalin started in ...
[main] INFO io.javalin.Javalin - Listening on ...
[main] INFO io.javalin.Javalin - You are running ...
```

You're done! You can now visit the link that Javalin is `Listening on ...` to view the app.

All the basic app features except for map images work because we've provided reference implementations for each interface that we'll learn in this class. The study of data structures and algorithms is the study of different approaches to implementing the same functionality, which is exactly what we'll be exploring through these projects.

### Optional: Load map images with MapBox

If you're interested to try the web app for yourself, the map images won't load without a MapBox access token. This access token is used to track your usage and (if you exceed free limits) bill you for their service.

To see the map images, [sign up for a free MapBox account](https://account.mapbox.com/auth/signup/?route-to=%22https://account.mapbox.com/access-tokens/%22) to get an access token. Husky Maps primarily uses the [Static Images API](https://docs.mapbox.com/api/maps/static-images/) which, at the time of writing, has a free limit of up to 50,000 requests per month.

Once you have your access token, in VS Code, create a new workspace configuration through the **Run \| Add Configuration...** menu. In the `configurations` section, find the entry for `MapServer` and add the following JSON:

```
"env": {
    "TOKEN": "..."
}
```

Remember to replace the ellipsis with your token, include double quotes around your token string, and add a comma to the end of the preceding line so that the JSON syntax is valid. Finally, re-run the `MapServer` class to launch the web app and enjoy the ["Ice Cream" map style by Maya Gao](https://www.mapbox.com/gallery#community-ice-cream).

## Optional: Web deployment

When you run `MapServer` in VS Code, it can only be accessed from your own computer. Optionally, follow these instructions to deploy `MapServer` to the web using the cloud application provider, Render. Render offers a free tier with 0.1 CPUs (one-tenth of a virtual CPU) and 512MB of RAM.

1. Render does not support CSE GitLab repositories, so you will need to sign up for a GitHub, GitLab, or BitBucket account if you do not already have one.

1. Create a private repository in your GitHub, GitLab, or BitBucket account and push your Husky Maps implementation to it.

1. [Register for a Render account](https://dashboard.render.com/register) using your linked GitHub, GitLab, or BitBucket account.

1. Follow the sign-up flow and, when prompted, create a new **Web Service**.

1. For **Source Code**, connect your private repository.

1. For **Name**, choose a name for your project that will become part of the URL.

1. For **Language**, choose Docker.

1. Under **Environment Variables**, add an entry for the variable name `TOKEN` with your [MapBox default public token](https://account.mapbox.com/) as the value.
