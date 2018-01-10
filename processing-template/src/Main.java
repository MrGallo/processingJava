import processing.core.*;

public class Main extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] {"Main"});
    }

    public void settings() {
        size(400, 200);
    }

    public void setup() {
        // Initialize variables and such
    }

    public void draw() {
        background(255);
        ellipse(50, 50, 50, 50);
    }
}
