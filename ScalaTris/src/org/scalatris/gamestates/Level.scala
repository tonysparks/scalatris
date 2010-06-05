/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core._
import org.myriad.core.listener._
import org.scalatris._
import java.io._;

/**
 * A Level represents the current "Level" of the tetris game.
 * @author Tony
 *
 */
class Level(startingLevelNumber:Int, dropSpeedMsec: TimeUnit, eventDispatcher: EventDispatcher) 
	extends RowDestroyedListener with Persistent {	
	
	/**
	 * The current block
	 */
	private var currentBlock: Block = null;
	
	/**
	 * The next Block
	 */
	private var nextBlock: Block = null;
	
	/**
	 * Drop speed - time to drop 1 cell
	 */
	private var dropSpeed = dropSpeedMsec;
	
	/**
	 * Starting level
	 */
	private var levelNumber = startingLevelNumber;
		
	/**
	 * Amount of rows deleted for the current
	 * level
	 */
	private var amountOfRowsDeleted = 0;
	
	/**
	 * Total of rows deleted
	 */
	private var totalAmountOfRowsDeleted = 0;
	
	
	/**
	 * Number of rows to be deleted to advance
	 */
	private val ROWS_TO_NEXT_LEVEL = 10;
	
	/**
	 * Increase the speed by 100 MS
	 */
	private val DROP_RATE = 100;
	
		/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput) {
		raf.writeLong( dropSpeed.getTime );
		raf.writeInt( levelNumber );
		raf.writeInt( amountOfRowsDeleted );
		raf.writeInt( totalAmountOfRowsDeleted );
		
		raf.writeInt( BlockFactory.getBlockId(currentBlock) );
		raf.writeInt( BlockFactory.getBlockId(nextBlock) );
		BlockFactory.write( raf );
	}
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput) {
		dropSpeed = new TimeUnit( raf.readLong );
		levelNumber = raf.readInt;
		amountOfRowsDeleted = raf.readInt;
		totalAmountOfRowsDeleted  = raf.readInt;
		
		currentBlock = BlockFactory.createById( raf.readInt );
		nextBlock = BlockFactory.createById( raf.readInt );
		BlockFactory.read( raf );
	}
	
	/**
	 * Update the Level
	 * 
	 * @param timeStep
	 */
	def update(timeStep: TimeStep) {
		updateBlockStatus();
	}
	
	/**
	 * Update the current and next block statuses
	 */
	def updateBlockStatus() {
		if ( currentBlock == null ) {
			currentBlock = BlockFactory.createRandom();
			currentBlock.setPosition(Board.WIDTH/2, 0);
			
			eventDispatcher.queueEvent(new BlockEvent(this, currentBlock, true))
		}
		
		if ( nextBlock == null ) {
			nextBlock = BlockFactory.createRandom();			
		}
		
		/*
		 * Update the next block
		 */
		if ( currentBlock.isPlaced() ) {
			currentBlock = nextBlock;
			currentBlock.setPosition(Board.WIDTH/2, 0);
			nextBlock = null;
			
			eventDispatcher.queueEvent(new BlockEvent(this, currentBlock, true))
		}
	}
	
	/**
	 * A row was destroyed
	 * @param event
	 */	
	def onRowDestroyed(event: RowDestroyedEvent) {
		amountOfRowsDeleted = amountOfRowsDeleted + 1;
		if ( amountOfRowsDeleted >= ROWS_TO_NEXT_LEVEL) {
			amountOfRowsDeleted = 0;
			TimeUnit.TimeUnitMinus(dropSpeed, 100, dropSpeed ); 
			levelNumber = levelNumber + 1;
		}
		
		totalAmountOfRowsDeleted = totalAmountOfRowsDeleted + 1;
	}
	
	/**
	 * The current block
	 * @return
	 */
	def getCurrentBlock = currentBlock;
	
	/**
	 * The next block
	 * 
	 * @return
	 */
	def getNextBlock = nextBlock;
	
	/**
	 * The Drop Speed
	 * @return
	 */
	def getDropSpeed = dropSpeed;
	
	/**
	 * The level number
	 * @return
	 */
	def getLevelNumber = levelNumber;
}