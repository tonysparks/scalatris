/**
 * 
 */
package org.scalatris.gamestates

/**
 * A Board position
 * 
 * @author Tony
 *
 */
class BoardPosition {
	/**
	 * The x location
	 */
	var x = 0;
	
	/**
	 * The y location
	 */
	var y = 0;
	
	/**
	 * Add to this position.
	 * @param p
	 */
	def +(p: BoardPosition): BoardPosition = {
		var newPosition = new BoardPosition;
		newPosition.x = this.x + p.x;
		newPosition.y = this.y + p.y;
		newPosition;		
	}
	
	/**
	 * Get the difference
	 */
	def -(p: BoardPosition): BoardPosition = {
		var newPosition = new BoardPosition;
		newPosition.x = this.x - p.x;
		newPosition.y = this.y - p.y;
		newPosition;		
	}
	
	/**
	 * Unit length of (+/-) 1
	 */
	def normalize: BoardPosition = {
		var newPosition = new BoardPosition;
		if ( this.x != 0 )
			newPosition.x = if ( this.x > 0 ) 1 else -1
		if ( this.y != 0 )
			newPosition.y = if ( this.y > 0 ) 1 else -1
		newPosition;				
	}
	
	
	def createClone(): BoardPosition = {
		var newPosition = new BoardPosition;
		newPosition.x = this.x;
		newPosition.y = this.y;
		newPosition;
	}
		
}