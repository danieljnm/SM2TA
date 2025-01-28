package Uppaal;

import danieljnm.sm2ta.StateMachine.State;

@SuppressWarnings("all")
public class CoordinateManager {
  private static int x = 0;

  private static int y = 0;

  public static int increment = 400;

  public static int labelIncrement = 150;

  public static int spacing = 15;

  public static Coordinate next() {
    Coordinate _xblockexpression = null;
    {
      int _x = CoordinateManager.x;
      CoordinateManager.x = (_x + CoordinateManager.increment);
      _xblockexpression = new Coordinate(CoordinateManager.x, CoordinateManager.y);
    }
    return _xblockexpression;
  }

  public static Coordinate next(final State state) {
    return new Coordinate(CoordinateManager.x, CoordinateManager.y);
  }

  public static int reset() {
    int _xblockexpression = (int) 0;
    {
      CoordinateManager.x = 0;
      _xblockexpression = CoordinateManager.y = 0;
    }
    return _xblockexpression;
  }
}
