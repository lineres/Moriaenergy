package dev.moriaenergy;

import java.io.FileWriter;
import java.io.IOException;

import dev.moriaenergy.geometry.Cell;
import dev.moriaenergy.geometry.Map;

public class Saver {

	public static void save(Map map, String name) {
		if(map == null || name == null)
			return;

		FileWriter writer;
		try {
			writer = new FileWriter("src/main/resources/custom levels/" + name);
		} catch( IOException e ) {
			return;
		}

		StringBuilder to_write = new StringBuilder( map.getH() + " " + map.getW() + " " );
		if(map.array[0][0].getMaxNeighbors() == 4)
			to_write.append( "S\n" );
		else
			to_write.append( "H\n" );

		for (Cell[] cells : map.array) {
		    for(Cell cell : cells) {
				String tile_str = cell.tile == null ? "." : cell.tile.toString();
				StringBuilder rotations_str = new StringBuilder();
				for( int i = 0; i < cell.rotations.size(); i++ ) {
					rotations_str.append( cell.rotations.get( i ) ).append( " " );
				}
				to_write.append( tile_str ).append( " " ).append( rotations_str );
			}
			to_write.append( "\n" );
		}

		try {
			writer.write( to_write.toString() );
			writer.close();
		} catch( IOException ignored ) {}
	}

}
