/*
 *   Copyright (c) 2007, 2014 Timo Larjo
 *   
 *   License: GPLv3
 *   
 *   This file is part of BeatDivider.
 *    
 *   BeatDivider is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   BeatDivider is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *   You should have received a copy of the GNU General Public License
 *   along with BeatDivider.  If not, see <http://www.gnu.org/licenses/>.
 */

package beatdivider;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class BackPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	BackPanel(LayoutManager lm) {
		super(lm);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		int width = getWidth();
		int height = getHeight();

		Color color1 = Color.CYAN;
		Color color2 = Color.GRAY;

		GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, width, height);
	}

}
