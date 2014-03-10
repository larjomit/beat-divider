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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class BeatDivider implements ActionListener, WindowListener {

	public static final String PLAY = "play";
	public static final String RESET_TIMER = "resetTimer";

	private BeatDividerGUI gui = null;
	private BDSequencer sequencer = null;

	private boolean playing = false;

	public BeatDivider() {
		sequencer = new BDSequencer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		System.out.println("Created GUI on EDT? "
				+ SwingUtilities.isEventDispatchThread());

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (Exception e2) {
				System.out.println("Couldn't set MetalLookAndFeel");
				e.printStackTrace();
			}
		}

		gui = new BeatDividerGUI(this, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(PLAY)) {

			if (playing) {
				System.out.println("Playing stopped.");
				sequencer.stop();
				gui.timerLabel.stopCounting();
				gui.startButton.setText("PLAY");
				playing = false;
			} else {
				int tempo = ((Integer) gui.tempoSpinner.getValue());
				int firstDiv = ((Integer) gui.div1ComboBox.getSelectedItem());
				int secondDiv = ((Integer) gui.div2ComboBox.getSelectedItem());
				int firstCount = ((Integer) gui.div1CountComboBox
						.getSelectedItem());
				int secondCount = ((Integer) gui.div2CountComboBox
						.getSelectedItem());
				int klik1 = ((Integer) gui.click1SoundSpinner.getValue());
				int klik2 = ((Integer) gui.click2SoundSpinner.getValue());

				// Initialize new divisions only if user given divisions have
				// changed.
				if (!(gui.permDialog.getFirstDivision() == firstDiv && gui.permDialog
						.getSecondDivision() == secondDiv)) {
					gui.permDialog.setDivisions(firstDiv, secondDiv);
				}

				gui.permDialog.setLocationRelativeTo(gui);
				gui.permDialog.setVisible(true);

				boolean[] firstTicks = gui.permDialog.getEkanIskut();
				boolean[] secondTicks = gui.permDialog.getTokanIskut();

				if (firstTicks != null && secondTicks != null) {
					System.out.println("Playing!");
					gui.permutationPanel.setPermutations(firstTicks, secondTicks);
					sequencer.play(tempo, firstDiv, secondDiv, firstCount,
							secondCount, klik1, klik2, firstTicks, secondTicks);
					gui.timerLabel.startCounting();
					gui.startButton.setText("STOP");
					playing = true;
				}
			}
		} else if (e.getActionCommand().equals(RESET_TIMER)) {
			gui.timerLabel.reset();
		} else {
			System.out.println("Unknown action!");
		}

	}

	public static void main(String[] args) {
		new BeatDivider();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Window closing!");
		sequencer.closing();
		gui.timerLabel.closing();
		sequencer = null;
		gui = null;
		System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
