package dev.moriaenergy.panel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.moriaenergy.LevelCategory;
import dev.moriaenergy.Main;

public class VictoryScreen extends QuittablePanel {
    
    public VictoryScreen(){
        JLabel labelVictoire = new JLabel("You won!");
        
        JButton mainMenuButton = new JButton("Return");
        setBoutonRetour(mainMenuButton);
        JButton nextLevelButton = new JButton("Next level");
        setupNextButton(nextLevelButton);

        setMinimumSize(new Dimension(600,400));

        setLayout(new GridBagLayout());
        this.setLayout(new GridLayout(2,1,10,10));
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setLayout(new GridLayout(1,2, 10, 10));
        
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setSize(25, 25);
        mainMenuPanel.add(mainMenuButton);
        JPanel nextLevelPanel = new JPanel();
        nextLevelPanel.setSize(25, 25);
        nextLevelPanel.add(nextLevelButton);

        buttonPanel.add(mainMenuPanel);
        File folder = new File("src/main/resources/official levels");
        if(MainMenu.getSelectedLevelCategory() == LevelCategory.OFFICIAL
           && getNextLevel(LevelPlayer.levelRef) <= folder.listFiles().length ){
            buttonPanel.add(nextLevelPanel);
        }

        this.add(labelVictoire);
        this.add(buttonPanel);
    }

    private static void setupNextButton(JButton boutonSuivant){
        AbstractAction boutonSuivantPresser = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try{
                Main.instance.switchTo( new LevelPlayer( "official levels/level" + getNextLevel( LevelPlayer.levelRef ) + ".nrg") );
            }catch(Exception error){
                error.printStackTrace();
            }
            }

        };

        boutonSuivant.addActionListener(boutonSuivantPresser);
        boutonSuivant.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,0), "S_pressed");
        boutonSuivant.getActionMap().put("S_pressed", boutonSuivantPresser);
    }

    private static void setBoutonRetour(JButton boutonRetour){
        AbstractAction boutonRetourPresser = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Main.instance.switchTo(Main.instance.mainMenu);
                }catch(Exception error){
                    error.printStackTrace();
                }
            }

        };

        boutonRetour.addActionListener(boutonRetourPresser);
        boutonRetour.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,0), "Z_pressed");
        boutonRetour.getActionMap().put("Z_pressed", boutonRetourPresser);
    }


    public static int getNextLevel(String actualLevel){
        StringBuilder result = new StringBuilder();

        for(int i =0;i<actualLevel.length();i++){
            char test = actualLevel.charAt(i);
            if(test == '0' || test == '1' || test == '2' || test == '3' || test == '4' || test == '5' || test == '6' || test == '7' || test == '8' || test == '9')
                result.append( test );
        }

        int x = Integer.parseInt( result.toString() );
        x++;

        return x;
    }

}
