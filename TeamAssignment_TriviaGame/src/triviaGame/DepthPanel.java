package triviaGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * A panel that floats: a soft shadow under its bottom-right edge, a
 * rounded face in its background colour, and a thin edge along the bottom
 * and right in a darker shade of that colour. Its children sit on top.
 *
 * Notes from Cooper:
 *   Cooper, check my work and edit as you see fit. Leave me a note here,
 *   one dated line each (like "20 Nov: ..."), and I will compare it to my
 *   source, then fold it in.
 *   - (none yet)
 *
 * @author Jaims
 */
public class DepthPanel extends JPanel {

	/**
	 * Marks the version of this class. Private static final: one value for the
	 * whole class that never changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * How wide the shadow strip is on the bottom and right, in pixels. Private
	 * static final: shared by the whole class, never changed.
	 */
	private static final int SHADOW = 16;

	/**
	 * How round the corners are, in pixels. Private static final: shared by the
	 * whole class, never changed.
	 */
	private static final int ARC = 24;

	/**
	 * How many see-through layers make up the shadow. Private static final:
	 * shared by the whole class, never changed.
	 */
	private static final int LAYERS = 8;

	/**
	 * true = a soft glow all the way round, false = the shadow down-right.
	 * Private static final: shared by the whole class, never changed.
	 */
	private static final boolean GLOW = false;

	/**
	 * How thick the darker edge is along the bottom and right, in pixels.
	 * Private static final: shared by the whole class, never changed.
	 */
	private static final int EDGE = 2;

	/**
	 * Makes a panel that paints its own rounded face. It is see-through so
	 * the corners and the shadow strip show what is behind it. Public:
	 * GamePanel makes these.
	 *
	 * @param layout how the children are arranged
	 */
	public DepthPanel(LayoutManager layout) {
		super(layout);
		super.setOpaque(false);
	}

	/**
	 * Ignores what it is asked and always stays see-through. Otherwise a
	 * caller's setOpaque(true) would paint a flat square behind the rounded
	 * face. Public: it overrides JComponent's public method. Void.
	 *
	 * @param isOpaque ignored
	 */
	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(false);
	}

	/**
	 * Draws the shadow and the face. The children are painted
	 * afterwards by Swing, on top. Protected: Swing calls it when the panel
	 * needs painting, and it overrides JComponent's protected method. Void.
	 *
	 * @param g the graphics Swing gives to paint with
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// first, so the UI delegate does its part; it fills nothing because
		// the panel is not opaque
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int fw = getWidth() - SHADOW;
		int fh = getHeight() - SHADOW;

		if (GLOW) {
			// the glow: the face sits in the middle, and see-through layers of
			// its own colour grow out on every side, brightest near the face
			int inset = SHADOW / 2;
			Color base = getBackground();
			g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 22));
			for (int i = 1; i <= LAYERS; i++) {
				int off = i * inset / LAYERS;
				g2.fillRoundRect(inset - off, inset - off, fw + 2 * off, fh + 2 * off, ARC + off, ARC + off);
			}
			g2.translate(inset, inset);
		} else {
			// the shadow: see-through layers, each pushed further down-right, so
			// it is darkest near the face and fades out at the edge of the strip
			g2.setColor(new Color(0, 0, 0, 32));
			for (int i = 1; i <= LAYERS; i++) {
				int off = i * SHADOW / LAYERS;
				g2.fillRoundRect(off, off, fw, fh, ARC, ARC);
			}
		}

		// the edge: a solid darker shade of the panel's own colour, nothing see-through
		g2.setColor(getBackground().darker());
		g2.fillRoundRect(0, 0, fw, fh, ARC, ARC);

		// the face goes on top, smaller by the edge, so the edge shows only at the bottom and right
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, fw - EDGE, fh - EDGE, ARC, ARC);

		g2.dispose();
	}
}
