package components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import Renderer.DebugDraw;
import engine.Window;
import util.Settings;

public class GridLines extends Component{

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

        //Use integer division to determine the location
        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1)* Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1)* Settings.GRID_HEIGHT;

        int numVertLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2; //Number of vertical lines to fit in screen
        int numHorizonLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2; //Number of horizontal lines to fir in screen

        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(numHorizonLines, numVertLines);
        Vector3f colour = new Vector3f(0.1f, 0.1f, 0.1f);

        for (int i=0; i < maxLines; i++) { //Draw the lines
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVertLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, y + height), colour);
            }
            if (i < numHorizonLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(x + width, y), colour);
            }
        }
    }

}
