package gamemap;

import java.util.Arrays;

public class TileStack {

  // max number of items on a square is 3 so we can just do this
  private int max_tile_size = 3;
  private int[] tile_ids = new int[max_tile_size];
  private boolean solid = false;
  private int size = 0;

  public void addTileID(int id) {
    if (size >= max_tile_size) return;
    tile_ids[size] = id;
    size++;
  }

  public int[] getTileIDs() {
    return Arrays.copyOf(tile_ids, size);
  }

  public boolean isSolid() {
    return solid;
  }
}
