package Uppaal

import danieljnm.sm2ta.StateMachine.State

class CoordinateManager {
	static int x = 0
	static int y = 0
	public static int increment = 400
	public static int labelIncrement = 150
	public static int spacing = 15
	
	static def next() {
		x += increment
		new Coordinate(x, y)
	}
	
	static def next(State state) {
		x += state.x + increment
		new Coordinate(x, y)
	}
	
	static def reset() {
		x = 0
		y = 0
	}
}