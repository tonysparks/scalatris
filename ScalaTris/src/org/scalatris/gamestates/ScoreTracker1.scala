/**
 * 
 */
package org.scalatris.gamestates

import java.io._;

/**
 * Keeps track of the Players score
 * 
 * @author Tony
 *
 */
class ScoreTracker extends RowDestroyedListener with Persistent {

	/**
	 * The players score
	 */
	private var playersScore = 0;
	
	
	/**
	 * A row was destroyed
	 */
	def onRowDestroyed(event: RowDestroyedEvent) {
		playersScore = playersScore + 100;
	} 
	
	/**
	 * Get the players score
	 * @return
	 */
	def getPlayersScore = playersScore; 
	
	/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput) {
		raf.writeInt(playersScore);
	}
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput) {
		playersScore = raf.readInt;
	}
	
}