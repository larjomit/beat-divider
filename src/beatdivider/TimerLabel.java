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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.Timer;

public class TimerLabel extends JLabel implements ActionListener {
	static final long serialVersionUID = 1;
	private int seconds = 0;
	private int minutes = 0;
	private int hours = 0;

	private Timer t = null;

	public TimerLabel() {
		super("" + new Date());
		this.t = new Timer(1000, this);
		setText("0:00:00");
	}

	public void startCounting() {
		t.start();
	}

	public void stopCounting() {
		t.stop();
	}

	public void reset() {
		seconds = 0;
		minutes = 0;
		hours = 0;
		setText("0:00:00");
	}

	public String getTime() {
		return new String(hours + "h " + minutes % 60 + "min " + seconds % 60 + "sec");
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		seconds++;
		if (seconds % 60 == 0)
			minutes++;
		if (seconds % 3600 == 0)
			hours++;

		String min = minutes % 60 < 10 ? "0" + minutes % 60 : "" + minutes % 60;
		String sec = seconds % 60 < 10 ? "0" + seconds % 60 : "" + seconds % 60;
		setText(hours + ":" + min + ":" + sec);
	}

	public void closing() {
		t = null;
	}
}
