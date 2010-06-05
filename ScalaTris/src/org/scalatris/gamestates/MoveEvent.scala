/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core.listener.Event
import java.io._;
/**
 * A request was made for motion.
 * 
 * @author Tony
 *
 */
class MoveEvent(src:Object, moveDir: BoardPosition, rotateDir: Int) 
	extends Event(src) with Persistent {
	
	private var rotationDirection = rotateDir;
	private var creationTime = getCreationTime
	
	def getMovementVelocity() = moveDir;
	def getRotation() = rotationDirection;
	def getEventTime() = creationTime;
	
	/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput) {
		raf.writeInt(moveDir.x);
		raf.writeInt(moveDir.y);
		raf.writeInt(rotationDirection);
		raf.writeLong(creationTime)
	}
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput) {
		moveDir.x = raf.readInt;
		moveDir.y = raf.readInt;
		rotationDirection = raf.readInt;
		creationTime = raf.readLong;
	}
}