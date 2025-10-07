package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

  public boolean key_up = false;
  public boolean key_down = false;
  public boolean key_left = false;
  public boolean key_right = false;

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
      key_up = true;
      break;
      case KeyEvent.VK_A:
      key_left = true;
      break;
      case KeyEvent.VK_S:
      key_down = true;
      break;
      case KeyEvent.VK_D:
      key_right = true;
      break;
      default:
      break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
      key_up = false;
      break;
      case KeyEvent.VK_A:
      key_left = false;
      break;
      case KeyEvent.VK_S:
      key_down = false;
      break;
      case KeyEvent.VK_D:
      key_right = false;
      break;
      default:
      break;
    }
  }
}
