package dev.moriaenergy.mouseadapters;

import dev.moriaenergy.Main;
import dev.moriaenergy.geometry.Cell;
import dev.moriaenergy.geometry.Map;
import dev.moriaenergy.panel.VictoryScreen;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class RotatorMouseAdapter extends MyMouseAdapter {

	private final boolean checkVictory;

	public RotatorMouseAdapter( Map map, JPanel panel, boolean checkVictory ) {
		super(map, panel);
		this.checkVictory = checkVictory;
	}

	@Override
	public void mouseClicked( MouseEvent e ) {

		Cell cell = getCellUnderMouse(e);
		if(cell == null)
			return;
		if( !cell.rotations.isEmpty())
			changed = true;
		//si distance nearest > celle du rayon alors on est dans une zone vide
		List<Integer> before_rotation = new ArrayList<>( cell.rotations );

		cell.rotate();

		// on regarde ces ancients voisins, et on desactive si ils ne sont
		// plus alimentées
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
		map.updateWifi();
		panel.repaint();

		if(checkVictory) { //on regarde si victoire
			if( this.map.victory() ) {
				Main.instance.switchTo( new VictoryScreen() );
			}
		}
	}
}
