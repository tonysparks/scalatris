/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core.listener.Event

/**
 * @author Tony
 *
 */
class BlockEvent(src: Object, block: Block, created:Boolean) extends Event {
	
	def getBlock = block;
	def isCreated = created;
	def isDestroyed = ! created;
}