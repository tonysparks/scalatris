/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core.listener.EventListener
import org.myriad.core.listener.EventMethod

/**
 * Listens for movement events
 * 
 * @author Tony
 *
 */
trait MoveListener extends EventListener {

	/**
	 * A moveEvent as occurred.
	 * @param event
	 */
	@EventMethod
	def onMoveEvent(event: MoveEvent)

}