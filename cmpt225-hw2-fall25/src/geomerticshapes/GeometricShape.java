package geomerticshapes;

public abstract class GeometricShape {

	protected int x, y;

	public GeometricShape() {
		x = 0 ;
		y = 0;
	}
	
	/**
	 * 
	 * @param initX
	 * @param initY
	 * a reference point of the shape (e.g., center or top-left)
	 */
	public GeometricShape(int initX, int initY) {
		x = initX;
		y = initY;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	/* instead of a setter */
	public void moveTo(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	public abstract void draw();

}




