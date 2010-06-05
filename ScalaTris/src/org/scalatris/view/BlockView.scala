/**
 * 
 */
package org.scalatris.view

import org.myriad.core.TimeStep
import org.myriad.render.Renderable

import org.myriad.core.TimeStep 
import org.myriad.core.TimeUnit
import org.myriad.render._
import org.scalatris.gamestates._
import org.myriad.shared.math._

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector3f;

/**
 * Renders a Block.
 * 
 * @author Tony
 *
 */
class BlockView(blockRenderPosition: Vector2f, cellColor: Vector3f, board: Board, block: Block) extends Renderable {

	
	def update(timeStep: TimeStep) {	  
	}

	def render(renderer: Renderer, camera: Camera, alpha: TimeUnit) {
		var rect = new Rectangle;
	
		var position = block.getPosition;
		var shape = block.getShape;
		
		
		renderer.setAlpha(1f)
		for(y <- 0 until shape.getHeight ) {
			for(x <- 0 until shape.getWidth ) {
				var renderShape = true;
				
				/*
				 * Only the cell if there is something
				 * there
				 */
				if ( shape.get(x, y) == 1 ) {
					
					/*
					 * Check and see if the row has been 
					 * deleted, if so - do not render the cell
					 */
					if ( block.isPlaced ) {
						if ( board.playField(y + position.y)( x + position.x) == 0 ) {
							renderShape = false;
						}
					}
				
					/*
					 * Render the cell
					 */
					if ( renderShape ) {
						
						/* apply the render offset and the position offset */
						rect.setX( blockRenderPosition.x.asInstanceOf[Int] 
						            + position.x * BoardView.CELL_WIDTH + x * BoardView.CELL_WIDTH);
						rect.setY( blockRenderPosition.y.asInstanceOf[Int] 
						            + position.y * BoardView.CELL_HEIGHT + y * BoardView.CELL_HEIGHT);
					
						rect.setWidth(BoardView.CELL_WIDTH);
						rect.setHeight(BoardView.CELL_HEIGHT);
			
						renderer.setColor( cellColor );
						renderer.fillRectangle(rect)
			
						/* Render the cell shading */
						renderer.setColor( Renderer.BLACK );
						rect.setX(blockRenderPosition.x.asInstanceOf[Int] + rect.getX()+1);
						rect.setY(blockRenderPosition.y.asInstanceOf[Int] + rect.getY()+1);
						rect.setWidth(BoardView.CELL_WIDTH-2);
						rect.setHeight(BoardView.CELL_HEIGHT-2);
						renderer.drawRectangle(rect)
					}
				}
			}		
		}
	}

}