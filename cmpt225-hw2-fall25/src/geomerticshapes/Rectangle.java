package geomerticshapes;

public class Rectangle extends GeometricShape {

	protected int length;
	protected int width;
	
	/**
	 * 
	 * @param initX
	 * @param initY
	 * @param initL - length
	 * @param initW - width
	 * initX, initY - top left corner of the rectangle
	 */
	public Rectangle(int initX, int initY, int initL,int initW) {
		super(initX, initY);
		length = initL;
		width = initW; 
	}

	public void setLength(int newL)	{
		length = newL;
	}

	public void setWidth(int newW) {
		width= newW;
	}


	public void draw()	{
		// drawing implementation...
	}

	@Override
	public String toString() {
		return "Rectangle: top-left=(" + x + "," + y +"), length = " + length + ", width = " + width;
	}

}
