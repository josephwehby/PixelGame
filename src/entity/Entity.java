package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Entity {
  protected int worldX;
  protected int worldY;
  protected int speed;
  protected int current_dir = 0;
  protected int current_frame = 0;
  protected int scale = 4;
  protected BufferedImage sprite_sheet;

  public Entity(int x, int y, int speed, String sprite_path) {
    this.worldX = x;
    this.worldY = y;
    this.speed = speed;
    try {
      this.sprite_sheet = ImageIO.read(new File(sprite_path));
    } catch (IOException e) {
      e.printStackTrace();
      this.sprite_sheet = null;
    }
  }

  public abstract void update();
  protected abstract void parseSheet();
  public abstract void draw(Graphics2D g);
}
