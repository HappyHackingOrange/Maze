/**
 * AnimatedShape.java
 */
package project;

import java.awt.*;
import javax.swing.JComponent;

/**
 * Start with some kind of interface that describes the basic properties of an
 * animated entity.
 * 
 * @author Vincent Stowbunenko
 *
 */
public interface AnimatedShape {
	public void update();
	public void paint(JComponent parent, Graphics2D g2d);
}
