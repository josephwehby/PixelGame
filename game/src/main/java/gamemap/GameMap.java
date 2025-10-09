package gamemap;

import entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.gson.Gson;

public class GameMap {
  private String sprite_path = "/landscape.png";
  private String map_path = "/map.json";

  private int rows;
  private int cols;
  private int sprite_rows = 6;
  private int sprite_cols = 8;

  public int maxWorldRow = 50;
  public int maxWorldCol = 50;

  private int scale = 4;
  private int frame_size = 16;
  public int tile_size = scale*frame_size;

  public int worldWidth = tile_size*maxWorldCol;
  public int worldHeight = tile_size*maxWorldRow;
  private int screenWidth, screenHeight;

  private BufferedImage sprite_sheet;
  private BufferedImage[][] frames;

  public MapData mapdata;
  public TileStack[] map;

  public GameMap(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.screenWidth = this.rows*tile_size;
    this.screenHeight = this.cols*tile_size; 
    this.map = new TileStack[maxWorldRow*maxWorldCol];
    for (int i = 0; i < this.map.length; i++) this.map[i] = new TileStack();

    frames = new BufferedImage[sprite_rows][sprite_cols];

    try {
      this.sprite_sheet = ImageIO.read(getClass().getResourceAsStream(sprite_path));
    } catch (IOException e) {
      e.printStackTrace();
      this.sprite_sheet = null;
    }

    readMap();
    parseSheet();
  }

  public void draw(Graphics2D g2, Player player) {

    int startRow = Math.max((player.getY() - player.getScreenY()) / tile_size, 0); 
    int startCol = Math.max((player.getX() - player.getScreenX()) / tile_size, 0); 
    int endRow = Math.min(startRow + rows+1, maxWorldRow);
    int endCol = Math.min(startCol + cols+1, maxWorldCol);

    for (int r = startRow; r < endRow; r++) {
      for (int c = startCol; c < endCol; c++) {
        int screenX = c * tile_size - player.getX() + player.getScreenX();
        int screenY = r * tile_size - player.getY() + player.getScreenY();
        int index = r*maxWorldCol + c;

        int[] tile_ids = map[index].getTileIDs();

        for (int i = tile_ids.length-1; i >= 0; i--) {
          int id = tile_ids[i];
          g2.drawImage(
            frames[id/sprite_cols][id%sprite_cols],
            screenX,
            screenY,
            null
          );
        }
      }
    }
  }

  private void readMap() {
    InputStream in = getClass().getResourceAsStream(map_path);

    if (in == null) {
      throw new RuntimeException("Map file not found");
    }

    Gson gson = new Gson();
    try (InputStreamReader reader = new InputStreamReader(in)) {
      mapdata = gson.fromJson(reader, MapData.class);
    } catch(IOException e) {
      e.printStackTrace();
    }

    for (Layer layer : mapdata.layers) {
      String label = layer.name;
      for (Tile tile : layer.tiles) {
        int index = mapdata.mapWidth*tile.row + tile.col;
        map[index].addTileID(tile.id);
        if (!label.equals("Walkable")) map[index].setSolid();
      }
    }
  }

  private void parseSheet() {
    for (int r = 0; r < sprite_rows; r++) {
      for (int c = 0; c < sprite_cols; c++) {
        BufferedImage sub = sprite_sheet.getSubimage(
          c*frame_size,
          r*frame_size,
          frame_size,
          frame_size
        );

        BufferedImage scaled = new BufferedImage(
          sub.getWidth()*scale, 
          sub.getHeight()*scale, 
          BufferedImage.TYPE_INT_ARGB
        );
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleop = new AffineTransformOp(
          at, 
          AffineTransformOp.TYPE_BILINEAR
        );
        frames[r][c] = scaleop.filter(sub, scaled);
      }
    }
  }
}
