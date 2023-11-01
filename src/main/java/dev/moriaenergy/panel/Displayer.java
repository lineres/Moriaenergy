package dev.moriaenergy.panel;

import dev.moriaenergy.geometry.*;
import dev.moriaenergy.mouseadapters.MyMouseAdapter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Displayer extends JPanel {

	private final Map map;
	private int cell_size = 50;

	public MyMouseAdapter mouseAdapter = null;

	Displayer( Map map ) {
		this.map = map;
		setBackground( Color.black );

		addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized( ComponentEvent e ) {
				cell_size = getWidth() / map.getW();
				cell_size = Math.min(cell_size, getHeight() / map.getH());
				if(mouseAdapter != null)
					mouseAdapter.cell_size = cell_size;
				repaint();
			}
		} );
	}

	public void setMouseAdapter( MyMouseAdapter mouseAdapter ) {
		if(this.mouseAdapter != null)
			removeMouseListener( this.mouseAdapter );
		this.mouseAdapter = mouseAdapter;
		mouseAdapter.cell_size = cell_size;
		addMouseListener( this.mouseAdapter );
	}

	@Override
	public void paint( Graphics g ) {
		super.paint( g );
		for( Cell[] cells : map.array )
			for( Cell cell : cells )
				cell.paint( g, cell_size );
	}

}
