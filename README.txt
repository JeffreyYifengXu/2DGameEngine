This is a 2D game engine written in java (Still in development)
Closely references the steps taken by youtube GamesWithGabe. While lower level functionalities
such as displaying window and imgui implementation is taken from gameswithgabe. The rest of the
functionalities (Camera zooming, dragging etc) are implemented by myself while taking snippets of ideas from the tutorial.

To switch between game engine and physics engine. Modify the main.java function by commenting out the first two or second two lines

Current functionalities (Game Engine)
-----------------------------

After compiling through gradle

An editor window will pop up which allows:
- Moving imgui menu around the window, docking
- click on a block to drag on to the screen then click again to place (For designing level)
- Hold middle mouse button to move camera around
- scroll mouse to zoom in and out
- Space bar to return to original camera position

(Physics Engine)
---------------------------
Use arrow keys to control a block to move around
- Basic collision, impiluse and gravity implemented.
