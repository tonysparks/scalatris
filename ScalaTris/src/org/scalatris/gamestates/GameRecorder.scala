/**
 * 
 */
package org.scalatris.gamestates

import scala.collection.mutable.Queue;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Records the game play
 * 
 * @author Tony
 *
 */
class GameRecorder(board: Board) extends MoveListener {

	private var movementQueue = new Queue[MoveEvent];
	private var isRecording = false;
	
	/**
	 * Records the movement event
	 */
	def onMoveEvent(event: MoveEvent) = {
		if ( isRecording ) {
			movementQueue += event;
		}
	}
	
	/**
	 * Start recording the events
	 * 
	 * @param file
	 */
	def startRecording(file: File) {
		
	}
}