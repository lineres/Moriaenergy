package dev.moriaenergy.mouseadapters;

import javax.swing.*;

import dev.moriaenergy.geometry.Cell;
import dev.moriaenergy.geometry.Map;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseAdapter extends MouseAdapter {

	protected final Map map;
	protected final JPanel panel;
	public int cell_size;
	public boolean changed = false;

	public MyMouseAdapter(Map map, JPanel panel ) {
		this.map = map;
		this.panel = panel;
	}

	protected Cell getCellUnderMouse( MouseEvent e ) {
		double nearest = -1;
		Cell nearestCell = null;
		Point clickPos = new Point( e.getX(), e.getY());
		for( Cell[] cells : map.array ) {
			for( Cell cell : cells ) {
				Point center = cell.centerPoint(cell_size);
				double distance =  clickPos.distance(center);
				if(nearestCell == null || (nearest > distance && distance < cell_size)){
					nearestCell = cell;
					nearest = distance;
				}
			}
		}
		return nearestCell;
	}

}
