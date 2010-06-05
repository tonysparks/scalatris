/**
 * 
 */
package org.scalatris.gamestates

import java.util.Random;
import java.io._;

object Block {	
	val NORTH 	= 1;
	val EAST 	= 2;
	val SOUTH 	= 3;
	val WEST 	= 4;
	
	val COUNTER_CLOCKWISE = -1;
	val CLOCKWISE 		  = 1;
}

/**
 * The Block factory.
 * 
 * @author Tony
 *
 */
object BlockFactory extends Persistent {
	def createI(): Block = {
		new IBlock;
	}
	
	def createL(): Block = {
		new LBlock;
	}
	
	def createJ(): Block = {
		new JBlock;
	}
	
	def createO(): Block = {
		new OBlock;
	}
	
	def createS(): Block = {
		new SBlock;
	}
	
	def createT(): Block = {
		new TBlock;
	}
	
	def createZ(): Block = {
		new ZBlock;
	}
	
	/**
	 * Write out the board
	 * @param raf
	 */
	def write(raf: DataOutput) {
		raf.writeLong(seed);
		raf.writeInt(iterations);
	}
	
	/**
	 * Read from the file
	 * @param raf
	 */
	def read(raf: DataInput) {
		seed = raf.readLong;
		iterations = raf.readInt;
		
		setSeed(seed, iterations);
	}
	
	/**
	 * Set the random seed
	 */
	def setSeed(seed: Long, iteration: Int) {
		rand = new Random(seed);
		for(i <- 0 until iteration ) {
			rand.nextInt(NUMBER_OF_BLOCKS)
		}
		
		this.seed = seed;
	}
	
	/**
	 * Our RNG
	 */
	private var rand : Random = null;
	
	/**
	 * Number of possible blocks
	 */
	private val NUMBER_OF_BLOCKS = 7;
	
	/**
	 * Number of iterations to the random function
	 */
	private var iterations = 0;
	
	/**
	 * The Seed
	 */
	private var seed = 0L;
	
	/**
	 * Create a random block
	 */
	def createRandom() : Block = {		
		if ( rand == null ) {
			setSeed(System.currentTimeMillis, iterations)
		}		
		
		iterations += 1;		
		createById( rand.nextInt(NUMBER_OF_BLOCKS) )					
	}		
	
	/**
	 * Create by Id
	 */
	def createById(id: Int) : Block = {		
		id match {
			case 0 => {
				new IBlock
			}
			case 1 => {
				new LBlock
			}
			case 2 => {
				new JBlock
			}
			case 3 => {
				new OBlock
			}
			case 4 => {
				new SBlock
			}
			case 5 => {
				new TBlock
			}
			case 6 => {
				new ZBlock
			}		
			case _ => null
		}
	}
	
	def getBlockId(block: Block) : Int = {
		block match {
			case IBlock() => {
				0
			}
			case LBlock() => {
				1
			}
			case JBlock() => {
				2
			}
			case OBlock() => {
				3
			}
			case SBlock() => {
				4
			}
			case TBlock() => {
				5
			}
			case ZBlock() => {
				6
			}		
			case _ => {
				-1
			}
		}
	}
}

/**
 * A construction Block
 * 
 * @author Tony
 *
 */
abstract case class Block {


	
		
	/**
	 * The North Shape
	 */
	protected var NORTH_SHAPE: Shape = null;
								/*Array( Array(0,0,0,0)
									  ,Array(0,0,0,0)
									  ,Array(0,0,0,0)
									  ,Array(0,0,0,0) );*/
	
	/**
	 * The East Shape
	 */
	protected var EAST_SHAPE: Shape  = null;
	
	/**
	 * The South Shape
	 */
	protected var SOUTH_SHAPE: Shape = null;
	
	/**
	 * The West Shape
	 */
	protected var WEST_SHAPE: Shape  = null;
	
	/**
	 * Current direction
	 */
	protected var currentDirection = Block.NORTH;
	
	/**
	 * If this block has been placed.
	 */
	private var placed = false;
	
	/**
	 * The position
	 */
	private var position = new BoardPosition;
	
	/**
	 * The velocity
	 */
	private var velocity = new BoardPosition;
	
	/**
	 * Deal with the bounds
	 * @param dir
	 */
	protected def rotate(dir: Int): Int = {
		currentDirection = currentDirection + dir;
		if ( currentDirection > Block.WEST ) {
			currentDirection  = Block.NORTH;
		}
		
		if ( currentDirection < Block.NORTH ) {
			currentDirection  = Block.WEST;
		}
		
		currentDirection 
	}
		
	/**
	 * Init the Block
	 */
	def init();
	init;
	
	/**
	 * Rotate counter clockwise
	 */
	def rotateCounterClockwise() {
		rotate(Block.COUNTER_CLOCKWISE)
	}
	
	/**
	 * Rotate clockwise
	 */
	def rotateClockwise() {
		rotate(Block.CLOCKWISE)
	}
	
	/**
	 * The shape.
	 * 
	 * @return the current shape
	 */
	def getShape()  = {
		currentDirection  match {
			case Block.NORTH => {
				NORTH_SHAPE 
			}
			case Block.EAST => {
				EAST_SHAPE 
			}
			case Block.SOUTH => {
				SOUTH_SHAPE
			}
			case Block.WEST => {
				WEST_SHAPE
			}
		}
	}
	
	/**
	 * Place the block.  By placing a block
	 * it can not move any more.
	 */
	def place() {
		this.placed = true;
	}
	
	/**
	 * If the block is placed
	 * @return
	 */
	def isPlaced() = placed;
	
	def getVelocity() = velocity.normalize;
	
	def setVelocity(x:Int, y:Int) {
		velocity.x = x;
		velocity.y = y;
	}
	
	def addForce(x:Int, y:Int) {
		velocity.x += x;
		velocity.y += y;
	}
	
	def addForce(f: BoardPosition) {
		addForce(f.x, f.y)
	}
	
	/**
	 * The upper left position
	 * @return
	 */
	def getPosition() = position
		
	
	/**
	 * Set the position
	 * @param x
	 * @param y
	 */
	def setPosition(x:Int, y:Int) {
		position.x = x;
		position.y = y;
	}
	
	/**
	 * Set the position
	 * @param p
	 */
	def setPosition(p: BoardPosition) {
		position = p;
	}
	
}

