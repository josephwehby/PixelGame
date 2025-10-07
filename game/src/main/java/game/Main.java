package game;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
  public static void main(String args[]) {
    GameCanvas game = new GameCanvas();
    game.gameLoop();
  }
}
