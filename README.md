# MusicTherapy
> This is the app I made along side my MSc IoT thesis

### Downloading
This app was created to be run with android studio  
To get this app first clone the repo:
```
git clone https://github.com/RoryNesbitt/MusicTherapy.git
```
The source code is located in the Music Therapy App directory, and the tensorFlow.ipynb file contains the code for the neural netowork.
Then music has to be added to the assets folder. The direcotry structure should be assets/music/\<mood number\>/\<music type\>/mp3 file  
This will also display the name of the MP3 file though to correctly display the detiails it should be named \<track name\>.\<artist\>.mp3  
As as example here is the directory structure I use:

[![Directory Structure](https://i.imgur.com/sIY5Jm3.png)]()

Each of the numbered directories have a .gitkeep file. This is to maintain this structure for github though the type directories within them can be changed so do not have this.

It is important that every directory gets at least one subdirectory and .mp3 file as the app will crash if it tries to play with no music availible.
