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

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class PermutationLabel extends JLabel {
	static final long serialVersionUID = 1;
	static final Font FONT = new Font("Monospaced", Font.BOLD, 14);
	static final Dimension LABEL_DIM = new Dimension(340, 26);

	public PermutationLabel(boolean[] firstTicks, boolean[] secondTicks) {

		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setPreferredSize(LABEL_DIM);
		this.setFont(FONT);
		this.setHorizontalAlignment(CENTER);
		setPermutations(firstTicks, secondTicks);
	}

	private String makePermutationString(boolean[] ticks) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ticks.length; i++) {
			if (ticks[i])
				sb.append("o");
			else
				sb.append("-");
		}
		return sb.toString();
	}

	public void setPermutations(boolean[] firstTicks, boolean[] secondTicks) {
		String firstPerm = makePermutationString(firstTicks);
		String secondPerm = makePermutationString(secondTicks);
		this.setText(firstPerm + " | " + secondPerm);
	}

}
