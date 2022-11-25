# RandomHandler for [DreamBot](https://dreambot.org)

One of the great things of the old days of OSRS cheating, especially when SCAR was popular, was the sharing and co-development of things like random event solvers so I'm bringing it back.

Here you'll find a collection of random event solvers that work out of the box with DreamBot.

More will be added as I encounter them and have the time to solve them.

## Usage
- Add the files to your script, ideally in a folder named randoms.
- Add ```RandomHandler.loadRandoms();``` to ```onStart```.
- Add ```RandomHandler.clearRandoms();``` to ```onExit```.
- Use ```RandomHandler.loadRandom(Event.DRUNKEN_DWARF)``` and ```RandomHandler.unloadRandom(Event.DRUNKEN_DWARF)``` respectively to only load your desired solver. 
- Lamp will automatically be used by GenieSolver, to take care of it manually, remove the line from GenieSolver and use ```RandomHandler.useLamp();``` where desired.
- Watch the magic happen!
