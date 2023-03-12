package com.ut3.moberunner;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.ut3.moberunner.actors.Actor;
import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.actors.Fire;
import com.ut3.moberunner.actors.Rock;
import com.ut3.moberunner.actors.Spike;
import com.ut3.moberunner.utils.AccelerationVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/*
    Class used to manage actors : handle their behavior, add them, remove them...
 */
public class ActorManager {

    private Random random = new Random();
    private List<Actor> actorList; // list of active non-chick actors (obstacles)
    private Chick chick;

    public ActorManager(Chick chick) {
        this.actorList = new CopyOnWriteArrayList<Actor>();
        this.chick = chick;
    }

    public void handleActors(Canvas canvas, AccelerationVector accelerationVector, double audioLevel) {
        if(actorList.size() > 0 && !actorList.isEmpty()) {
            for(Actor actor : actorList) {
                handleActor(actor, canvas, accelerationVector, audioLevel);
            }
        }
    }

    // Handle actor movement and collision with chick
    private void handleActor(Actor actor, Canvas canvas, AccelerationVector accelerationVector, double audioLevel) {

        if(actor instanceof Rock){
            Rock rock = (Rock) actor;
            rock.setState(accelerationVector);
            if(rock.getState() == Rock.RockState.DOWN){
                despawnActor(actor);
            }
        }

        if (actor instanceof Fire) {
            Fire fire = (Fire) actor;
            fire.setState(audioLevel);
            if (fire.getState() == Fire.FireState.EXTINGUISH) {
                despawnActor(actor);
            }
        }

        actor.nextFrame(canvas);
        if (actor.isCollidingWith(chick)) {
            Log.d("DEV", "Collision");
            chick.paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            chick.setState(Chick.ChickState.DEAD);
        }
        // if actor is out of the screen, remove it
        if (actor.getX() < 0) {
            despawnActor(actor);
        }
    }

    public void addActor(Actor actor) {
        actorList.add(actor);
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    // Remove all actors from list
    public void clearActors() {
        actorList.clear();
    }

    // Removes an actor from the actorList
    private void despawnActor(Actor actor) {
        if(actorList.contains(actor)) {
            actorList.remove(actor);
        }
    }
}
