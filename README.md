# powerworks

This is Powerworks. It is a 2d, top down, tile based game in which one builds factories. The objective is to eventually automate
the production of war robots, with which one can go into the multiplayer world and attack other's factories. However, this is the end goal for development and the game is mostly framework as of now.

It is currently in a basic form, with features such as:
* Two basic blocks: conveyor belt and ore miner
* Deterministic level generated using simplex noise
* Powerful GUI system, allowing for creating entire interfaces with only a few lines
* Sound effects for the conveyor belt (not the ore miner as I can't find a good sound)
* Good performance - can run at 60fps using 2 percent CPU even while the screen is totally full of objects that require updating and rendering. The GPU is utilised for maximum efficiency. With all stops off, it can run at 3000 FPS in game and 6000+ on the main menu
* Easily configurable and easily usable input system

Note: this is not meant to be extended or worked on by anybody else. Feel free to, but this will not be updated by me as I have switched to Kotlin and that repository is private and will be private as long as I am working on it.

CLOC stats:

language: Java         
files : 127           
blank : 1612           
comment : 1838           
code : 8768

Check out https://twitter.com/PowerworksGame for news about the current version!
Message me if you want to get a download of it right now, or look at the current source code (I've come a long way from this)
