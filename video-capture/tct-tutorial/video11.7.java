/*
 * 11.7: Computer Vision: Blob Detection - Processing Tutorial
 * https://www.youtube.com/watch?v=ce-2l2wRqO8&list=PLRqwX-V7Uu6bw0bVn4M63p8TMJf3OhGy8&index=7
 */

import processing.video.*;

Capture video;
color trackColor;
float threshold = 20;
float distThreshold = 75;

ArrayList<Blob> blobs = new ArrayList<Blob>();

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
  
  blobs.clear();
  threshold = 80;
  
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
      float d = distSq(r1, g1, b1, r2, g2, b2);
      if (d < threshold*threshold) {
        boolean found = false;
        
        for (Blob b : blobs) {
          if (b.isNear(x, y)) {
            b.add(x, y);
            found = true;
            break;
          }
        }
        
        if (!found) {
          Blob b = new Blob(x, y);
          blobs.add(b);
        }
        
      }
      
    }
  }

  for (Blob b : blobs) {
    if (b.size() > 500) {
      b.show();
    }
  }
}

float distSq(float x1, float y1, float x2, float y2) {
  float d = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
  return d;
}

float distSq(float x1, float y1, float z1, float x2, float y2, float z2) {
  float d = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1);
  return d;
}
  
void mousePressed() {
  int loc = mouseX + mouseY * video.width;
  trackColor = video.pixels[loc];
}

void keyPressed() {
  if (key == 'a') {
    distThreshold++;
  } else if (key == 'z') {
    distThreshold--;
  }
  println(distThreshold);
}

