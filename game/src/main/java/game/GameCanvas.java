package game;

import entity.Entity;
import entity.Player;
import gamemap.GameMap;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameCanvas {
  private final int og_tile_size = 16;
  private final int scale = 4;
  private final int tile_size = og_tile_size*scale;

  private final int max_screen_col = 16;
  private final int max_screen_row = 12;
  private final int screen_width = tile_size*max_screen_col;
  private final int screen_height = tile_size*max_screen_row;

  private Canvas canvas;
  private BufferStrategy bs;
  private GameMap map;
  private CollisionHandler collision_handler;
  KeyHandler key_handler = new KeyHandler();
  private Player player;

  int fps = 30;

  public GameCanvas() {
    JFrame frame = new JFrame("Game");
    map = new GameMap(max_screen_row, max_screen_col);
    collision_handler = new CollisionHandler(map);
    canvas = new Canvas();
    canvas.setSize(screen_width, screen_height);
    canvas.addKeyListener(key_handler);
    canvas.setFocusable(true);
    canvas.requestFocus();
    frame.add(canvas);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    canvas.createBufferStrategy(2);
    bs = canvas.getBufferStrategy();

    player = new Player(25*tile_size,20*tile_size,5,screen_width,screen_height,"/player.png",key_handler);
  }

  public void gameLoop() {
    long draw_interval = 1000000000/fps;
    long last_time = System.nanoTime();

    while (true) {
      long current_time = System.nanoTime();
      long elapsed = current_time - last_time;
      last_time = current_time;

      update();
      render();

      long sleep_time = (draw_interval - (System.nanoTime() - current_time))/1000000;
      if (sleep_time > 0) {
        try { 
          Thread.sleep(sleep_time); 
        } catch (InterruptedException e) { 
          e.printStackTrace(); 
        }
      }
    }
  }

  private void update() {
    player.update(collision_handler);
  }

  private void render() {
    Graphics g = bs.getDrawGraphics();
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.BLACK);
    g2.fillRect(0,0,screen_width,screen_height);

    map.draw(g2, player);
    player.draw(g2);

    g2.dispose();
    bs.show();
  }

}