/**
 * The I Block
 * @author Tony
 *
 */
case class IBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {						
		NORTH_SHAPE = new Shape( Array(Array(1)
								  	 , Array(1)
								  	 , Array(1)
								  	 , Array(1)) );
		
		EAST_SHAPE = new Shape( Array( Array(1,1,1,1)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(1)
								  	 , Array(1)
								  	 , Array(1)
								  	 , Array(1)) );
		
		WEST_SHAPE = new Shape( Array( Array(1,1,1,1)) );
	}
}


/**
 * The L Block
 * @author Tony
 *
 */
case class LBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {						
		NORTH_SHAPE = new Shape( Array(Array(1,1)
								  	 , Array(0,1)
								  	 , Array(0,1)) );
		
		EAST_SHAPE = new Shape( Array(Array(0,0,1)
				                     ,Array(1,1,1)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(1,0)
								  	 , Array(1,0)
								  	 , Array(1,1)) );
		
		WEST_SHAPE = new Shape( Array(Array(1,1,1)
				                     ,Array(1,0,0)) );		
	}
}

/**
 * The L Block
 * @author Tony
 *
 */
case class JBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {						
		NORTH_SHAPE = new Shape( Array(Array(1,1)
								  	 , Array(1,0)
								  	 , Array(1,0)) );
		
		EAST_SHAPE = new Shape( Array(Array(1,1,1)
				                     ,Array(0,0,1)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(0,1)
								  	 , Array(0,1)
								  	 , Array(1,1)) );
		
		WEST_SHAPE = new Shape( Array(Array(1,0,0)
				                     ,Array(1,1,1)) );
	}
}

/**
 * The O Block
 * @author Tony
 *
 */
case class OBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {					
		NORTH_SHAPE = new Shape( Array(Array(1,1)
								  	 , Array(1,1)) );
		
		EAST_SHAPE 	= NORTH_SHAPE;		
		SOUTH_SHAPE = NORTH_SHAPE;		
		WEST_SHAPE 	= NORTH_SHAPE;		
	}
}

/**
 * The S Block
 * @author Tony
 *
 */
case class SBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {					
		NORTH_SHAPE = new Shape( Array(Array(1,0)
								  	 , Array(1,1)
								  	 , Array(0,1)) );
		
		EAST_SHAPE = new Shape( Array(Array(0,1,1)
				                     ,Array(1,1,0)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(1,0)
								  	 , Array(1,1)
								  	 , Array(0,1)) );
		
		WEST_SHAPE = new Shape( Array(Array(0,1,1)
				                     ,Array(1,1,0)) );		
	}
}

/**
 * The T Block
 * @author Tony
 *
 */
case class TBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {						
		NORTH_SHAPE = new Shape( Array(Array(1,1,1)
								  	 , Array(0,1,0)) );
		
		EAST_SHAPE = new Shape( Array(Array(1,0)
				                     ,Array(1,1)
				                     ,Array(1,0)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(0,1,0)
								  	 , Array(1,1,1)) );
		
		WEST_SHAPE = new Shape( Array(Array(0,1)
				                     ,Array(1,1)
				                     ,Array(0,1)) );			
	}
}

/**
 * The Z Block
 * @author Tony
 *
 */
case class ZBlock extends Block {
	
	/**
	 * Init the Block
	 */
	def init() = {					
		NORTH_SHAPE = new Shape( Array(Array(0,1)
								  	 , Array(1,1)
								  	 , Array(1,0)) );
		
		EAST_SHAPE = new Shape( Array(Array(1,1,0)
				                     ,Array(0,1,1)) );
		
		SOUTH_SHAPE = new Shape( Array(Array(0,1)
								  	 , Array(1,1)
								  	 , Array(1,0)) );
		
		WEST_SHAPE = new Shape( Array(Array(1,1,0)
				                     ,Array(0,1,1)) );			
	}
}
