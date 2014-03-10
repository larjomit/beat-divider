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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PermutationDialog extends JDialog implements
		PropertyChangeListener {
	static final long serialVersionUID = 1;
	private int firstDivision;
	private int secondDivision;

	private JRadioButton[] firstButtons;
	private JRadioButton[] secondButtons;

	private static final String READY = "Ready";
	private static final String CANCEL = "Cancel";
	private static final String MESSAGE = "Choose the division ticks to play:";

	private static final Dimension PANEL_SIZE = new Dimension(520, 60);

	private boolean[] firstTicks;
	private boolean[] secondTicks;

	private final JOptionPane optionPane;

	public PermutationDialog(Frame aFrame, int first, int second) {
		super(aFrame, true);
		this.firstDivision = first;
		this.secondDivision = second;

		Object[] array = new Object[] { MESSAGE, makeRadioButtonJPanel() };
		Object[] options = { READY, CANCEL };

		optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

		this.setContentPane(optionPane);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		optionPane.addPropertyChangeListener(this);
	}

	private JPanel makeRadioButtonJPanel() {
		firstButtons = new JRadioButton[firstDivision];
		secondButtons = new JRadioButton[secondDivision];
		JPanel rButtonPanel = new JPanel(
				new FlowLayout(FlowLayout.CENTER, 5, 5));

		for (int i = 0; i < firstButtons.length; i++) {
			firstButtons[i] = new JRadioButton();
			firstButtons[i].setSelected(true);
			rButtonPanel.add(firstButtons[i]);
		}
		rButtonPanel.add(new JLabel("|"));
		for (int i = 0; i < secondButtons.length; i++) {
			secondButtons[i] = new JRadioButton();
			secondButtons[i].setSelected(true);
			rButtonPanel.add(secondButtons[i]);
		}
		rButtonPanel.setPreferredSize(PANEL_SIZE);
		return rButtonPanel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || 
					JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {

			Object value = optionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}
			// Reset the JOptionPane's value.
			// If you don't do this, then if the user
			// presses the same button next time, no
			// property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (READY.equals(value)) {
				firstTicks = new boolean[firstDivision];
				secondTicks = new boolean[secondDivision];
				for (int i = 0; i < firstButtons.length; i++) {
					firstTicks[i] = firstButtons[i].isSelected();
				}
				for (int i = 0; i < secondButtons.length; i++) {
					secondTicks[i] = secondButtons[i].isSelected();
				}

				setVisible(false);
			} else {
				firstTicks = null;
				secondTicks = null;
				setVisible(false);
			}
		}
	}

	public void setDivisions(int first, int second) {
		this.firstDivision = first;
		this.secondDivision = second;
		optionPane.setMessage(new Object[] { MESSAGE, makeRadioButtonJPanel() });
	}

	public boolean[] getEkanIskut() {
		return firstTicks;
	}

	public boolean[] getTokanIskut() {
		return secondTicks;
	}

	public int getFirstDivision() {
		return firstDivision;
	}

	public int getSecondDivision() {
		return secondDivision;
	}

}
