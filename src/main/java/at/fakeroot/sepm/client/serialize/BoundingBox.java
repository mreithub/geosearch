package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class implements a rectangular BoundingBox.
 * @author MK
 */
public class BoundingBox implements IsSerializable
{
	private double x1, y1;
	private double x2, y2;
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public BoundingBox()
	{
	}
	
	/**
	 * Constructor.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public BoundingBox(double x1, double y1, double x2, double y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * Getters for x1, y1, x2, y2. Setters are not required because a bounding box is read-only.
	 * @return
	 */
	public double getX1()
	{
		return(x1);
	}
	
	public double getY1()
	{
		return(y1);
	}
	
	public double getX2()
	{
		return(x2);
	}
	
	public double getY2()
	{
		return(y2);
	}
	
	public String toString() {
		return "x1: "+x1+", y1: "+y1+", x2: "+x2+", y2: "+y2;
	}
}
