# RPG-DUDE
Text-based RPG written in Java.

Created by @kleinesfilmroellchen and DJH as a freetime project.

__Note: all of the menu and manual is currently in German. There is support for other languages, though (got around to this earlier than I thought :D ) and we appreciate any help in translating.__

The English translation will soon be started, when we are finished with externalizing all strings

The most recent version is 0.0.0008a, with "a" standing for Alpha, and development is made on 0.0.0009a, which will include a lot of refactoring and optimization, as well as part of the item and ability system.

A changelog in German can be found in the ```CHANGELOG.txt``` file.

Instructions on how to run the game and information about command line arguments follow.

## What is RPG-DUDE?

RPG-DUDE, more commonly known as "java-RPG", is a very basic text-based adventure and role-play-game (RPG) written in Java and using nothing but the command line as an interface to the user: they write their actions into the command line and get back text prompts.

In java-RPG, you are a fearless adventurer stranded in a strange world inhabited by guards, hunters, wild animals and monsters. Navigate through puzzles, collect items and fight your foes to gain experience and become the most powerful adventurer alive! There are magical abillities to be learned and weapons to be mastered!

## How do I run RPG-DUDE?

There are two options: either use any terminal or (on Windows machines) execute the batch scripts.

The application can be run by executing the command ```java -jar RPG.jar <arguments>```.

You need to have Java version 1.8 or later installed (test this by writing ```java -h``` in the command line and pressing enter, you should get a detailed information page).

There are several arguments to the executable:
- ```manual``` opens the game manual with detailed instructions and information. This is still in development and not complete in any way.
- ```debug``` enables the "debugger" with additional information and abillities. This is equivalent to using the 'd' command in-game.
- ```logging``` enables logging of all input and output into a ```log.txt``` file in the same directory. Useful for bugtracking (see below) and debugging.

In the ```res``` folder besides your executable there must be ```map1.json```, ```config.json``` and ```enemies.json``` files, which are read by the program and used for setting up your game. The config file can be specified with the command line option ```config=<filename>``` but must also be contained in the 'res'-folder. This configuration file specifies which language and maps should be used. Feel free to study the file structure and create your own configurations, maps and enemies! We only check the program against official map and enemy files as well as the default config, so your's might not work, also when switching from one version to another.

## How can I contribute to RPG-DUDE?

Contributions as well as feedback is always welcome. We accept feedback in the form of issues, especially when you suggest changes or additions. Contributions in the form of pull requests can also be made; we will decide over these on a case-by-case basis.

Bug reports as issues are also welcome, even better are fixes! When you submit bugs, you might want to include the log.txt file which contains your bug (see above on how to recieve one).
