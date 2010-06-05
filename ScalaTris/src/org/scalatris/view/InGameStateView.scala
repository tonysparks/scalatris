/**
 * 
 */
package org.scalatris.view

import org.myriad.render._
import org.myriad.core._
import org.myriad.shared.math._

import org.scalatris.gamestates._
import org.scalatris._
import scala.collection.mutable._

/**
 * The visual representation of the game.
 * 
 * @author Tony
 *
 */
class InGameStateView(gameState: InGameState) extends GameStateView {
	
	/**
	 * The rendering position of the board
	 */
	private val boardRenderPosition = new Vector2f(0,0);

	/*
	 * The board visuals
	 */
	private val boardView = new BoardView(boardRenderPosition, gameState.board);
	
	/**
	 * Score view
	 */
	private val scoreView = new ScoreView(gameState.scoreTracker);
	
	/**
	 * Update this Renderable.  Updates may include moving ahead with animation frames.
	 * 
	 * @param timeStep
	 */
	def update(timeStep: TimeStep) = {
		boardView.update(timeStep);
		scoreView.update(timeStep);
	}
	
	/**
	 * Render this object.
	 * 
	 * @param renderer
	 * @param camera
	 * @param alpha
	 */
	def render(renderer: Renderer, camera:  Camera, alpha: TimeUnit ) = {
		boardView.render(renderer, camera, alpha)
		scoreView.render(renderer, camera, alpha)
		
		if ( gameState.isPaused ) {
			val pausedStr = "P A U S E D";
			val widthOffset = renderer.getDefaultFont().getWidth(pausedStr);
			
			renderer.setColor(Renderer.RED)
			renderer.drawString(pausedStr, renderer.getWindowWidth/2-widthOffset-1, renderer.getWindowHeight/2-1)
			renderer.setColor(Renderer.WHITE)
			renderer.drawString(pausedStr, renderer.getWindowWidth/2-widthOffset, renderer.getWindowHeight/2)
		}
	}
}