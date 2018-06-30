/**
 * AnimatedLine.java
 */
package project;

import java.awt.*;

import javax.swing.JComponent;

/**
 * Animate lines (either for building solution path or opening door between two
 * adjacent rooms)
 * 
 * @author Vincent Stowbunenko
 *
 */
public class AnimatedLine extends AbstractAnimatedShape {

	private Color color;

	public AnimatedLine(int x, int y, int width, int height, int direction, Color color) {

		int speed = 12;
		setDirection(direction);
		this.color = color;
		setParentBounds(new Rectangle(x, y, width, height));

		switch (direction) {
		case 0: // north
			setBounds(new Rectangle(x, y + height, width, 0));
			setDx(0);
			setDy(-speed);
			break;
		case 1: // south
			setBounds(new Rectangle(x, y, width, 0));
			setDx(0);
			setDy(speed);
			break;
		case 2: // east
			setBounds(new Rectangle(x, y, 0, height));
			setDx(speed);
			setDy(0);
			break;
		case 3: // west
			setBounds(new Rectangle(x + width, y, 0, height));
			setDx(-speed);
			setDy(0);
			break;
		}

	}

	public Color getColor() {
		return color;
	}

	@Override
	public void paint(JComponent parent, Graphics2D g2d) {
		Rectangle bounds = getBounds();
		g2d.setColor(getColor());
		g2d.fill(bounds);
	}

}
