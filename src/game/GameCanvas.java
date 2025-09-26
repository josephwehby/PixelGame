package game;

import entity.Entity;
import entity.Player;

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

  KeyHandler key_handler = new KeyHandler();
  private Player player;

  int fps = 60;

  public GameCanvas() {
    JFrame frame = new JFrame("Game");
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

    player = new Player(100,100,4,"assets/player.png",key_handler);
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
    player.update();
  }

  private void render() {
    Graphics g = bs.getDrawGraphics();
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.WHITE);
    g2.fillRect(0,0,screen_width,screen_height);

    player.draw(g2);

    g2.dispose();
    bs.show();
  }

}
