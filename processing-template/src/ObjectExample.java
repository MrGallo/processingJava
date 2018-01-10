/***
 * When instantiating an external class to a sketch, you need to pass the instance
 * the parent PApplet context so the class can draw on that context.
 * In Main sketch file instantiate new external class -> new Stripe(this);
 * In class file, set the parent context as a PApplet pointer in the constructor:
 *
 * PApplet parent;
 *
 * Stripe(PApplet p) {
 *     parent = p;
 * }
 *
 * Then draw to the parent context within the class:
 * parent.fill(255, 255, 255);
 * parent.ellipse(x, y, w, h);
 */

import processing.core.PApplet;

public class ObjectExample {

	PApplet parent; // The parent PApplet that we will render ourselves onto

    ObjectExample(PApplet p) {
		parent = p;
	}

	public void draw() {
		parent.fill(0, 0, 255);
		parent.noStroke();
		parent.rect(parent.width/2,parent.width/2, 50, 50);
	}
}
