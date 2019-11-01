import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class MenuBar extends JMenuBar {

//    private JButton holeButton;
//    private JButton groundButton;
//    private JButton wallButton;
//    private JButton inputButton;
//    private JButton outputButton;
    private Terrain terrain;
    private Script script;

    private JMenu gameTypeMenu;
    private JMenu terrainMenu;

    public MenuBar(Terrain terrainParam, Script scriptParam) {

        terrain = terrainParam;
        script = scriptParam;

        gameTypeMenu = new JMenu("Game");
        this.add(gameTypeMenu);
        gameTypeMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                Terrain.TerrainMode newMode = terrain.switchModes();
                if (newMode == Terrain.TerrainMode.PLAY) {
                    // TODO: gray out some buttons or menuitems.
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        terrainMenu = new JMenu("Terrain");

        JMenuItem item = new JMenuItem("Hole");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                terrain.setTool(Terrain.TerrainTool.HOLE);
            }
        });

        terrainMenu.add(item);

        item = new JMenuItem("Ground");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                terrain.setTool(Terrain.TerrainTool.GROUND);
            }
        });

        terrainMenu.add(item);

        item = new JMenuItem("Wall");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                terrain.setTool(Terrain.TerrainTool.WALL);
            }
        });

        terrainMenu.add(item);

        item = new JMenuItem("Input");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                terrain.setTool(Terrain.TerrainTool.INPUT);
            }
        });

        terrainMenu.add(item);

        item = new JMenuItem("Output");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                terrain.setTool(Terrain.TerrainTool.OUTPUT);
            }
        });

        terrainMenu.add(item);

        this.add(terrainMenu);
    }

}
