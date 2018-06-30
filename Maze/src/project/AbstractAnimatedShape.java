/**
 * AbstractAnimatedShape.java
 */
package project;

import java.awt.Rectangle;

/**
 * Implements most common aspects of an animated shape.
 * 
 * @author Vincent Stowbunenko
 *
 */
public abstract class AbstractAnimatedShape implements AnimatedShape {

	private Rectangle bounds, parentBounds;
	private int dx, dy;
	private int direction;

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getParentBounds() {
		return parentBounds;
	}

	public void setParentBounds(Rectangle parentBounds) {
		this.parentBounds = parentBounds;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	@Override
	public void update() {
		Rectangle bounds = getBounds();
		int dx = getDx();
		int dy = getDy();
		switch (direction) {
		case 0: // north
			if (bounds.y <= parentBounds.y)
				setDy(0);
			else {
				if (bounds.y + dy < parentBounds.y) {
					bounds.y = parentBounds.y;
					bounds.height = parentBounds.height;
				} else {
					bounds.y += dy;
					bounds.height -= dy;
				}
			}
			break;
		case 1: // south
			if (bounds.height < parentBounds.height)
				if (bounds.height + dy > parentBounds.height)
					bounds.height = parentBounds.height;
				else
					bounds.height += dy;
			else
				setDy(0);
			break;
		case 2: // east
			if (bounds.width < parentBounds.width)
				if (bounds.width + dx > parentBounds.width)
					bounds.width = parentBounds.width;
				else
					bounds.width += dx;
			else
				setDx(0);
			break;
		case 3: // west
			if (bounds.x <= parentBounds.x)
				setDx(0);
			else {
				if (bounds.x + dx < parentBounds.x) {
					bounds.x = parentBounds.x;
					bounds.width = parentBounds.width;
				} else {
					bounds.x += dx;
					bounds.width -= dx;
				}
			}
			break;
		}
	}

}
