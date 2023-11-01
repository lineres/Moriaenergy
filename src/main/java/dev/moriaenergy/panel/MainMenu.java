package dev.moriaenergy.panel;

import javax.swing.*;

import dev.moriaenergy.LevelCategory;
import dev.moriaenergy.Main;
import dev.moriaenergy.Parser;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainMenu extends QuittablePanel {

    private static final File officialResources = new File("src/main/resources/official levels");
    private static final File customResources = new File("src/main/resources/custom levels");
    private static LevelCategory selectedLevelCategory = LevelCategory.OFFICIAL;
    public static LevelCategory getSelectedLevelCategory() {
        return selectedLevelCategory;
    }

    final JComboBox<String> bankComboBox = new JComboBox<>( new String[]{
              "Official levels", "Custom levels" } );
    final JComboBox<String> levelsComboBox = new JComboBox<>();

    public MainMenu(){

        setLayout(new GridBagLayout());

        this.setLayout(new GridLayout(1,2,10,10));
        setMinimumSize(new Dimension(600,400));

        // Initialisation des grid layouts
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        JPanel levelPanel = new JPanel(new GridBagLayout());
        levelPanel.setLayout(new GridLayout(1, 2, 10, 10));
        JButton placeHolder = new JButton();
        placeHolder.setVisible(false);

        JButton quitButton = new JButton("Quit");
        JButton playButton = new JButton("Play");
        JButton editButton = new JButton("Edit");
        JButton levelMakerButton = new JButton("Level maker");

        KeyboardListener.setupQuitButton(quitButton);
        KeyboardListener.setupEditButton(editButton,levelsComboBox,selectedLevelCategory);
        KeyboardListener.setupLevelMakerButton(levelMakerButton);
        KeyboardListener.setupPlayButton(playButton,levelsComboBox,selectedLevelCategory);

        editButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                try {
                    Main.instance.switchTo( new LevelMaker(Parser.parse( getSelectedLevelPath())));
                } catch( Exception ex ) {
                    throw new RuntimeException( ex );
                }
            }
        } );


        JPanel editerPanel = new JPanel();
        editerPanel.setSize(25, 25);
        editerPanel.add(levelMakerButton);

        JPanel jouerPanel = new JPanel();
        jouerPanel.setSize(25, 25);
        jouerPanel.add(playButton);

        JPanel editLevelPanel = new JPanel();
        editLevelPanel.setSize(25,25);
        editLevelPanel.add(editButton);

        JPanel quitterPanel = new JPanel();
        quitterPanel.setSize(25, 25);
        quitterPanel.add(quitButton);

        buttonPanel.add(jouerPanel);
        buttonPanel.add(editLevelPanel);
        buttonPanel.add(editerPanel);
        buttonPanel.add(placeHolder);
        buttonPanel.add(quitterPanel);
        updateLevelsList();
        JPanel choixBanquePanel = new JPanel();
        choixBanquePanel.setSize(25, 25);
        choixBanquePanel.add(bankComboBox);
        JPanel choixNiveau = new JPanel();
        choixNiveau.setSize(25, 25);
        choixNiveau.add(levelsComboBox);

        setupComboBox();

        levelPanel.add(choixBanquePanel);
        levelPanel.add(choixNiveau);

        this.add(buttonPanel);
        this.add(levelPanel);
    }


    public void updateLevelsList() {
        String[] newLevels;
        if( Objects.equals( bankComboBox.getSelectedItem(), "Official levels" ) ){
            selectedLevelCategory = LevelCategory.OFFICIAL;
            newLevels = loadOfficialBank();
        }else{
            selectedLevelCategory = LevelCategory.CUSTOM;
            newLevels = loadLevelBank(customResources);
        }
        int size = levelsComboBox.getItemCount();
        for(int i=0;i<size;i++){
            levelsComboBox.removeItemAt(0);
        }
        for( String nouveauxItem : newLevels ) {
            levelsComboBox.addItem( nouveauxItem );
        }
    }

    private static String[] loadOfficialBank() {
        String[] list = loadLevelBank( MainMenu.officialResources );
        String[] result = new String[list.length];
        // tri de la liste
        for( String str : list ) {
            String level_str = str.substring(5);
            int level = Integer.parseInt( level_str );
            result[level - 1] = level_str;
        }
        return result;
    }

    private static String[] loadLevelBank(File folder){
        ArrayList<String> liste = new ArrayList<>();
        for (File fileEntry : Objects.requireNonNull( folder.listFiles() ) ) {
            String name = fileEntry.getName();
            if(name.endsWith(".nrg"))
                liste.add(name.substring( 0, name.length()-4 ));
        }
        return liste.toArray(new String[0]);
    }

    private String getSelectedLevelPath() {
        String fileName = (String) levelsComboBox.getSelectedItem();
        if( selectedLevelCategory == LevelCategory.OFFICIAL ) {
            fileName = "official levels/level" + fileName + ".nrg";
        } else {
            fileName = "custom levels/" + fileName + ".nrg";
        }
        return fileName;
    }

    private void setupComboBox(){
        AbstractAction cbActionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLevelsList();
            }
        };
        bankComboBox.addActionListener(cbActionListener);
    }

}
