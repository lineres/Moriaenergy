package dev.moriaenergy;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dev.moriaenergy.geometry.*;

public class Parser {
    public static Map parse( String file ) throws Exception {
        Scanner scan = new Scanner(new File("src/main/resources/" + file));
        String str = scan.nextLine();
        String[] options = str.split( " " );
        int H = Integer.parseInt(options[0]);
        int W = Integer.parseInt(options[1]);

        Constructor<?> constructor;
        if(options[2].equals( "S" ))
            constructor = Square.class.getConstructors()[0];
        else
            constructor = Hexagon.class.getConstructors()[0];

        Map map = new Map(H,W);
        for( int i = 0; i < H; i++ ) {
            for( int j = 0; j < W; j++ ) {
                map.array[i][j] = (Cell)
                          constructor.newInstance( null, j, i, new ArrayList<>() );
            }
        }

        int ligneY = 0;
        while(scan.hasNextLine()){
            str = scan.nextLine();
            convertLine(map, str , ligneY, constructor);
            ligneY++;
        }

        map.enableSources();
        map.updateWifi();
        return map;
    }

    public static void convertLine( Map map, String str, int index, Constructor<?> c) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        String[] tab = str.split(" ");
        int counter = 0;
        String tile_str = tab[0];
        List<Integer> rotations = new ArrayList<>();
        for( int i = 1; i < tab.length; i++ ) {
            String s = tab[i];
            switch( s ) {
                case ".","S","W","L" -> {
                    Tile tile = null;
                    if(!tile_str.equals( "." )) {
                        tile = Tile.valueOf( tile_str );
                    }
                    map.array[index][counter] = (Cell)
                              c.newInstance( tile, counter, index, rotations );
                    tile_str = s;
                    rotations = new ArrayList<>();
                    counter++;
                }
                default -> {
                    int val = Integer.parseInt( s );
                    rotations.add( val );
                }
            }
        }

        Tile tile = null;
        if(!tile_str.equals( "." )) {
            tile = Tile.valueOf( tile_str );
        }
        map.array[index][counter] = (Cell)
                  c.newInstance( tile, counter, index, rotations );
    }

}
