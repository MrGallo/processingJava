/*
 * 11.8: Computer Vision: Improved Blob Detection - Processing Tutorial
 * https://www.youtube.com/watch?v=1scFcY-xMrI&list=PLRqwX-V7Uu6bw0bVn4M63p8TMJf3OhGy8&index=8
 */

class Blob {
  float minX;
  float minY;
  float maxX;
  float maxY;
  
  ArrayList<PVector> points;
    
  Blob(float x, float y) {
    points = new ArrayList();
    minX = x;
    minY = y;
    maxX = x;
    maxY = y;
    points.add(new PVector(x, y));
  }
  
  boolean isNear(float x, float y) {
    float clampX = constrain(x, minX, maxX);
    float clampY = constrain(y, minY, maxY);
    float d = distSq(clampX, clampY, x, y);

    // alternative method, poor performance
    //float d = Integer.MAX_VALUE;
    //for (PVector v : points) {
    //  float tempD = distSq(x, y, v.x, v.y);
    //  if (tempD < d) {
    //    d = tempD;
    //  }
    //}
    
    return d < distThreshold * distThreshold;
  }
  
  void add(float x, float y) {
    points.add(new PVector(x, y));
    minX = min(minX, x);
    minY = min(minY, y);
    
    maxX = max(maxX, x);
    maxY = max(maxY, y);
  }
  
  void show() {
    stroke(0);
    fill(255);
    strokeWeight(2);
    rectMode(CORNERS);
    rect(minX, minY, maxX, maxY);
    
    // poor performance
    //for (PVector v : points) {
    //  stroke(0, 0, 255);
    //  point(v.x, v.y);
    //}
  }
  
  float size() {
    return (maxX-minX)*(maxY-minY);
  }
}

import processing.video.*;

Capture video;
color trackColor;
float threshold = 20;
float distThreshold = 40;

ArrayList<Blob> blobs = new ArrayList<Blob>();

void setup() {
  size(640, 480);
  //String[] cameras = Capture.list();
  //printArray(cameras);
  video = new Capture(this, "name=Microsoft LifeCam Front,size=640x480,fps=30");
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
    distThreshold += 5;
  } else if (key == 'z') {
    distThreshold -= 5;
  }
  println(distThreshold);
}
