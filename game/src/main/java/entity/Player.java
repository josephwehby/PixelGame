package entity;

import java.awt.*;
import game.KeyHandler;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import game.CollisionHandler;

public class Player extends Entity {

  private KeyHandler key_handler;
  private int row = 4;
  private int col = 4;
  private int frame_counter = 0;
  private int frame_delay = 8;
  private int screenX;
  private int screenY;
  private int width = 0;
  private int height = 0;

  private BufferedImage[][] frames = new BufferedImage[row][col];

  public Player(int x, int y, int speed, int width, int height, String sprite, KeyHandler keys) {
    super(x,y,speed,sprite);
    key_handler = keys;
    this.width = width;
    this.height = height;
    screenX = width/2 - (frame_size*scale)/2;
    screenY = height/2 - (frame_size*scale)/2;
    hitBox = new Rectangle(10, 8, 44, 48);
    parseSheet();
  }

  @Override
  public void update(CollisionHandler collision) {
    int nextX = worldX;
    int nextY = worldY;
    boolean moving = false;

    if (key_handler.key_up) {
      nextY -= speed;
      if (current_dir != 1) {
        current_frame = 0;
        current_dir = 1;
      }     
      moving = true;
    }
    if (key_handler.key_down) {
      nextY += speed;
      if (current_dir != 0) {
        current_frame = 0;
        current_dir = 0;
      } moving = true;
    }
    if (key_handler.key_left) {
      nextX -= speed;
      if (current_dir != 3) {
        current_frame = 0;
        current_dir = 3;
      } moving = true;
    }
    if (key_handler.key_right){
      nextX += speed;
      if (current_dir != 2) {
        current_frame = 0;
        current_dir = 2;
      } moving = true;
    }

    if (!collision.checkCollision(this, nextX, nextY)) {
      worldX = nextX;
      worldY = nextY;
    }

    if (moving) {
      frame_counter++;
      if (frame_counter >= frame_delay) {
        current_frame = (current_frame+1)%4;
        frame_counter = 0;
      }
    } else {
      current_frame = 0;
      frame_counter = 0;
    }
  }

  @Override
  public void draw(Graphics2D g2) {
    g2.drawImage(frames[current_dir][current_frame],screenX,screenY,null);
    g2.setColor(Color.BLUE);
    g2.drawRect(screenX + hitBox.x, screenY+hitBox.y, hitBox.width, hitBox.height);
  }

  @Override
  protected void parseSheet() {
    for (int r = 0; r < row; r++) {
      for (int c = 0; c < col; c++) {
        // scale the image
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

  public int getScreenX() { return screenX; }
  public int getScreenY() { return screenY; }
}
