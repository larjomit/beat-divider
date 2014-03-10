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

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class BDSequencer implements MetaEventListener {
	private Synthesizer synth = null;
	private Sequencer sequencer = null;

	public BDSequencer() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);

			synth = MidiSystem.getSynthesizer();
			synth.open();

			Receiver synthReceiver = synth.getReceiver();
			Transmitter seqTransmitter = sequencer.getTransmitter();
			seqTransmitter.setReceiver(synthReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSequence(Sequence s) {
		if (sequencer != null) {
			try {
				sequencer.setSequence(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void play(int tempo, int first, int second, int firstCount,
			int secondCount, int clickSound1, int clickSound2,
			boolean[] firstTicks, boolean[] secondTicks) {

		BDSequencerFactory st = BDSequencerFactory.getSequenceFactory();
		st.setTempo(tempo);
		st.setFirst(first);
		st.setSecond(second);
		st.setFirstCount(firstCount);
		st.setSecondCount(secondCount);
		st.setClickSound1(clickSound1);
		st.setClickSound2(clickSound2);
		st.setFirstTicks(firstTicks);
		st.setSecondTicks(secondTicks);
		st.countTempo();
		st.makeSequence();
		try {
			sequencer.setSequence(st.getSequence());
		} catch (Exception e) {
			e.printStackTrace();
		}
		sequencer.start();
	}

	public void stop() {
		sequencer.stop();
		sequencer.setTickPosition(0);
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType() == 1) {
			sequencer.stop();
			sequencer.setTickPosition(0);
			sequencer.start();
		}
	}

	public void closing() {
		System.out.println("Closing operations");
		sequencer.stop();
		sequencer.close();
		synth.close();

		sequencer = null;
		synth = null;
	}
}
