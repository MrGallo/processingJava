/* 
 * 11.6: Computer Vision: Motion Detection - Processing Tutorial
 * https://www.youtube.com/watch?v=QLHMtE5XsMs&list=PLRqwX-V7Uu6bw0bVn4M63p8TMJf3OhGy8&index=6
 */
 
import processing.video.*;

Capture video;
color trackColor;
float threshold = 25;

void setup() {
  size(640, 480);
  //String[] cameras = Capture.list();
  //printArray(cameras);
  video = new Capture(this, "name=/dev/video0,size=640x480,fps=30");
  video.start();
  // start off tracking for red
  trackColor = color(255, 0, 0);
}

void captureEvent(Capture video) {
  video.read();
}

void draw() {
  video.loadPixels();
  image(video, 0, 0);
  
  threshold = map(mouseX, 0, width, 0, 100);
  
  if (mousePressed) {
    trackColor = color(video.pixels[mouseX + mouseY * video.width]);  
  }
    
  // XY closest color
  float avgX = 0;
  float avgY = 0;
  
  int count = 0;
  
  // begin loop through every pixel
  for (int x = 0; x < video.width; x++) {
    for (int y = 0; y < video.height; y++) {
      int loc = x + y*video.width;
      // what is current color
      color currentColor = video.pixels[loc];
      float r1 = red(currentColor);
      float g1 = green(currentColor);
      float b1 = blue(currentColor);
      float r2 = red(trackColor);
      float g2 = green(trackColor);
      float b2 = blue(trackColor);
      
      // using euclidian distance to compare colors
      float d = dist(r1, g1, b1, r2, g2, b2);
      if (d < threshold) {
        // turn each discovered point white.
        // poor performance
        //stroke(255);
        //strokeWeight(1);
        //point(x, y);
        
        avgX += x;
        avgY += y;
        count++;
      }
    }
  }
  
  if (count > 0) {
    avgX = avgX / count;
    avgY = avgY / count;
    fill(trackColor);
    strokeWeight(4.0);
    stroke(0);
    ellipse(avgX, avgY, 16, 16);
  }
  
}

