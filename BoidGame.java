package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import static android.content.ContentValues.TAG;

/**
 * Created by anderstimo1 on 8/03/2018.
 */

public class BoidGame extends GameThread{

    ArrayList<Boid> boidList;

    //Constructor
    public BoidGame(GameView gameView) {
        //House keeping
        super(gameView);
        boidList = new ArrayList<Boid>();

        //initialise the list of boids
        //all initialise with random speeds and directions
        for (int i = 0; i < 5; i++) {
            Boid boid = new Boid (BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.small_red_ball),
                    0, 0, (float)(35 + Math.random() * 70) , (float)(35 + Math.random() * 70));
            boidList.add(boid);
        }
    }

    //initial setup of the "world"
    public void setupBeginning(){
        //loop through all of the boids in the list
        for (int i = 0; i < boidList.size(); i++) {
            //generate random positions
            float x = (float)(Math.random() * mCanvasWidth/2);
            float y = (float)(Math.random() * mCanvasHeight/2);

            //set the position
            boidList.get(i).setX(x);
            boidList.get(i).setY(y);
        }

    }

    //setup the canvas with the boids
    protected void doDraw(Canvas canvas) {
        Boid thisBoid;
        //checks if the canvas exists
        if (canvas != null) {
            //if it does...
            super.doDraw(canvas);
            //loop through the list of boids
            for (int i = 0; i < boidList.size(); i++) {
                thisBoid = boidList.get(i);
                //draw the boid on the canvas
                canvas.drawBitmap(thisBoid.getGraphic(), thisBoid.getX() - thisBoid.getGraphic().getWidth() / 2,
                        thisBoid.getY() - thisBoid.getGraphic().getHeight() / 2, null);
            }
        }
    }


//    protected void actionOnTouch(float x, float y) {
//        Boid thisBoid;
//
//        for (int i = 0; i < boidList.size(); i++) {
//            thisBoid = boidList.get(i);
//
//            thisBoid.setXSpd(x - thisBoid.getX());
//            thisBoid.setYSpd(y - thisBoid.getY());
//        }
//    }

    //this method is called every "tick" of the game engine
    protected void updateGame(float secondsElapsed) {
        //create variables for the method
        Boid thisBoid;
        ArrayList<Float> schooling = new ArrayList<Float>();
        ArrayList<Float> separation = new ArrayList<Float>();
        ArrayList<Float> alignment = new ArrayList<Float>();

        float xSpd;
        float ySpd;

        for (int i = 0; i < boidList.size(); i++) {
            //get the current boid
            thisBoid = boidList.get(i);

            //set speeds to 0 to remove previous iterations speed
            xSpd = 0;
            ySpd = 0;

            //get the speeds from the boid behaviour rules
            schooling = thisBoid.school(boidList);
            separation = thisBoid.separate(boidList);
            alignment = thisBoid.align(boidList);

            //set the speed of the boid
            //the speed should be the sum of the speeds generated by all of the behaviours AND the current speed
            //0 is the x value, 1 is the y value
            xSpd = thisBoid.getXSpd() + schooling.get(0) + separation.get(0) + alignment.get(0);
            ySpd = thisBoid.getYSpd() + schooling.get(1) + separation.get(1) + alignment.get(1);

            thisBoid.setXSpd(xSpd);
            thisBoid.setYSpd(ySpd);
        }

        //update the positions of the boids
        for (int i = 0; i < boidList.size(); i++) {
            thisBoid = boidList.get(i);
            thisBoid.setX(thisBoid.getX() + (secondsElapsed*thisBoid.getXSpd()));
            thisBoid.setY(thisBoid.getY() + (secondsElapsed*thisBoid.getYSpd()));
        }

//        Log.d(TAG, "updateGame: called");
    }
}