package triviaGame;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

/**
 * A button that floats: a soft shadow under it, a shaded face, and a thin
 * edge along the bottom and right in a darker shade of its own colour. It
 * lifts higher under the mouse and sinks when pressed. A locked button sits
 * flat with no shadow and no edge.
 *
 * Notes from Cooper:
 *   Cooper, check my work and edit as you see fit. Leave me a note here,
 *   one dated line each (like "20 Nov: ..."), and I will compare it to my
 *   source, then fold it in.
 *   - (none yet)
 *
 * @author Jaims
 */
public class DepthButton extends JButton {

	/**
	 * Marks the version of this class. Private static final: one value for the
	 * whole class that never changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * How round the corners are, in pixels. Private static final: shared by the
	 * whole class, never changed.
	 */
	private static final int ARC = 24;

	/**
	 * Room kept free at the right and bottom so the deepest shadow (6px) still
	 * fits. Private static final: shared by the whole class, never changed.
	 */
	private static final int MARGIN = 7;

	/**
	 * How far the face sinks when pressed. Private static final: shared by the
	 * whole class, never changed.
	 */
	private static final int SINK = 2;

	/**
	 * How thick the darker edge is along the bottom and right, in pixels.
	 * Private static final: shared by the whole class, never changed.
	 */
	private static final int EDGE = 2;

	/**
	 * Makes a button that paints itself. Swing's flat fill and border are
	 * turned off, and rollover is turned on so hovering can lift the button.
	 * Public: GamePanel makes these.
	 */
	public DepthButton() {
		setContentAreaFilled(false);
		setBorderPainted(false);
		setRolloverEnabled(true);
		// no Swing focus ring: it is drawn around the whole button, not the face,
		// and it stays after a click. Hover shows as the shadow growing instead
		setFocusPainted(false);
	}

	/**
	 * Ignores what it is asked and always stays see-through. Otherwise
	 * GamePanel's setOpaque(true) would paint a flat square behind the
	 * rounded face. Public: it overrides JComponent's public method. Void.
	 *
	 * @param isOpaque ignored
	 */
	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(false);
	}

	/**
	 * Draws the shadow and the face, then the text and icon on top.
	 * Protected: Swing calls it when the button needs painting, and it
	 * overrides JComponent's protected method. Void.
	 *
	 * @param g the graphics Swing gives to paint with
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		boolean up = isEnabled();
		boolean down = up && getModel().isPressed();
		boolean hover = up && getModel().isRollover() && !down;

		int fw = getWidth() - MARGIN;
		int fh = getHeight() - MARGIN;
		Color base = getBackground();

		// the shadow goes down first: three see-through layers, each smaller, so the middle is darker than the edge
		if (up) {
			int off = down ? 1 : hover ? 6 : 4;
			g2.setColor(new Color(0, 0, 0, 30));
			for (int i = 0; i < 3; i++) {
				g2.fillRoundRect(off + i, off + i, fw - 2 * i, fh - 2 * i, ARC, ARC);
			}
		}

		// a pressed face, and its words, move down-right so the button sinks
		if (down) {
			g2.translate(SINK, SINK);
		}

		// the edge: a solid darker shade of the button's own colour, nothing see-through,
		// so it never turns muddy. Only a raised button has one; a pressed one sits flush
		int lip = (up && !down) ? EDGE : 0;
		if (lip > 0) {
			g2.setColor(base.darker());
			g2.fillRoundRect(0, 0, fw, fh, ARC, ARC);
		}

		// the face goes on top, smaller by the lip, so the edge shows only at the bottom and right
		if (up) {
			g2.setPaint(new GradientPaint(0, 0, base.brighter(), 0, fh - lip, base));
		} else {
			g2.setColor(base);
		}
		g2.fillRoundRect(0, 0, fw - lip, fh - lip, ARC, ARC);

		// the text and icon go on top, and they move with the face
		super.paintComponent(g2);
		g2.dispose();
	}
}
