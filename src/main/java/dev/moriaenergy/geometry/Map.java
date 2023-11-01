package dev.moriaenergy.geometry;


public class Map {
    private final int H, W;
    public final Cell[][] array;

    public Map( int H, int W  ){ // Mettre les infos des levels dedans ou dans une class à part ?
        this.H = H;
        this.W = W;
        this.array = new Cell[H][W];
    }

    public void updateWifi() {
        boolean enabled = false;
        for(Cell[] cells : array)
            for(Cell cell : cells)
                if( cell.tile == Tile.W && cell.seekPower( this ) ) {
                    enabled = true;
                    break;
                }
        for(Cell[] cells : array)
            for(Cell cell : cells)
                if(cell.tile == Tile.W)
                    cell.setEnabled( this, enabled );
    }

    public int getW(){return this.W;}

    public int getH(){return this.H;}

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < getW() && y >= 0 && y < getH();
    }

    public boolean victory(){
        boolean foundLamp = false;
        for( Cell[] cells : this.array ) { //on regarde si toutes les lampes sont allumés
            for( Cell cell : cells ) {
                if( cell.tile == Tile.L ) {
                    foundLamp = true;
                    if( !cell.isEnabled() ) {
                        return false;
                    }
                }
            }
        }
        return foundLamp;
    }

    

    public void enableSources() {
        for( Cell[] cells : array) {
            for( Cell cell : cells ) {
                if(cell.tile == Tile.S)
                    cell.setEnabled(this, true );
            }
        }
    }
}