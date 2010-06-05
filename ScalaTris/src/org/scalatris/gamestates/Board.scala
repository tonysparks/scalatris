/**
 * 
 */
package org.scalatris.gamestates

import org.scalatris._
import org.myriad.core._
import org.myriad.core.listener._
import java.lang.Math;
import java.io._;
import scala.collection.mutable.Queue;


object Board {
	/**
	 * Field dimensions
	 */
	val WIDTH = 10;
	val HEIGHT = 20;	
}

/**
 * A board represents the playing field of the tetris game.
 * @author Tony
 *
 */
class Board(level: Level, eventDispatcher: EventDispatcher) extends MoveListener with Persistent {

	
	val currentLevel = level;
	private var timeToNextMove = new TimeUnit(0);
	private var movementQueue = new Queue[MoveEvent];

	
	/**
	 * Initialize the play field
	 */
	val playField = new Array[Array[Int]](Board.HEIGHT);
	for( i <- 0 until Board.HEIGHT ) {
		playField(i) = new Array[Int](Board.WIDTH);
	}		

	/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput) {
		raf.writeLong(timeToNextMove.getTime);
		raf.writeInt(movementQueue.size)
		movementQueue.foreach(event => event.write(raf));
		
		for(y <- 0 until Board.HEIGHT ) {
			for(x <- 0 until Board.WIDTH ) {
				raf.writeInt(playField(y)(x))
			}
		}
	}
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput) {
		timeToNextMove.setTime(raf.readLong);
		var size = raf.readInt
		for(i <- 0 until size) {
			var event = new MoveEvent(this, new BoardPosition, 0);
			event.read(raf);
			
			movementQueue += event;
		}
		for(y <- 0 until Board.HEIGHT ) {
			for(x <- 0 until Board.WIDTH ) {
				playField(y)(x) = raf.readInt();
			}
		}
	}
	
	/**
	 * A moveEvent as occurred.
	 * @param event
	 */
	def onMoveEvent(event: MoveEvent) {
		movementQueue += event;
	}

	
	/**
	 * Update the Board
	 * 
	 * @param timeStep
	 */
	def update(timeStep: TimeStep) {		
		def updatePosition(xCollided:Boolean, yCollided:Boolean) {
			var block = currentLevel.getCurrentBlock;			
			val newPosition = block.getPosition + block.getVelocity;
			
			if ( !xCollided )
				block.getPosition.x = newPosition.x
			if ( !yCollided )
				block.getPosition.y = newPosition.y
				
			if ( block.getVelocity.y > 1 || block.getVelocity.x > 1) {
				println("############ HIT ############")
				println(block.getVelocity.x + ", " + block.getVelocity.y)
			}
				
			block.setVelocity(0,0) // clear out the velocity
		}		
		
		var block = currentLevel.getCurrentBlock;
		
		/* First check the users movement 
		 */
		applyMovementEvent
		
		/* Always apply the gravity
		 * if the collision fails on the
		 * gravity check, the tile can't move
		 * anymore.
		 */
		applyGravity(timeStep);
		
		/* Check the collisions */
		var (xToMove, yToMove ) = checkXYCollision(block.getPosition
				                                   , block.getVelocity
				                                   , block.getShape);
		
		if ( yToMove ) {
			/*
			 * We have collided, therefore
			 * place the block
			 */
			block.place;
			finalizeMovement;
		}
		else {
			/*
			 * We can move ahead with the move event
			 */
			updatePosition(xToMove, yToMove);
		}
		
		
		checkForPoints;
	}
	

	/**
	 * Mark the play field with the block data
	 */
	private def finalizeMovement() {
		var currentBlock = currentLevel.getCurrentBlock
		var shape = currentBlock.getShape();
		timeToNextMove.toZero
		scan(currentBlock.getPosition, shape,
				(x, y, rx, ry) => {										
					playField(y)(x) |= shape.get(rx,ry);						
					false
				}				
		)
	}
	
	/**
	 * Destroy rows that can be destroyed.
	 */
	private def checkForPoints() {
		var tally = 0;
		for(y <- 0 until Board.HEIGHT ) {			
			for(x <- 0 until Board.WIDTH ) {
				if (playField(y)(x) == 1) {
					tally += 1;
				}
			}
			
			if ( tally >= Board.WIDTH ) {
				destroyRow(y);
			}
			tally = 0;
			
		}
	}
	
	/**
	 * Destroys a row
	 * @param rowNumber
	 */
	private def destroyRow(rowNumber: Int) {
		for(x <- 0 until Board.WIDTH ) {
			playField(rowNumber)(x) = 0;
		}
		
		var y = rowNumber - 1;
		while(y > -1 ) {
			System.arraycopy(playField(y), 0,playField(y+1),0, playField(y).length)
			y-=1;
		}

		// send a row destroyed event
		eventDispatcher.queueEvent( new RowDestroyedEvent(this, rowNumber));
	}
	
	/**
	 * Apply a movement event
	 */
	private def applyMovementEvent() {
		if ( ! movementQueue.isEmpty ) {
			var moveEvent = movementQueue.dequeue;
			var block = currentLevel.getCurrentBlock
			
			block.addForce(moveEvent.getMovementVelocity);
			
			var rot = moveEvent.getRotation()
			if ( rot > 0 ) {
				block.rotateClockwise;
				
				/* Check the collisions */
				var (xToMove, yToMove ) = checkXYCollision(block.getPosition
						                                   , block.getVelocity
						                                   , block.getShape);
				
				if ( xToMove || yToMove ) {
					block.rotateCounterClockwise()
				}
			}
			else if ( rot < 0 ) {
				block.rotateCounterClockwise;
				
				/* Check the collisions */
				var (xToMove, yToMove ) = checkXYCollision(block.getPosition
						                                   , block.getVelocity
						                                   , block.getShape);
				
				if ( xToMove || yToMove ) {
					block.rotateClockwise()
				}
			}
		}
	}
	
	/**
	 * Apply gravity
	 * 
	 * @param timeStep
	 */
	private def applyGravity(timeStep: TimeStep) {
		/* Add to the Time */
		TimeUnit.TimeUnitPlus(timeToNextMove, timeStep.getElapsedTime, timeToNextMove);
		
		/* If we have reached our time to move, go ahead 
		 * and move to the next position.
		 */
		if ( timeToNextMove.greaterThanEq(currentLevel.getDropSpeed)) {
			currentLevel.getCurrentBlock.addForce(0, 1); /* Drop one cell */	
			timeToNextMove.toZero
		}
	}
	
	/**
	 * Check both collision components.
	 * 
	 * @param position
	 * @param velocity
	 * @param shape
	 * @return
	 */
	private def checkXYCollision(position: BoardPosition
			           , velocity: BoardPosition
			           , shape: Shape): (Boolean, Boolean) = {
	
		var newPosition = position + velocity;
				
		
		var newY = position.createClone;
		newY.y = newPosition.y;
		
		var newX = position.createClone;
		newX.x = newPosition.x;
				
		var xCollide = checkCollision(newX, shape)
		var yCollide = checkCollision(newY, shape)
		
		/* check the two, since the shape may
		 * alter the previous checks
		 */
		if ( ! checkCollision(newPosition, shape) ) {
			xCollide = false;
			yCollide = false;
		}
		
		return (xCollide, yCollide);
	}
	
	/**
	 * Check to see if there is a collision if we move ahead
	 * @param newPosition
	 */	
	private def checkCollision(newPosition: BoardPosition, shape: Shape):Boolean = {
				
		if (newPosition.x + shape.getWidth > Board.WIDTH ||
		    newPosition.x < 0 ) {
			return true;
		}
		
		if (newPosition.y + shape.getHeight > Board.HEIGHT ) {
			return true;
		}
		
		return scan (newPosition
				   , shape
				   , (x, y, rx, ry) => {
							( shape.get(rx,ry) > 0 && playField(y)(x) > 0 );
					})		
	}
	
	/**
	 * Scan the playing field at a particular position.
	 * @param newPosition
	 * @param action
	 * @return
	 */
	private def scan(position: BoardPosition, currentShape: Shape, action: (Int, Int, Int, Int) => Boolean ): Boolean = {
		var rx = 0;
		var ry = 0;
						
		var height = Math.min( (position.y + currentShape.getHeight), Board.HEIGHT )
		var width =  Math.min( (position.x + currentShape.getWidth), Board.WIDTH )
						
		for(y <- position.y until height ) {						
			for(x <- position.x until width ) {		
				if ( action(x,y, rx, ry) ) {					
					return true;
				}
				rx = rx + 1;
			}
			ry = ry + 1;		
			rx = 0;
		}
		
		return false;
	}
}