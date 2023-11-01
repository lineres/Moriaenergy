package dev.moriaenergy.mouseadapters;

import dev.moriaenergy.geometry.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellEditorMouseAdapter extends MyMouseAdapter {

	public CellEditorMouseAdapter( Map map, JPanel panel) {
		super(map, panel);
	}

	@Override
	public void mouseClicked( MouseEvent e ) {
		Cell cell = getCellUnderMouse( e );
		if(cell == null)
			return;
		int result = JOptionPane.showConfirmDialog(
				  null,
				  new RotationEditorPanel( getCellUnderMouse( e ) ),
				  "Add or remove connections",
				  JOptionPane.DEFAULT_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			panel.repaint();
		}
		map.enableSources();
		map.updateWifi();

	}

	public class RotationEditorPanel extends JPanel {

		private final Cell cell;

		RotationEditorPanel( Cell cell ) {
			this.cell = cell;
			setPreferredSize( new Dimension( 150, 300 ) );

			for( int i = 0; i < cell.getMaxNeighbors(); i++ ) {
				JButton button = new JButton( cell.getSideString()[i] );
				button.setBackground(
						  cell.rotations.contains( i ) ? Color.green : Color.red
									);
				Integer finalI = i;
				button.addMouseListener( new MouseAdapter() {
					@Override
					public void mouseClicked( MouseEvent e ) {
						if( cell.rotations.contains( finalI ) ) {
							cell.rotations.remove( finalI );
							e.getComponent().setBackground( Color.red );
						} else {
							cell.rotations.add( finalI );
							e.getComponent().setBackground( Color.green );
						}
						changed = true;
						repaint();
					}
				} );
				add( button );
			}

			JButton sourceButton = new JButton( "Source" );
			sourceButton.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseClicked( MouseEvent e ) {
					cell.tile = cell.tile != Tile.S ? Tile.S : null;
					changed = true;
					repaint();
				}
			} );
			add( sourceButton );

			JButton lampButton = new JButton( "Lamp" );
			lampButton.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseClicked( MouseEvent e ) {
					cell.tile = cell.tile != Tile.L ? Tile.L : null;
					changed = true;
					repaint();
				}
			} );
			add( lampButton );

			JButton wifiButton = new JButton( "Wifi" );
			wifiButton.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseClicked( MouseEvent e ) {
					cell.tile = cell.tile != Tile.W ? Tile.W : null;
					changed = true;
					repaint();
				}
			} );
			add( wifiButton );
		}

		@Override
		public void paint( Graphics g ) {
			super.paint( g );
			g.setColor( Color.BLACK );
			g.fillRect( 0, 180, 120, 120 );
			cell.update_rotations_images();
			cell.paint( g, 10, 190, 100 );
		}

	}

}
