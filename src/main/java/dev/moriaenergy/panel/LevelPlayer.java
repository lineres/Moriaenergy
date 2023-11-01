package dev.moriaenergy.panel;

import dev.moriaenergy.Main;
import dev.moriaenergy.Parser;
import dev.moriaenergy.Utils;
import dev.moriaenergy.geometry.Cell;
import dev.moriaenergy.geometry.Map;
import dev.moriaenergy.mouseadapters.RotatorMouseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelPlayer extends QuittablePanel {

	static String levelRef;

	public LevelPlayer(String level) throws Exception {
		levelRef = level;
		setLayout( new GridBagLayout() );

		Map map = Parser.parse( level );
		randomRotation( map );
		Displayer displayer = new Displayer( map );
		add( displayer );

		JPanel controller = new JPanel();
		controller.setLayout( new GridLayout( 4, 1, 10, 10) );
		JButton returnButton = new JButton("Return");
		returnButton.addMouseListener(  new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				quit();
			}
		} );
		KeyboardListener.setupReturn(returnButton,this);
		controller.add( returnButton );

		Utils.configGirdBagLayout( this, displayer, controller );
		RotatorMouseAdapter rotatorMouseAdapter = new RotatorMouseAdapter( map, this, true );
		displayer.setMouseAdapter( rotatorMouseAdapter );
		setVisible( true );
	}

	@Override
	public void quit() {
		setVisible( false );
		Main.instance.switchTo(Main.instance.mainMenu);
	}

	public static void randomRotation(Map map){
		Random rand = new Random();
		for(int i =0;i<map.array.length;i++){
			for(int j =0;j<map.array[i].length;j++) {
				Cell cell = map.array[i][j];

				int valeur = 1 + rand.nextInt( cell.getMaxNeighbors()-1 );
				List<Integer> before_rotation = new ArrayList<>( cell.rotations );
				for(int k=0;k<valeur;k++){
					cell.rotate();
				}
				for(Cell neighbor : cell.getNeighbors(map, before_rotation)) {
					if( !neighbor.seekPower( map ) ) {
						neighbor.setEnabled( map, false );
					}
				}
				// on regarde si un des ces nouveaux voisins est alimenté
				boolean found = false;
				for(Cell neighbors : cell.getNeighbors(map)) {
					if( neighbors.isEnabled() ) {
						found = true;
						break;
					}
				}
				cell.setEnabled( map, found );
				cell.update_rotations_images();
			}
		}

	}

}
