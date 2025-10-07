package gamemap;

import entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameMap {
  private String sprite_path = "/landscape.png";
  private String map_path = "/map.txt";

  private int rows;
  private int cols;
  private int sprite_rows = 5;
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
  public int[] map;

  public GameMap(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.screenWidth = this.rows*tile_size;
    this.screenHeight = this.cols*tile_size; 

    frames = new BufferedImage[sprite_rows][sprite_cols];
    map = new int[maxWorldRow*maxWorldCol];

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

        int texture = map[r*maxWorldCol + c];
        int screenX = c * tile_size - player.getX() + player.getScreenX();
        int screenY = r * tile_size - player.getY() + player.getScreenY();

        g2.drawImage(
          frames[texture/sprite_cols][texture%sprite_cols],
          screenX,
          screenY,
          null
        );
      }
    }
  }

  private void readMap() {
    InputStream in = getClass().getResourceAsStream(map_path);

    if (in == null) {
      System.out.println("here");
      throw new RuntimeException("Map file not found");
    }

    int r = 0;
    try (Scanner reader = new Scanner(in)) {
      while (reader.hasNextLine()) {
        String[] line = (reader.nextLine()).split(",");
        for (int c = 0; c < maxWorldCol; c++) {
          map[c + r*maxWorldCol] = Integer.parseInt(line[c]);
        }
        r++;
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
