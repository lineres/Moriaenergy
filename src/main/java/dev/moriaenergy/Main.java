package dev.moriaenergy;

import javax.swing.*;

import dev.moriaenergy.geometry.*;
import dev.moriaenergy.panel.LevelMaker;
import dev.moriaenergy.panel.MainMenu;
import dev.moriaenergy.panel.QuittablePanel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Main extends JFrame{

	public static Main instance;
	public final MainMenu mainMenu = new MainMenu();
	private QuittablePanel currentPanel;

	Main() {
		super();
		instance = this;
		setTitle("MoriaEnergy");
//		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo( null );
		setMinimumSize( new Dimension(400, 400) );

		var frame = this;
		addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent e ) {
				super.keyPressed( e );
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					frame.setVisible( false );
					frame.dispose();
				}
			}
		} );
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if(currentPanel == mainMenu) {
					setVisible( false );
					dispose();
				} else
					currentPanel.quit();
			}
		});
		switchTo(mainMenu);
		pack();
		setVisible( true );
	}

	public void switchTo(QuittablePanel panel) {
		getContentPane().removeAll();
		getContentPane().add(panel);
		revalidate();
		pack();
		currentPanel = panel;
	}

	public void levelMakerPopup() throws Exception {

		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);
		xField.setText( "5" );
		yField.setText("5");
		JPanel initInputPanel = new JPanel();
		initInputPanel.add(new JLabel("width:"));
		initInputPanel.add(xField);
		initInputPanel.add(Box.createHorizontalStrut(15)); // a spacer
		initInputPanel.add(new JLabel("height:"));
		initInputPanel.add(yField);
		initInputPanel.add(new JLabel("Form:"));
		JComboBox<String> formComboBox = new JComboBox<>( new String[]{ "Square", "Hexagon" } );
		initInputPanel.add( formComboBox );

		int result = JOptionPane.showConfirmDialog(null, initInputPanel,
				  "Please enter map's height and width", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Constructor<?> formConstructor = null;
			switch( formComboBox.getItemAt( formComboBox.getSelectedIndex() ) ) {
				case "Square" -> formConstructor = Square.class.getConstructors()[0];
				case "Hexagon" -> formConstructor = Hexagon.class.getConstructors()[0];
			}
			if(formConstructor == null)
				return;

			Map map = new Map( Integer.parseInt( yField.getText() ), Integer.parseInt( xField.getText()));
			for( int i = 0; i < map.getH(); i++ )
				for( int j = 0; j < map.getW(); j++ )
					if(map.array[i][j] == null)
						map.array[i][j] = (Cell)
							  formConstructor.newInstance( null, j, i, new ArrayList<>() );

			switchTo( new LevelMaker(map) );
		}
	}

	public static void main( String[] args ) {
		new Main();
	}

}
