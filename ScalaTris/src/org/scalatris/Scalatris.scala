/**
 * 
 */
package org.scalatris

import org.myriad.core.ClientKernel;
import org.myriad.core.Console;
import org.myriad.core.CvarRegistryListener;
import org.myriad.core.CvarSystem;
import org.myriad.core.Driver;
import org.myriad.core.Game;
import org.myriad.core.Kernel;
import org.myriad.core.TimeStep;
import org.myriad.core.CvarSystem.Cvar;
import org.myriad.core.exception.MyriadException;
import org.myriad.core.loader.LoaderProperties;
import org.myriad.core.loader.WorldLoader;
import org.myriad.game.entity.DefaultEntityLoader;
import org.myriad.game.entity.Entity;
import org.myriad.game.entity.EntityControllerFactory;
import org.myriad.game.entity.EntityFactory;
import org.myriad.game.entity.EntityLoader;
import org.myriad.game.entity.EntityManager;
import org.myriad.game.entity.ViewportCullStrategy;
import org.myriad.game.fsm.StateFactory;
import org.myriad.input.KeyConverter;
import org.myriad.input.KeyEvent;
import org.myriad.network.Client;
import org.myriad.network.DeliveryType;
import org.myriad.network.Network;
import org.myriad.network.NetworkUpdateListener;
import org.myriad.network.PacketHandler;
import org.myriad.network.Server;
import org.myriad.physics.PhysicsSystem;
import org.myriad.physics._;
import org.myriad.physics.PhysicsObject._;
import org.myriad.render.RenderManagerSystem;
import org.myriad.render.font.FontManager;
import org.myriad.render.font.FontManager.FontType;
import org.myriad.render.scene.SceneLoader;
import org.myriad.render.scene.standard.StandardSceneLoader;
import org.myriad.render.impl._;
import org.myriad.render._;
import org.myriad.shared.ds._;
import org.myriad.script.ScriptEngine;
import org.myriad.shared.Version;
import org.myriad.vfs.VirtualFileSystem;

import org.scalatris.gamestates._
import org.scalatris.view._

/**
 * Main entry point to Scalatris (a Tetris clone)
 * 
 * @author Tony
 *
 */
object Scalatris {

	/**
	 * Entry point
	 */
	def main(args: Array[String]): Unit = {  
		var kernel = new ClientKernel();				
		try {
			var config = ".\\config.cfg";
			
			if ( args.length > 0 ) {
				config = args(0);
			}
					
			kernel.initialize(config);
			kernel.attachGame(new ScalatrisGame());
			kernel.run();
						
		}
		catch {
			case e =>
				e.printStackTrace();
				kernel.printL(e.getClass().getSimpleName() + " - " + e.getMessage());
		}
		finally {
			kernel.shutdown();
		}
	}

	/**
	 * The Tetris game
	 */
	class ScalatrisGame extends Game {
		
		/**
		 * The Driver
		 */
		val DRIVER = new Driver( new Version(1,0,0,0), classOf[ScalatrisGame] );
		
		/**
	     * Initializes a subsystem
	     * 
	     * @param kernel
	     */ 
		def init(kernel: Kernel) = {
			def createCamera() : Camera = {
				var info = new PhysicsInfo;
				info.bound.setBounds(0, 0, 800, 600);
				
				var physObj = kernel.getPhysicsSystem().create(info);
				new Camera2d(new EntDict, physObj);
			}
			
			val eventDispatcher = GameEventDispatcher.eventDispatcher;
			var gameState = new InGameState(eventDispatcher);
			
			addState(gameState, new InGameStateView(gameState))
			
			val renderMng = kernel.getRenderManager();
			renderMng.getCameraManager().addCamera( createCamera );
			
		}
    
	    /**
	     * Get the implementation {@link Driver}.
	     * 
	     * @return
	     */
		def getDriver() : Driver = DRIVER
		

	    /**
	     * Shuts down the system, freeing any resources
	     */ 
	    def shutdown = {
		}
	}
}