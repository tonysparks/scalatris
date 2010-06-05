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

/**
 * @author Tony
 *
 */
class ScoreView(scoreTracker: ScoreTracker) extends Renderable {

	def update(timeStep: TimeStep) {	  
	}

	def render(renderer: Renderer, camera: Camera, alpha: TimeUnit) {  
		renderScore(renderer, camera, alpha)
	}

	/**
	 * Render the score
	 * @param renderer
	 * @param camera
	 * @param alpha
	 */
	private def renderScore(renderer: Renderer, camera:  Camera, alpha: TimeUnit ) = {
		renderer.setColor(Renderer.WHITE)
		renderer.drawString("Score: " + scoreTracker.getPlayersScore, 320, 180)
	}
}