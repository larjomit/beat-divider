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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

public class BeatDividerGUI extends JFrame {
	static final long serialVersionUID = 1;

	private final String TITTLE = "Beat Divider v1.0";
	private final String TIMER_TITTLE = "Play time";
	private final String SOUND1_TOOLTIP = "MIDI sound for division.";
	private final String SOUND2_TOOLTIP = "MIDI sound for tempo pulse (bpm).";

	static final Dimension TEMPO_SPINNER_DIM = new Dimension(154, 94);
	static final Dimension COMBOBOX_DIM = new Dimension(54, 28);
	static final Dimension RESETBUTTON_SIZE = new Dimension(140, 30);
	static final Dimension STARTBUTTON_SIZE = new Dimension(140, 90);
	static final Dimension TIMER_SIZE = new Dimension(140, 50);

	static final Font TEMPO_FONT = new Font("SansSerif", Font.BOLD, 72);
	static final Font TIMER_FONT = new Font("Courier", Font.BOLD, 30);
	static final Font RESET_FONT = new Font("SansSerif", Font.BOLD, 14);
	static final Font START_FONT = new Font("SansSerif", Font.BOLD, 32);
	static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);

	static final Insets INSETS1 = new Insets(10, 0, 0, 0);
	static final Insets INSETS_NONE = new Insets(0, 0, 0, 0);

	JLabel tempoLabel = new JLabel("Tempo (bpm)");
	JLabel permLabel = new JLabel("Permutations");
	JLabel div1Label = new JLabel("Div 1");
	JLabel div2Label = new JLabel("Div 2");
	JLabel div1CountLabel = new JLabel("Count");
	JLabel div2CountLabel = new JLabel("Count");

	JLabel click1SoundLabel = new JLabel("Sound");
	JLabel click2SoundLabel = new JLabel("Sound");

	JSpinner tempoSpinner = new JSpinner(new SpinnerNumberModel(60, 10, 300, 1));

	// MIDI-percussion instruments: 27-95
	JSpinner click1SoundSpinner = new JSpinner(new SpinnerNumberModel(75, 27, 95, 1));
	JSpinner click2SoundSpinner = new JSpinner(new SpinnerNumberModel(76, 27, 95, 1));

	JComboBox<Integer> div1ComboBox = new JComboBox<Integer>();
	JComboBox<Integer> div2ComboBox = new JComboBox<Integer>();
	JComboBox<Integer> div1CountComboBox = new JComboBox<Integer>();
	JComboBox<Integer> div2CountComboBox = new JComboBox<Integer>();

	JButton startButton = new JButton("PLAY");
	JButton resetButton = new JButton("RESET");

	PermutationLabel permutationPanel = null;
	TimerLabel timerLabel = new TimerLabel();
	PermutationDialog permDialog = null;

	public BeatDividerGUI(ActionListener listener, WindowListener wListener) {

		permDialog = new PermutationDialog(this, 2, 2);
		permDialog.pack();

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		Integer[] kliks = new Integer[19];
		for (int i = 1; i < 20; i++) {
			kliks[i - 1] = new Integer(i);
		}
		DefaultComboBoxModel<Integer> cm = new DefaultComboBoxModel<Integer>(kliks);
		DefaultComboBoxModel<Integer> cm2 = new DefaultComboBoxModel<Integer>(kliks);
		div1ComboBox.setModel(cm);
		div2ComboBox.setModel(cm2);
		div1ComboBox.setSelectedIndex(1);
		div2ComboBox.setSelectedIndex(1);

		Integer[] count = new Integer[100];
		for (int i = 1; i < 101; i++)
			count[i - 1] = new Integer(i);
		DefaultComboBoxModel<Integer> cmCount = new DefaultComboBoxModel<Integer>(count);
		DefaultComboBoxModel<Integer> cmCount2 = new DefaultComboBoxModel<Integer>(count);
		div1CountComboBox.setModel(cmCount);
		div2CountComboBox.setModel(cmCount2);
		div1CountComboBox.setSelectedIndex(1);
		div2CountComboBox.setSelectedIndex(1);

		JPanel backPanel = new BackPanel(gbl);

		permutationPanel = new PermutationLabel(new boolean[] { true, true },
				new boolean[] { true, true });

		backPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		tempoLabel.setFont(LABEL_FONT);
		timerLabel.setFont(TIMER_FONT);
		tempoSpinner.setFont(TEMPO_FONT);
		permLabel.setFont(LABEL_FONT);
		div1Label.setFont(LABEL_FONT);

		div2Label.setFont(LABEL_FONT);
		div1CountLabel.setFont(LABEL_FONT);
		div2CountLabel.setFont(LABEL_FONT);
		click1SoundLabel.setFont(LABEL_FONT);
		click2SoundLabel.setFont(LABEL_FONT);

		tempoSpinner.setPreferredSize(TEMPO_SPINNER_DIM);
		timerLabel.setPreferredSize(TIMER_SIZE);
		div1ComboBox.setPreferredSize(COMBOBOX_DIM);
		div2ComboBox.setPreferredSize(COMBOBOX_DIM);
		div1CountComboBox.setPreferredSize(COMBOBOX_DIM);
		div2CountComboBox.setPreferredSize(COMBOBOX_DIM);
		click1SoundSpinner.setPreferredSize(COMBOBOX_DIM);
		click2SoundSpinner.setPreferredSize(COMBOBOX_DIM);

		// Play/Pause button
//		try {
//			Image img = ImageIO.read(new File(getClass().getResource(
//					"/resources/play.png").toURI()));
//			Image img2 = ImageIO.read(new File(getClass().getResource(
//					"/resources/play2.png").toURI()));
//			Image img3 = ImageIO.read(new File(getClass().getResource(
//					"/resources/play3.png").toURI()));
//
//			startButton.setIcon(new ImageIcon(img));
//			startButton.setRolloverIcon(new ImageIcon(img2));
//			startButton.setPressedIcon(new ImageIcon(img3));
//
//			startButton.setHorizontalTextPosition(JButton.CENTER);
//			startButton.setRolloverEnabled(true);
//			startButton.setFocusPainted(false);
//			startButton.setBorderPainted(false);
//			startButton.setContentAreaFilled(false);
//
//			startButton.setPreferredSize(new Dimension(142, 134));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

		startButton.setPreferredSize(STARTBUTTON_SIZE);
		startButton.setFont(START_FONT);
		resetButton.setPreferredSize(RESETBUTTON_SIZE);
		resetButton.setFont(RESET_FONT);

		TitledBorder bor = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), TIMER_TITTLE);
		bor.setTitleFont(LABEL_FONT);
		timerLabel.setBorder(bor);

		// listenerin settaukset
		startButton.addActionListener(listener);
		startButton.setActionCommand(BeatDivider.PLAY);
		resetButton.addActionListener(listener);
		resetButton.setActionCommand(BeatDivider.RESET_TIMER);

		click1SoundSpinner.setToolTipText(SOUND1_TOOLTIP);
		click2SoundSpinner.setToolTipText(SOUND2_TOOLTIP);

		c.fill = GridBagConstraints.NONE;
		c.insets = INSETS1;
		c.ipady = 0;

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(tempoLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 2;
		gbl.setConstraints(tempoSpinner, c);

		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(timerLabel, c);

		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(resetButton, c);

		c.insets = INSETS1;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(permLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 5;
		c.gridheight = 1;
		gbl.setConstraints(permutationPanel, c);

		c.insets = INSETS1;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div1Label, c);

		c.insets = INSETS_NONE;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div1ComboBox, c);

		c.insets = INSETS1;
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div2Label, c);

		c.insets = INSETS_NONE;
		c.gridx = 2;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div2ComboBox, c);

		c.insets = INSETS1;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div1CountLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div1CountComboBox, c);

		c.insets = INSETS1;
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div2CountLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 2;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(div2CountComboBox, c);

		c.insets = INSETS1;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(click1SoundLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(click1SoundSpinner, c);

		c.insets = INSETS1;
		c.gridx = 2;
		c.gridy = 9;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(click2SoundLabel, c);

		c.insets = INSETS_NONE;
		c.gridx = 2;
		c.gridy = 10;
		c.gridwidth = 2;
		c.gridheight = 1;
		gbl.setConstraints(click2SoundSpinner, c);

		c.gridx = 3;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(startButton, c);

		backPanel.add(tempoLabel);
		backPanel.add(tempoSpinner);
		backPanel.add(timerLabel);
		backPanel.add(resetButton);
		backPanel.add(permLabel);
		backPanel.add(permutationPanel);
		backPanel.add(div1Label);
		backPanel.add(div1ComboBox);
		backPanel.add(div2Label);
		backPanel.add(div2ComboBox);
		backPanel.add(div1CountLabel);
		backPanel.add(div1CountComboBox);
		backPanel.add(div2CountLabel);
		backPanel.add(div2CountComboBox);
		backPanel.add(click1SoundLabel);
		backPanel.add(click1SoundSpinner);
		backPanel.add(click2SoundLabel);
		backPanel.add(click2SoundSpinner);
		backPanel.add(startButton);

		this.getContentPane().add(backPanel);
		this.pack();
		this.setResizable(false);
		this.setTitle(TITTLE);
		this.addWindowListener(wListener);
		this.setVisible(true);
	}
}
