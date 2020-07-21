package astar;

import humanresourcemachine.ScriptModel;
import humanresourcemachine.TerrainModel;
import java.util.ArrayList;

/**
 * This class applies the A* algorithm to find a sequence of 'Push' instructions
 * that solves the level.
 *
 * @author arthurmanoha
 */
public class AstarComputer {

    private TerrainModel terrainModel;
    private ScriptModel script;

    private ArrayList<AstarNode> openList;
    private ArrayList<AstarNode> closedList;

    public AstarComputer(TerrainModel terrainModelParam, ScriptModel scriptModelParam) {
        this.terrainModel = terrainModelParam;
        this.script = scriptModelParam;

        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(new AstarNode(script));
    }

}
