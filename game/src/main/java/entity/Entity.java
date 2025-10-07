package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import game.CollisionHandler;

public abstract class Entity {
  protected int worldX;
  protected int worldY;
  protected int speed;
  protected int current_dir = 0;
  protected int current_frame = 0;
  protected int scale = 4;
  protected int frame_size = 16;
  public int tile_size = scale*frame_size;
  protected BufferedImage sprite_sheet;
  protected Rectangle hitBox;

  public Entity(int x, int y, int speed, String sprite_path) {
    this.worldX = x;
    this.worldY = y;
    this.speed = speed;
    try {
      this.sprite_sheet = ImageIO.read(getClass().getResourceAsStream(sprite_path));
    } catch (IOException e) {
      e.printStackTrace();
      this.sprite_sheet = null;
    }
  }

  public Rectangle getHitBox() { return hitBox; }
  public int getX() { return worldX; }
  public int getY() { return worldY; }
  public int getDir() { return current_dir; }

  public abstract void update(CollisionHandler collision);
  protected abstract void parseSheet();
  public abstract void draw(Graphics2D g);
}
