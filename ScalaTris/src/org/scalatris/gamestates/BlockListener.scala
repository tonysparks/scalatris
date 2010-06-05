/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core.listener._

/**
 * @author Tony
 *
 */
trait BlockListener extends EventListener {

	@EventMethod
	def onBlockEvent(event: BlockEvent);
}