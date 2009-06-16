package at.fakeroot.sepm.shared.client.serialize;

import java.io.Serializable;

/**
 * This class implements a rectangular BoundingBox.
 * @author MK
 */
public class BoundingBox implements Serializable
{
	/// default serial version id
	private static final long serialVersionUID = 1L;
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
		//Make sure the the coordinates of (x1, y1) are lower than for the point (x2, y2).
		if (x1 <= x2)
		{
			this.x1 = x1;
			this.x2 = x2;
		}
		else
		{
			this.x1 = x2;
			this.x2 = x1;
		}
		
		if (y1 <= y2)
		{
			this.y1 = y1;
			this.y2 = y2;
		}
		else
		{
			this.y1 = y2;
			this.y2 = y1;
		}
	}
	
	/**
	 * Getter for x1. Setters are not required because a bounding box is read-only.
	 * @return
	 */
	public double getX1()
	{
		return (x1);
	}

	/**
	 * Getter for y1. Setters are not required because a bounding box is read-only.
	 * @return
	 */
	public double getY1()
	{
		return (y1);
	}

	/**
	 * Getter for x2. Setters are not required because a bounding box is read-only.
	 * @return
	 */	
	public double getX2()
	{
		return (x2);
	}
	
	/**
	 * Getter for y2. Setters are not required because a bounding box is read-only.
	 * @return
	 */
	public double getY2()
	{
		return (y2);
	}
	
	public String toString() {
		return "x1: "+x1+", y1: "+y1+", x2: "+x2+", y2: "+y2;
	}
}
