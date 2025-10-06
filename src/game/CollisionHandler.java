package game;

import java.awt.Rectangle;
import entity.Entity;
import gamemap.GameMap;

public class CollisionHandler {
  private GameMap gamemap;

  public CollisionHandler(GameMap gamemap) {
    this.gamemap = gamemap;
  }

  public boolean checkCollision(Entity entity, int worldX, int worldY) {
    Rectangle hitBox = entity.getHitBox();
    int current_dir = entity.getDir();

    int leftWorldX = worldX + hitBox.x;
    int rightWorldX = worldX + hitBox.x + hitBox.width;
    int topWorldY = worldY + hitBox.y;
    int bottomWorldY = worldY + hitBox.y + hitBox.height;


    int leftCol = leftWorldX / gamemap.tile_size;
    int rightCol = rightWorldX / gamemap.tile_size;
    int topRow = topWorldY / gamemap.tile_size;
    int bottomRow = bottomWorldY / gamemap.tile_size;

    int tile1 = 0, tile2 = 0;

    switch(current_dir) {
      case 0:
      tile1 = gamemap.map[bottomRow * gamemap.maxWorldRow + leftCol];
      tile2 = gamemap.map[bottomRow * gamemap.maxWorldRow + rightCol];
      break;
      case 1:
      tile1 = gamemap.map[topRow * gamemap.maxWorldRow + leftCol];
      tile2 = gamemap.map[topRow * gamemap.maxWorldRow + rightCol];
      break;
      case 2:
      tile1 = gamemap.map[bottomRow * gamemap.maxWorldRow + rightCol];
      tile2 = gamemap.map[topRow * gamemap.maxWorldRow + rightCol];
      break;
      case 3:
      tile1 = gamemap.map[bottomRow * gamemap.maxWorldRow + leftCol];
      tile2 = gamemap.map[topRow * gamemap.maxWorldRow + leftCol];
      break;
      default:
      break;
    }
    if (tile1 != 27 || tile2 != 27) {
      System.out.println("wall");
    }
    return (tile1 != 27 || tile2 != 27);
  }
}
