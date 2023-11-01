package dev.moriaenergy.geometry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings( {"unused"} )
public enum TileMap {

	SQUARE(0,0,120,120),
	SQUARE_W(120,120, 120,120 ),
	SQUARE_L(2*120,120,120,120),
	SQUARE_S(0,4*120,120,120),
	SQUARE_SHORT_LINE(0,2*120, 120,120),
	SQUARE_DIAG_LINE(120,2*120,120,120),
	SQUARE_LONG_LINE(2*120,2*120, 120,120),
	HEX(3*120, 0, 120, 104),
	HEX_W(4*120,120, 120,104),
	HEX_L(5*120,120, 120,104),
	HEX_S(3*120,4*120,120, 104),
	HEX_SHORT_LINE(3*120,2*120, 120,104),
	HEX_SHORT_DIAG_LINE(4*120,2*120,120,104),
	HEX_LONG_DIAG_LINE(5*120,2*120,120,104),
	HEX_LONG_LINE(6*120,2*120, 120,104);

	final int x, y, width, height;

	TileMap( int x, int y, int width, int height ) {
		this.x = x; this.y = y; this.width = width; this.height = height;
	}

	Image getImage(boolean enabled) {
		if(enabled && this != SQUARE_S && this != HEX_S)
			return tileMap.getSubimage( x, y+360, width, height );
		return tileMap.getSubimage( x, y, width, height );
	}

	private static final BufferedImage tileMap;

	static {
		try {
			tileMap = ImageIO.read( new File( "src/main/resources/tuiles.png") );
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}

}
