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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class BDSequencerFactory {
	public static final int MIN_IN_MICROSECONDS = 60000000;

	private int clickSound1 = 35;
	private int clickSound2 = 56;

	private int tempo;
	private int first;
	private int second;
	private int firstMpq;
	private int secondMpq;
	private int firstCount;
	private int secondCount;

	private Sequence sequence = null;
	private static BDSequencerFactory sequenceFactory = null;

	private boolean[] firstTicks = null;
	private boolean[] secondTicks = null;

	public BDSequencerFactory(int tempo, int first, int second, int klik1,
			int klik2, boolean[] firstTicks, boolean[] secondTicks) {
		this.first = first;
		this.second = second;
		this.tempo = tempo;
		this.clickSound1 = klik1;
		this.clickSound2 = klik2;
		this.firstTicks = firstTicks;
		this.secondTicks = secondTicks;
	}

	public BDSequencerFactory() {
	}

	public static BDSequencerFactory getSequenceFactory() {
		if (sequenceFactory == null)
			sequenceFactory = new BDSequencerFactory();
		return sequenceFactory;
	}

	public void countTempo() {
		int tempoMs = MIN_IN_MICROSECONDS / tempo;
		int firstTempoMs = tempoMs / first;
		int secondTempoMs = tempoMs / second;

		firstMpq = firstTempoMs;
		secondMpq = secondTempoMs;

		// System.out.println("tempMs: " + tempoMs);
		// System.out.println("firstTempoMs: " + firstTempoMs);
		// System.out.println("tempoMs %: " + tempoMs % first);
		// System.out.println("secondTempoMs: " + secondTempoMs);
		// System.out.println("firstTempoMs %: " + tempoMs % second);
	}

	public void makeSequence() {
		try {
			sequence = new Sequence(Sequence.PPQ, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Track track1 = sequence.createTrack();
		Track track2 = sequence.createTrack();

		// Old loop
		/*
		 * for (int i = 0; i < first*2 + second*2; i++) { if (i == 0 || i ==
		 * first || i == first*2 || i == first*2 + second) {
		 * track2.add(createNoteOnEvent(clickSound2, i)); }
		 * 
		 * if ( i < first*2 && firstTicks[i % first] )
		 * track1.add(createNoteOnEvent(clickSound1, i)); else if (
		 * secondTicks[i % second] ) track1.add(createNoteOnEvent(clickSound1,
		 * i)); }
		 */

		int x = 0;
		for (int i = 0; i < first * firstCount + second * secondCount; i++) {

			if (i < first * firstCount) {
				if (i % first == 0) {
					track2.add(createNoteOnEvent(clickSound2, i));
				}
			} else {
				if (x % second == 0) {
					track2.add(createNoteOnEvent(clickSound2, i));
				}
			}

			if (i < first * firstCount) {
				if (firstTicks[i % first]) {
					track1.add(createNoteOnEvent(clickSound1, i));
				}
			} else if (secondTicks[x++ % second]) {
				track1.add(createNoteOnEvent(clickSound1, i));
			}
		}

		final int TEMPO = 0x51;
		long tick1 = 0;
		long tick2 = first * firstCount;

		byte[] data1 = new byte[3];
		byte[] data2 = new byte[3];

		data1[0] = (byte) ((firstMpq >> 16) & 0xFF);
		data1[1] = (byte) ((firstMpq >> 8) & 0xFF);
		data1[2] = (byte) (firstMpq & 0xFF);
		addEvent(track1, TEMPO, data1, tick1);

		data2[0] = (byte) ((secondMpq >> 16) & 0xFF);
		data2[1] = (byte) ((secondMpq >> 8) & 0xFF);
		data2[2] = (byte) (secondMpq & 0xFF);
		addEvent(track1, TEMPO, data2, tick2);

		final int TEXT = 0x01;
		long tick = first * firstCount + second * secondCount;
		String text = "...";
		addEvent(track1, TEXT, text.getBytes(), tick);
	}

	private static MidiEvent createNoteOnEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_ON, nKey, 64, lTick);
	}

	private static MidiEvent createNoteEvent(int nCommand, int nKey,
			int nVelocity, long lTick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(nCommand, 9, nKey, nVelocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		MidiEvent event = new MidiEvent(message, lTick);
		return event;
	}

	private void addEvent(Track track, int type, byte[] data, long tick) {
		MetaMessage message = new MetaMessage();
		try {
			message.setMessage(type, data, data.length);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public int getClickSound1() {
		return clickSound1;
	}

	public void setClickSound1(int clickSound1) {
		this.clickSound1 = clickSound1;
	}

	public int getClickSound2() {
		return clickSound2;
	}

	public void setClickSound2(int clickSound2) {
		this.clickSound2 = clickSound2;
	}

	public boolean[] getFirstTicks() {
		return firstTicks;
	}

	public void setFirstTicks(boolean[] firstTicks) {
		this.firstTicks = firstTicks;
	}

	public boolean[] getSecondTicks() {
		return secondTicks;
	}

	public void setSecondTicks(boolean[] secondTicks) {
		this.secondTicks = secondTicks;
	}

	public int getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(int firstCount) {
		this.firstCount = firstCount;
	}

	public int getSecondCount() {
		return secondCount;
	}

	public void setSecondCount(int secondCount) {
		this.secondCount = secondCount;
	}
}
