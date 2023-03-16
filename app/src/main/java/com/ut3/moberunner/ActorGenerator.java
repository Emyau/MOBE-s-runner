package com.ut3.moberunner;

import android.view.View;

import com.ut3.moberunner.actors.Actor;
import com.ut3.moberunner.actors.Fire;
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

    private static final int MAX_ACTOR_ID = 3;
    private static final int MIN_DELAY = 1500;
    private static final int MAX_DELAY = 3500;

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
            if (actorManager.getActorList().isEmpty()) { //obligé de mettre les deux conditions ?
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
            case (0):
                System.out.println("0, generating Spike");
                return new Spike(10, view.getWidth(), (int) (view.getHeight() * 0.8));
            case (1):
                System.out.println("1, generating Rock");
                return new Rock(10, view.getWidth(), (int) (view.getHeight() * 0.8));
            case (2):
                System.out.println("1, generating Fire");
                return new Fire(10, view.getWidth(), (int) (view.getHeight() * 0.8), view.getContext());
            default:
                System.out.println("Unknown number, generating Spike");
                return new Spike(10, view.getWidth(), (int) (view.getHeight() * 0.8));
        }
    }

    // Toggle actor generation
    public void setGenerating(boolean generating) {
        this.isGenerating = generating;
    }
}
