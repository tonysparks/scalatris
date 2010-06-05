/**
 * 
 */
package org.scalatris.gamestates

import org.myriad.core._
import java.io.File;
/**
 * A command for recording game session
 * 
 * @author Tony
 *
 */
class RecordCommand(console: Console) extends Command(console, "record" ) {
	
	/**
	 * Max number of record files
	 */
	val MAX_RECORDS = 100;
	
	
	
	/**
	 * Execute a function.
	 * 
	 * @param args
	 * @throws MyriadException
	 */
	def execute(args: Array[String]) = {
		
	}
	
		/**
	 * Get the next available image file.
	 * 
	 * @return
	 */
	private def getNextAvailableRecordFile() : File = {
		var recordNumber = 0;
		var fileName="record_%d.rcd";
		
		/*
		 * Find the next available file name
		 */
		while ( recordNumber < MAX_RECORDS ) {
			var name = String.format( fileName, recordNumber.asInstanceOf[Object]);
			var pFile = new File(name);
			if ( ! pFile.exists() ) {
				return (pFile);
			}
			recordNumber += 1;
		}
		
		return (null);
	}
}