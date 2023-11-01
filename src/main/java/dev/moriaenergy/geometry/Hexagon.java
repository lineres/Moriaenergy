package dev.moriaenergy.geometry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import dev.moriaenergy.Pair;
import dev.moriaenergy.Utils;

public class Hexagon extends Cell {

	public Hexagon( Tile tile, int x, int y, ArrayList<Integer> rotations ) {
		if(rotations.size() > 6)
			throw new IllegalArgumentException("rotations size should not be more than 5 for hexagons");
		this.tile = tile;
		this.x = x;
		this.y = y;
		this.rotations = rotations;
		update_rotations_images();
	}

	@Override
	public int getMaxNeighbors() {
		return 6;
	}

	@Override
	public String[] getSideString() {
		return new String[] {
			"haut", "haut-droit", "bas-droit", "bas", "bas-gauche", "haut-gauche"
		};
	}

	@Override
	public void paint( Graphics g, int x, int y, int cell_size ) {
		Image bordure = TileMap.HEX.getImage( isEnabled() )
									  .getScaledInstance( cell_size, cell_size, Image.SCALE_SMOOTH );
		g.drawImage( bordure , x, y,null);

		for (Image img : rotations_images) {
			g.drawImage( img, x,y, cell_size, cell_size,null );
		}

		if(tile != null) {
			g.drawImage( TileMap.valueOf( "HEX_" + tile ).getImage( isEnabled() ), x, y, cell_size, cell_size, null );
		}
	}

	@Override
	public void paint( Graphics g, int cell_size ) {
		int x_pos = x*cell_size, y_pos = y*cell_size;
		x_pos -= x*(cell_size/4);
		if(x%2 == 1) {
			y_pos += cell_size/2;
		}
		paint(g, x_pos, y_pos, cell_size);
	}

	@Override
	public void rotate() {
		this.rotations.replaceAll( integer -> ( integer + 1 ) % 6 );
	}

	@Override
	public void update_rotations_images() {
		rotations_images.clear();
		if(rotations.size() == 0) return;

		if(tile != null) {
			for (int r : rotations) {
				Image img = TileMap.HEX_SHORT_LINE.getImage( isEnabled() );
				rotations_images.add(Utils.rotate((BufferedImage) img, r*60));
			}
			return;
		}

		int r1 = rotations.get(0);
		for(int j = 1; j < rotations.size(); j++ ) {
			int r2 = rotations.get(j);
			int angle = 0;
			// lignes en diagonale courte
			if( r1 == (r2+1)%6 || r2 == (r1+1)%6 ) {
				TileMap line_tile = TileMap.HEX_SHORT_DIAG_LINE;
				if( ( r1 == 1 && r2 == 2 ) || ( r1 == 2 && r2 == 1 ) )
					angle = 60;
				else if( ( r1 == 2 && r2 == 3 ) || ( r1 == 3 && r2 == 2 ) )
					angle = 2*60;
				else if( ( r1 == 3 && r2 == 4 ) || ( r1 == 4 && r2 == 3 ) )
					angle = 3*60;
				else if( ( r1 == 4 && r2 == 5 ) || ( r1 == 5 && r2 == 4 ) )
					angle = 4*60;
				else if( ( r1 == 5 && r2 == 0 ) || ( r1 == 0 && r2 == 5 ) )
					angle = 5*60;
				rotations_images.add( Utils.rotate(
						  (BufferedImage) line_tile.getImage(isEnabled()),
						  angle ) );
			}
			// lignes en diagonale longue
			else if( r1 == (r2+2)%6 || r2 == (r1+2)%6 ) {
				TileMap line_tile = TileMap.HEX_LONG_DIAG_LINE;
				if( ( r1 == 1 && r2 == 3 ) || ( r1 == 3 && r2 == 1 ) )
					angle = 60;
				else if( ( r1 == 2 && r2 == 4 ) || ( r1 == 4 && r2 == 2 ) )
					angle = 2*60;
				else if( ( r1 == 3 && r2 == 5 ) || ( r1 == 5 && r2 == 3 ) )
					angle = 3*60;
				else if( ( r1 == 4 && r2 == 0 ) || ( r1 == 0 && r2 == 4 ) )
					angle = 4*60;
				else if( ( r1 == 5 && r2 == 1 ) || ( r1 == 1 && r2 == 5 ) )
					angle = 5*60;
				rotations_images.add( Utils.rotate(
						  (BufferedImage) line_tile.getImage(isEnabled()),
						  angle ) );
			} // lignes droite longue
			else if(r1 == (r2+3)%6 || r2 == (r1+3)%6) {
				TileMap line_tile = TileMap.HEX_LONG_LINE;
				if( ( r1 == 1 && r2 == 4 ) || ( r1 == 4 && r2 == 1 ) )
					angle = 60;
				if( ( r1 == 2 && r2 == 5 ) || ( r1 == 5 && r2 == 2 ) )
					angle = 2*60;
				rotations_images.add( Utils.rotate(
						  (BufferedImage) line_tile.getImage(isEnabled()),
						  angle ) );
			}
		}
	}


	@Override
	public List<Cell> getNeighbors( Map map, List<Integer> rotations ) {
		List<Pair<Integer, Point>> neighborsPositions = new ArrayList<>();
		for( int n : rotations ) {
			int neighbor_x = 0, neighbor_y = 0, neighbor_rotation = -1;
			switch( n ) {
				case 0 -> {
					neighbor_x = x;
					neighbor_y = y - 1;
					neighbor_rotation = 3;
				}
				case 1 -> {
					neighbor_x = x + 1;
					neighbor_y = y - ( x % 2 == 0 ? 1 : 0 );
					neighbor_rotation = 4;
				}
				case 2 -> {
					neighbor_x = x + 1;
					neighbor_y = y + ( x % 2 == 1 ? 1 : 0 );
					neighbor_rotation = 5;
				}
				case 3 -> {
					neighbor_x = x;
					neighbor_y = y + 1;
					neighbor_rotation = 0;
				}
				case 4 -> {
					neighbor_x = x - 1;
					neighbor_y = y + ( x % 2 == 1 ? 1 : 0 );
					neighbor_rotation = 1;
				}
				case 5 -> {
					neighbor_x = x - 1;
					neighbor_y = y - ( x % 2 == 0 ? 1 : 0 );
					neighbor_rotation = 2;
				}
			}
			neighborsPositions.add( new Pair<>(
					  neighbor_rotation, new Point( neighbor_x, neighbor_y ) ) );
		}
		List<Cell> neighbors = new ArrayList<>();
		for(Pair<Integer, Point> position : neighborsPositions) {
			int x = position.snd().x, y = position.snd().y;
			if( map.isInBounds( x, y )
				&& map.array[y][x] != null
				&& map.array[y][x].rotations.contains( position.fst()) ) {
				neighbors.add(map.array[y][x]);
			}
		}
		return neighbors;
	}

	@Override
	public Point centerPoint( int cell_size ) {
		int x_pos = x*cell_size, y_pos = y*cell_size;
		x_pos -= x*(cell_size/4);
		if(x%2 == 1) {
			y_pos += cell_size/2;
		}
		return new Point(x_pos + cell_size/2, y_pos + cell_size/2);
	}

}
