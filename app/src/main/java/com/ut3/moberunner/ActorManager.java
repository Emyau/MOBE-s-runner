package com.ut3.moberunner;

import android.graphics.Canvas;
import android.util.Log;

import com.ut3.moberunner.actors.Actor;
import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.actors.Spike;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ActorManager {

    private Random random = new Random();
    private List<Actor> actorList;

    private static final int MAX_ACTOR_ID = 5;
    private static final int MIN_ACTOR_ID = 0;

    public ActorManager(LinkedList<Actor> actorList) {
        this.actorList = actorList;
        generateRandomActorList();
    }

    public void generateRandomActorList() {
        for(int i = 0; i<actorList.size(); i++) {
            Actor randomActor = generateRandomActor();
        }
    }

    public Actor generateRandomActor() {
        int actorTypeId = random.nextInt(MAX_ACTOR_ID);
        switch (actorTypeId) {
            case(0) :
                return new Spike(10,0, 10);
            default:
                return new Spike(10,0, 10);
        }
    }

    public void handleActor(Actor actor, Canvas canvas, Chick chick) {
        actor.nextFrame(canvas);
        if (actor.isCollidingWith(chick)) {
            Log.d("DEV", "Collision");
            chick.paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            chick.setState(Chick.ChickState.DEAD);
        }
        if (actor.getX() > -50)
            despawnActor(actor);
    }

    public void despawnActor(Actor actor) {
        if(actorList.contains(actor)) {
            actorList.remove(actor);
            actorList.add(generateRandomActor());
        }
    }
}
