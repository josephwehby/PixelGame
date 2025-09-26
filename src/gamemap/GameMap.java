package gamemap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameMap {
  private String sprite_path = "assets/landscape.png";
  private String map_path = "assets/map.txt";

  private int rows;
  private int cols;
  private int sprite_rows = 8;
  private int sprite_cols = 20;

  private int scale = 4;
  private int frame_size = 16;

  private BufferedImage sprite_sheet;
  private BufferedImage[][] frames;
  private int[] map;

  public GameMap(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    frames = new BufferedImage[sprite_rows][sprite_cols];
    map = new int[rows*cols];

    try {
      this.sprite_sheet = ImageIO.read(new File(sprite_path));
    } catch (IOException e) {
      e.printStackTrace();
      this.sprite_sheet = null;
    }

    readMap();
    parseSheet();
  }

  public void draw(Graphics2D g2) {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int texture = map[r*cols + c];
        g2.drawImage(
          frames[texture/sprite_cols][texture%sprite_cols],
          c*(scale*frame_size),
          r*(scale*frame_size),
          null
        );
      }
    }
  }

  private void readMap() {
    File file = new File(map_path);

    int r = 0;
    try (Scanner reader = new Scanner(file)) {
      while (reader.hasNextLine()) {
        String[] line = (reader.nextLine()).split(",");
        for (int c = 0; c < cols; c++) {
          map[c + r*cols] = Integer.parseInt(line[c]);
        }
        r++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
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
