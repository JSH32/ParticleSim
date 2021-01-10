## Cellular automata / Falling sand

![Screenshot](https://i.postimg.cc/dVSPcbyz/Zju9qRw.png)

`ParticleSim.SandLab.main` is the entry point. This program is designed differently than the original architecture given in the document (I have been given permission to deviate).

### General architecture
Each pixel derives from Cell class and has its own update function that is called with each display update. Each pixel has a state such as `Wood`/`FlammableCell` having durability and a `damage` method.

### Interesting internal design
-  `ParticleSim.Cell.getSurroundingCell`
	This method looks for surrounding cells that either are any type on `Cell`'s inheritance tree, accomplished with generics. This is used in `ParticleSim.Cells.Fire` to look for surrounding `ParticleSim.FlammableCell`
-  Each cell has a bitflag as does the program itself, this flag will be flipped when a cell is updated and the program's bitflag will be flipped once all updates are completed. This ensures a cell will not be updated twice after moving.

### Important notes
I was told not to modify `ParticleSim.SandLab.run` but the time loop was broken and the slider was useless. I implemented my own TPS system for world updates per second. I changed `step` to `update` for personal preference. Due to architecture design I had to add more fields to `ParticleSim.SandLab`, this includes new buttons and bounds checking methods or circular brush sizes. The slider on the bottom that should be used for speed but didn't work is now a brush size slider. The reset button does not instantly reset the map, you need to click in the map to reset it. `ParticleSim.SandDisplay` was also modified to accomodate these changes.

### Running
I would have preffered to not need any build systems to make this function as it should but unfortunately with Java there is no such luck. This is bundled as a barebones maven project, as long as JDK and maven are downloaded/installed you should be able to run `run.bat` or `run.sh` in the root directory on your respective platform.