/**
 * Arrow.java
 */
package project;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/**
 * Creates an arrow instance.
 * 
 * @author vxs8122
 *
 */
public class Arrow {

	private int x;
	private int y;
	private int size;
	private int direction;

	private float arrowRatio;
	private float arrowLength;
	private float endX;

	private float veeX;

	private Path2D.Float path;
	private Line2D.Float line;

	private float waisting;

	private float waistX;
	private float waistY;
	private float arrowWidth;

	public Arrow(int x, int y, int size, int direction) {

		this.x = x;
		this.y = y;
		this.size = size;
		this.direction = direction;

		arrowRatio = 0.75f;
		arrowLength = size * 2f / 5;
		endX = size * 7f / 8;

		veeX = endX - size / 20f / arrowRatio;

		path = new Path2D.Float();

		waisting = 0.5f;

		waistX = endX - arrowLength * 0.5f;
		waistY = arrowRatio * arrowLength * 0.5f * waisting;
		arrowWidth = arrowRatio * arrowLength;

		path.moveTo(veeX - arrowLength, -arrowWidth);
		path.quadTo(waistX, -waistY, endX, 0.0f);
		path.quadTo(waistX, waistY, veeX - arrowLength, arrowWidth);

		path.lineTo(veeX - arrowLength * 0.75f, 0.0f);
		path.lineTo(veeX - arrowLength, -arrowWidth);

		line = new Line2D.Float(size / 8f, 0.0f, veeX - arrowLength * 0.5f, 0.0f);

	}

	public void draw(Graphics2D g) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double angle = 0;
		switch (direction) {
		case 0:
		case -1:
			angle = - Math.PI / 2;
			break;
		case 1:
			angle = Math.PI / 2;
			break;
		case 3:
			angle = Math.PI;
			break;
		}

		g.setStroke(new BasicStroke(size / 10f));
		g.translate(x, y + size / 2);
		g.rotate(angle, size / 2, 0);

		g.setColor(Color.BLACK);
		g.fill(path);
		g.draw(line);

		g.rotate(-angle, size / 2, 0);
		g.translate(-x, -y - size / 2);
		g.setStroke(new BasicStroke(1f));

	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame("Random Arrows");
				JPanel panel = new JPanel() {
					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g) {
						int arrowsize = 200;
						new Arrow(0, 0, arrowsize, 0).draw((Graphics2D) g);
						new Arrow(0, arrowsize, arrowsize, 1).draw((Graphics2D) g);
						new Arrow(arrowsize, 0, arrowsize, 2).draw((Graphics2D) g);
						new Arrow(arrowsize, arrowsize, arrowsize, 3).draw((Graphics2D) g);

						int size = 400;
						g.setColor(Color.RED);
						g.drawLine(0, size / 2, size, size / 2);
						g.drawLine(size / 2, 0, size / 2, size);
					}
				};

				panel.setPreferredSize(new Dimension(400, 400));
				frame.add(panel, BorderLayout.CENTER);

				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}
