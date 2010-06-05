/**
 * 
 */
package org.scalatris.gamestates

/**
 * Shape of the Block.
 * 
 * @author Tony
 *
 */
class Shape(shape: Array[Array[Int]]) {
	def getWidth = shape(0).length;
	def getHeight = shape.length;
	def get(x:Int, y:Int) = shape(y)(x);
}