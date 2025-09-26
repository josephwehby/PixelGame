package entity;

import java.awt.*;
import game.KeyHandler;
import java.awt.image.BufferedImage;

public class Player extends Entity {
  private KeyHandler key_handler;
  private int row = 4;
  private int col = 4;
  private int frame_size = 16;

  private BufferedImage[][] frames = new BufferedImage[row][col];

  public Player(int x, int y, int speed, String sprite, KeyHandler keys) {
    super(x,y,speed,sprite);
    key_handler = keys;
    parseSheet();
  }

  @Override
  public void update() {
    if (key_handler.key_up) {
      y -= speed;
      current_dir = 1;
    }
    if (key_handler.key_down) {
      y += speed;
      current_dir = 0;
    }
    if (key_handler.key_left) {
      x -= speed;
      current_dir = 3;
    }
    if (key_handler.key_right){
      x += speed;
      current_dir = 2;
    }
  }

  @Override
  public void draw(Graphics2D g2) {
    g2.drawImage(frames[current_dir][current_frame],x,y,null);
  }

  @Override
  protected void parseSheet() {
    for (int r = 0; r < row; r++) {
      for (int c = 0; c < col; c++) {
        frames[r][c] = sprite_sheet.getSubimage(
          c*frame_size,
          r*frame_size,
          frame_size,
          frame_size
        );
      }
    }
  }
}
