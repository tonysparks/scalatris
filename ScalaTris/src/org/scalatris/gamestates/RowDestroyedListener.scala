/**
 * 
 */
package org.scalatris.gamestates


import org.myriad.core.listener._

/**
 * Listens for RowDestroyedEvents.
 * 
 * @author Tony
 *
 */
trait RowDestroyedListener extends EventListener {

	/**
	 * A row was destroyed
	 * @param event
	 */
	@EventMethod
	def onRowDestroyed(event: RowDestroyedEvent);
}