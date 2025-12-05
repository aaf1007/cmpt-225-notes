package geomerticshapes;

// and rectangle is a subclass of GeometricShape
public class Square extends Rectangle {

	public Square(int initX, int initY, int initL) {
		super(initX, initY, initL, initL); // call the constructor of Rectangle
	}
	
	@Override
	public void setLength(int newL)	{
		super.setLength(newL);
		super.setWidth(newL);
	}

	@Override
	public void setWidth(int newL)	{
		super.setLength(newL);
		super.setWidth(newL);
	}

	@Override
	public void draw()	{
		// drawing implementation...
	}

	@Override
	public String toString() {
		return "Square: top-left=(" + x + "," + y +"), length = " + length;
	}

}
