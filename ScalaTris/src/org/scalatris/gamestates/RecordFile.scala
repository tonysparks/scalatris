/**
 * 
 */
package org.scalatris.gamestates

import java.io.File;
import java.io.RandomAccessFile;

object RecordFile {
	
	def open {}
}

/**
 * @author Tony
 *
 */
class RecordFile(board: Board, scoreTracker: ScoreTracker, file: File) {
	val raf = new RandomAccessFile(file, "rw" );
	
	def createRecordFile() = {		
		
		/* First persist the players score */
		raf.writeInt(scoreTracker.getPlayersScore);
		
		/* Now persist the playing field */
		for(y <- 0 until Board.HEIGHT ) {
			for(x <- 0 until Board.WIDTH ) {
				raf.writeInt(board.playField(y)(x))
			}
		}
	}
}