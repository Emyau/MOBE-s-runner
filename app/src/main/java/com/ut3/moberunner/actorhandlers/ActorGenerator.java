package com.ut3.moberunner.actorhandlers;

import android.util.Log;
import android.view.View;

import com.ut3.moberunner.actors.Actor;
import com.ut3.moberunner.actors.Fire;
import com.ut3.moberunner.actors.Gate;
import com.ut3.moberunner.actors.Rock;
import com.ut3.moberunner.actors.Spike;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
    This class runs in its own thread and generate random actors at random intervals
 */
public class ActorGenerator implements Runnable {

    private final View view;
    private ActorManager actorManager;
    private boolean isGenerating = true;

    private Random random = new Random();

    private static final int MAX_ACTOR_ID = 4;
    private static final int MIN_DELAY = 1500;
    private static final int MAX_DELAY = 3500;
    private static final double HEIGHT_FACTOR = 0.88;

    public ActorGenerator(ActorManager actorManager, View view) {
        this.actorManager = actorManager;
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            generateActors();
        }
    }

    public void generateActors() {
        if (isGenerating) {
            // If there are no actors, generate one right away
            if (actorManager.getActorList().isEmpty()) {
                actorManager.addActor(generateRandomActor());
            }
            // Create a random delay for the next random actor
            long randomDelay = ThreadLocalRandom.current().nextLong(MIN_DELAY, MAX_DELAY);
            try {
                Thread.sleep(randomDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Generate the actor and add it to the manager
            actorManager.addActor(generateRandomActor());
        }
    }

    private Actor generateRandomActor() {
        // Generate a number between 0 and MAX_ACTOR_ID
        int actorTypeId = random.nextInt(MAX_ACTOR_ID);
        // Create an actor based on number
        switch (actorTypeId) {
            case (1):
                Log.d("DEV", "generateRandomActor: Generate Rock");
                return new Rock(10, view.getWidth(), (int) (view.getHeight() * HEIGHT_FACTOR), view.getContext());
            case (2):
                Log.d("DEV", "generateRandomActor: Generate Fire");
                return new Fire(10, view.getWidth(), (int) (view.getHeight() * HEIGHT_FACTOR), view.getContext());
            // TODO : Implement gate with gyro meter
            /**case (3):
                Log.d("DEV", "generateRandomActor: Generate Gate");
                return new Gate(10, view.getWidth(), (int) (view.getHeight() * HEIGHT_FACTOR));**/
            default:
                Log.d("DEV", "generateRandomActor: Generate Spike");
                return new Spike(10, view.getWidth(), (int) (view.getHeight() * HEIGHT_FACTOR), view.getContext());
        }
    }

    // Toggle actor generation
    public void setGenerating(boolean generating) {
        this.isGenerating = generating;
    }
}
