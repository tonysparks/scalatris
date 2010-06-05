/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core._
import org.myriad.game._
import org.myriad.input._
import org.myriad.core.listener._
import org.scalatris._

/**
 * The in game state, captures the game state.
 * 
 * @author Tony
 *
 */
class InGameState(eventDispatcher: EventDispatcher) extends GameState {
			
	/**
	 * The starting level
	 */
	val level = new Level(1, new TimeUnit(1000L), eventDispatcher);
	
	/**
	 * The play field
	 */
	val board = new Board(level, eventDispatcher);
	
	/*
	 * Players score
	 */
	val scoreTracker = new ScoreTracker; 
	
	/**
	 * Pausing the game
	 */
	var isPaused = false;
	
	eventDispatcher.addEventListener(classOf[MoveEvent], board );
	eventDispatcher.addEventListener(classOf[RowDestroyedEvent], scoreTracker );
	eventDispatcher.addEventListener(classOf[RowDestroyedEvent], level );
	
	setName("InGameState");
	
	/**
	 * Enter into the {@link State}.
	 * 
	 * @param stateData
	 */
	def doEnter(stateData: GameStateData) = {		
	}
	
	/**
	 * Update the {@link State}.
	 * 
	 * @param stateData
	 */
	def doUpdate(stateData: GameStateData, timeStep: TimeStep) = {
		if ( ! isPaused ) {
			eventDispatcher.processQueue();
			level.update(timeStep);
			board.update(timeStep);
		}
	}
	
	/**
	 * Exit the {@link State}.
	 * 
	 * @param stateData
	 */
	def doExit(stateData: GameStateData) = {		
	}
	
	 /**
	  * Clean up resources and shutdown
	  */
	 def shutdown() = {		
	 }
	 
	 /**
	 * A key was pressed.
	 * 
	 * @param event
	 */
	def keyPressed(event: KeyEvent) = {		 
		event.getKey match {
			case KeyEvent.MK_LEFT => {
				var v = new BoardPosition;
				v.x = -1;				
				eventDispatcher.queueEvent( new MoveEvent(this, v, 0))
			}
			case KeyEvent.MK_RIGHT => {
				var v = new BoardPosition;
				v.x = 1;				
				eventDispatcher.queueEvent( new MoveEvent(this, v, 0))
			}
			case KeyEvent.MK_UP => {				
				var v = new BoardPosition;				
				eventDispatcher.queueEvent( new MoveEvent(this, v, 1))
			}
			case KeyEvent.MK_DOWN => {
				var v = new BoardPosition;
				v.y = 1;				
				eventDispatcher.queueEvent( new MoveEvent(this, v, 0))
			}		
			case KeyEvent.MK_p => {
				isPaused = ! isPaused;
			}
			case _ => {}
		}
	}
	
	/**
	 * A key was released.
	 * 
	 * @param event
	 */
	def keyReleased(event: KeyEvent) = {		
	}
	
	/**
	 * A mouse button was pressed
	 * @param event
	 */
	def mouseButtonPressed(event: MouseEvent)  = {		
	}
	
	/**
	 * a mouse button was released
	 * @param event
	 */
	def mouseButtonReleased(event: MouseEvent) = {
		
	}
	
	/**
	 * The Mouse has moved.
	 * 
	 * @param event
	 */
	def mouseMotion(event: MouseEvent) = {
		
	}
}