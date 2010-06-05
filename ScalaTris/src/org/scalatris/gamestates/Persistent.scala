/**
 * 
 */
package org.scalatris.gamestates

import java.io._;

/**
 * Persistent to a file storage 
 * 
 * @author Tony
 *
 */
trait Persistent {

		/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput);
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput);
}