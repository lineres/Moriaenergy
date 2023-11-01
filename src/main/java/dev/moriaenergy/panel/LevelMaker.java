package dev.moriaenergy.panel;

import dev.moriaenergy.Main;
import dev.moriaenergy.Saver;
import dev.moriaenergy.Utils;
import dev.moriaenergy.geometry.Cell;
import dev.moriaenergy.geometry.Map;
import dev.moriaenergy.mouseadapters.CellEditorMouseAdapter;
import dev.moriaenergy.mouseadapters.RotatorMouseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelMaker extends QuittablePanel {

	private final Displayer displayer;

	public LevelMaker( Map map ) {
		displayer = new Displayer( map );
		RotatorMouseAdapter rotatorMouseAdapter = new RotatorMouseAdapter( map, this, false );
		CellEditorMouseAdapter cellEditorMouseAdapter = new CellEditorMouseAdapter( map, this );
		setLayout( new GridBagLayout() );

		JPanel controller = new JPanel();
		controller.setLayout( new GridLayout( 5, 1, 10, 10) );
		JButton rotatorButton = new JButton("Rotating Mode");
		rotatorButton.addMouseListener(  new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				displayer.setMouseAdapter( rotatorMouseAdapter );
			}
		} );
		JButton rotationAdderButton = new JButton("Cell editor mode");
		rotationAdderButton.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				displayer.setMouseAdapter( cellEditorMouseAdapter );
			}
		} );
		JButton saveButton = new JButton("Save");
		saveButton.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				if(!map.victory()) {
					JOptionPane.showConfirmDialog(
							  null,
							  "A level need to be saved in the winning position",
							  "Error",
							  JOptionPane.DEFAULT_OPTION);
					return;
				}
				String filename = JOptionPane.showInputDialog("Save", "MyLevel");
				if(filename != null && !filename.isBlank()) {
					Saver.save( map, filename.trim() + ".nrg" );
					displayer.mouseAdapter.changed = false;
				}
			}
		} );

		JButton emptyButton = new JButton("Empty");
		emptyButton.setBorder( BorderFactory.createLineBorder(Color.RED));
		emptyButton.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				int res = JOptionPane.showConfirmDialog(
						  null,
						  "Are you really sure you want to empty the whole level?",
						  "Confirmation",
						  JOptionPane.YES_NO_OPTION);
				if(res == JOptionPane.OK_OPTION) {
					for(Cell[] cells : map.array) {
						for(Cell cell : cells) {
							cell.clear();
							displayer.repaint();
							displayer.mouseAdapter.changed = true;
						}
					}
				}
			}
		} );

		JButton returnButton = new JButton("Return");
		returnButton.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				quit();
			}
		} );
		KeyboardListener.setupReturn(returnButton,this);
		
		controller.add( rotatorButton );
		controller.add( rotationAdderButton );
		controller.add( emptyButton );
		controller.add( saveButton );
		controller.add( returnButton );
		Utils.configGirdBagLayout( this, displayer, controller );
		displayer.setMouseAdapter(rotatorMouseAdapter);
		setVisible( true );
	}

	@Override
	public void quit() {
		int res;
		if(displayer.mouseAdapter.changed )
			res = JOptionPane.showConfirmDialog(
					  null,
					  "You have unsaved changes, are you sure you want to leave?",
					  "Confirmation",
					  JOptionPane.YES_NO_OPTION);
		else
			res = JOptionPane.OK_OPTION;
		if(res == JOptionPane.OK_OPTION) {
			setVisible( false );
			Main.instance.switchTo( Main.instance.mainMenu );
		}
	}

}
