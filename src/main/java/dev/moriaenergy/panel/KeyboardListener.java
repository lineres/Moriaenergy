package dev.moriaenergy.panel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;

import dev.moriaenergy.LevelCategory;
import dev.moriaenergy.Main;
import dev.moriaenergy.Parser;

public class KeyboardListener {
    

    public static void setupLevelMakerButton(JButton editButton){
        AbstractAction editButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Main.instance.levelMakerPopup();
                }catch(Exception error){
                    error.printStackTrace();
                }
            }

        };
        editButton.addActionListener(editButtonAction);
        editButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                  put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L,0), "L_pressed");
        editButton.getActionMap().put("L_pressed", editButtonAction);
    }


    public static void setupQuitButton(JButton quitButton){
        AbstractAction quitButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.instance.setVisible(false);
                Main.instance.dispose();
            }
        };

        quitButton.addActionListener(quitButtonAction);
        quitButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                  put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,0), "Q_pressed");
        quitButton.getActionMap().put("Q_pressed", quitButtonAction);

    }

    public static void setupEditButton(JButton editButton,JComboBox<String> levelsComboBox,LevelCategory levelCategory){
        AbstractAction editButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Main.instance.switchTo( new LevelMaker(Parser.parse( getSelectedLevelPath(levelsComboBox,levelCategory))));
                }catch(Exception error){
                    error.printStackTrace();
                }
            }

        };
        editButton.addActionListener(editButtonAction);
        editButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                  put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E,0), "E_pressed");
        editButton.getActionMap().put("E_pressed", editButtonAction);
    }

    private static String getSelectedLevelPath(JComboBox<String> levelsComboBox,LevelCategory selectedLevelCategory) {
        String fileName = (String) levelsComboBox.getSelectedItem();
        if( selectedLevelCategory == LevelCategory.OFFICIAL ) {
            fileName = "official levels/level" + fileName + ".nrg";
        } else {
            fileName = "custom levels/" + fileName + ".nrg";
        }
        return fileName;
    }

    public static void setupPlayButton(JButton playButton,JComboBox<String> levelsComboBox,LevelCategory selectedLevelCategory){
        AbstractAction playButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String levelPath = (String) levelsComboBox.getSelectedItem();
                    if(selectedLevelCategory == LevelCategory.OFFICIAL)
                        levelPath = "official levels/level" + levelPath + ".nrg";
                    else
                        levelPath = "custom levels/" + levelPath + ".nrg";
                    Main.instance.switchTo( new LevelPlayer(levelPath) );
                }catch(Exception error){
                    error.printStackTrace();
                }
            }
        };

        playButton.addActionListener(playButtonAction);
        playButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                  put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,0), "P_pressed");
        playButton.getActionMap().put("P_pressed", playButtonAction);
    }

    public static void setupReturn(JButton quitButton, QuittablePanel l){
        AbstractAction quitButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.quit();
            }
        };

        quitButton.addActionListener(quitButtonAction);
        quitButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
                  put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,0), "Q_pressed");
        quitButton.getActionMap().put("Q_pressed", quitButtonAction);

    }

}
