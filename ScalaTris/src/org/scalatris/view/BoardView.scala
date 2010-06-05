/**
 * 
 */
package org.scalatris.view

import org.myriad.core.TimeStep 
import org.myriad.core.TimeUnit
import org.myriad.render._
import org.scalatris.gamestates._
import org.myriad.shared.math._

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector3f;

import org.scalatris.gamestates._
import org.scalatris._
import scala.collection.mutable._

/**
 * Defines global constants
 * 
 * @author Tony
 *
 */
object BoardView {
	
	/**
	 * The width of a CELL	
	 */
	val CELL_WIDTH = 30;
	
	/**
	 * The heigh of a CELL
	 */
	val CELL_HEIGHT = 30;
}

/**
 * @author Tony
 *
 */
class BoardView(boardPosition: Vector2f, board: Board) extends Renderable {

	private var blockViews = new ArrayBuffer[BlockView];
	
	/*
	 * Listen for block creations
	 */
	var eventDispatcher = GameEventDispatcher.eventDispatcher;
	eventDispatcher.addEventListener(classOf[BlockEvent], new BlockListener {
		def onBlockEvent(event: BlockEvent) {
			if ( event.isCreated ) {
				val blockColor = getBlockColor(event.getBlock)
				blockViews += new BlockView(boardPosition, blockColor, board, event.getBlock)
			}
		}
	});
	
	private def getBlockColor(block: Block): Vector3f = {
		block match {
			case b:IBlock => Renderer.BLUE
			case b:LBlock => Renderer.GREEN
			case b:JBlock => Renderer.YELLOW
			case b:OBlock => Renderer.ORANGE
			case b:SBlock => Renderer.VIOLET
			case b:TBlock => Renderer.LITE_GREEN
			case b:ZBlock => Renderer.RED
		}
	}
	
	/**
	 * Updates any animations
	 */
	def update(timeStep: TimeStep) {	
		for( i <- 0 until blockViews.size ) {
			blockViews(i).update(timeStep)
		}		
	}

	/**
	 * Renders
	 */
	def render(renderer: Renderer, camera: Camera, alpha: TimeUnit) {
		var v1 = new Vector2f();
		var v2 = new Vector2f();
				
		renderer.setColor( Renderer.WHITE );
		for(y <- 0 to Board.HEIGHT ) {
			for(x <- 0 to Board.WIDTH ) {
				v1.x = boardPosition.x + x * BoardView.CELL_WIDTH;
				v1.y = boardPosition.y + y;
				
				v2.x = boardPosition.x + x * BoardView.CELL_WIDTH;
				v2.y = boardPosition.y + y * BoardView.CELL_HEIGHT;
				
				renderer.drawLine(v1, v2)
			}
			
			v1.x = boardPosition.x;
			v1.y = boardPosition.y + y * BoardView.CELL_HEIGHT;
				
			v2.x = boardPosition.x + Board.WIDTH * BoardView.CELL_WIDTH;
			v2.y = boardPosition.y + y * BoardView.CELL_HEIGHT;
				
			renderer.drawLine(v1, v2)
		}
		
		renderBlocks(renderer, camera, alpha)
		renderPreview(board.currentLevel.getNextBlock, renderer, camera, alpha)
		// debugRender(renderer)
	}
	
	/**
	 * Render the preview box
	 * @param renderer
	 * @param camera
	 * @param alpha
	 */
	private def renderPreview(nextBlock: Block, renderer: Renderer, camera: Camera, alpha: TimeUnit) {
		if ( nextBlock != null ) {
			val rect = new Rectangle
			val xOffset = boardPosition.x.asInstanceOf[Int] + Board.WIDTH * BoardView.CELL_WIDTH + 20
			val width = 5 * BoardView.CELL_WIDTH
			val height = 5 * BoardView.CELL_HEIGHT
			rect.setX(xOffset)
			rect.setY(10)
			rect.setWidth(width)
			rect.setHeight(height)
			
			renderer.setColor( Renderer.WHITE )
			renderer.drawRectangle(rect);
			
			val xPos = width/2 - (nextBlock.getShape.getWidth * BoardView.CELL_WIDTH)
			val yPos = height/2 - (nextBlock.getShape.getHeight * BoardView.CELL_HEIGHT)
			
			new BlockView(new Vector2f(xOffset + xPos + 20, yPos+40), getBlockColor(nextBlock), board, nextBlock)
				.render(renderer, camera, alpha)
		}
	}
	

	
	/**
	 * Render the blocks
	 * 
	 * @param renderer
	 * @param camera
	 * @param alpha
	 */
	private def renderBlocks(renderer: Renderer, camera:  Camera, alpha: TimeUnit ) = {				
		for( i <- 0 until blockViews.size ) {
			blockViews(i).render(renderer, camera, alpha)
		}
	}
	
	/**
	 * Debug rendering 
	 */
	private def debugRender(renderer: Renderer) {
		var rect = new Rectangle;
				
		renderer.setColor( Renderer.YELLOW );
		for(y <- 0 until Board.HEIGHT ) {
			for(x <- 0 until Board.WIDTH ) {
				if ( board.playField(y)(x) == 1 ) {
					rect.setX(x * BoardView.CELL_WIDTH);
					rect.setY(y * BoardView.CELL_HEIGHT);
				
					rect.setWidth(BoardView.CELL_WIDTH);
					rect.setHeight(BoardView.CELL_HEIGHT);
				
					renderer.fillRectangle(rect)
				}
			}
		}
	}

}