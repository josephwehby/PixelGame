package gamemap;

import com.google.gson.annotations.SerializedName;

public class Tile {
  public int id;
  @SerializedName("y")
  public int row;
  @SerializedName("x")
  public int col;
}
