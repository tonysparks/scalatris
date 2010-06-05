/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core.listener.Event

/**
 * A Row was destroyed.
 * 
 * @author Tony
 *
 */
class RowDestroyedEvent(src: Object, rowNumber: Int) extends Event {
	def getRowNumber = rowNumber;
}