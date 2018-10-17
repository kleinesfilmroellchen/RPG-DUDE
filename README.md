# RPG-DUDE
Text-based RPG written in Java.

Created by @kleinesfilmroellchen and DJH as a freetime project.

__Note: all of the menu and manual is currently in German. At some point we might translate it into English, but this is not planned currently.__

The most recent version is 0.0.0008a, with "a" standing for Alpha.

A changelog in German can be found in the ```CHANGELOG.txt``` file.

Instructions on how to run the game and information about command line arguments (all German) below.

## What is RPG-DUDE?

RPG-DUDE, more commonly known as "java-RPG", is a very basic text-based adventure and role-play-game (RPG) written in Java and using nothing but the command line as an interface to the user: they write their actions into the command line and get back text prompts.

In java-RPG, you are a fearless adventurer stranded in a strange world inhabited by guards, hunters, wild animals and monsters. Navigate through puzzles, collect items and fight your foes to gain experience and become the most powerful adventurer alive! There are magical abillities to be learned and weapons to be mastered!

## How do I run RPG-DUDE?

There are two options: either use any terminal or (on Windows machines) execute the batch scripts.

The application can be run by executing the command ```java -jar RPG.jar <arguments>```.

You need to have Java version 1.8 or later installed (test this by writing ```java -h``` in the command line and pressing enter, you should get a detailed information page).

There are several arguments to the executable:
- ```manual``` opens the game manual with detailed instructions and information. This is still in development and not complete in any way.
- ```debug``` enables the "debugger" with additional information and abillities.
- ```logging``` enables output of all input and output into a ```log.txt``` file in the same directory. Useful for bugtracking and debugging.

In the same folder as your executable there must be ```map1.json``` and ```enemies.json``` files, which are read by the program and used for setting up your game. Feel free to study the file structure and create your own maps and enemies! We only check the program against official map and enemy files, so your's might not work, also when switching from one version to another.

## How can I contribute to RPG-DUDE?

Contributions as well as feedback is always welcome. We accept feedback in the form of issues, especially when you suggest changes or additions. Contributions in the form of pull requests can also be made; we will decide over these on a case-by-case basis.

Bug reports as issues are also welcome, even better are fixes! When you submit bugs, you might want to include the log.txt file (see above on how to recieve one).