package com.mycompany.a1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.codename1.charts.models.Point;
import com.mycompany.a1.GameObjects.*;
import com.mycompany.a1.GameObjects.Fixed.*;
import com.mycompany.a1.GameObjects.Movable.*;

public class GameWorld {
    private int lives;
    private int clock;
    private int winClock;
    private int numOfFlags;
    private int numOfFoodStations;

    // Hash Keys
    /**
     * private String flagTag = "Flag";
     */
    private String flagTag = "Flag";
    /**
     * private String antTag = "Ant";
     */
    private String antTag = "Ant";
    /**
     * private String spiderTag = "Spider";
     */
    private String spiderTag = "Spider";
    /***
     * private String foodStationTag = "FoodStation";
     */
    private String foodStationTag = "FoodStation";

    Hashtable<String, GameObject> gameObjects = new Hashtable<String, GameObject>();

    // Init Methods

    public void init() {
        this.lives = 3;
        this.clock = 0;
        this.initObjects();
    }

    public void initObjects() {
        this.gameObjects.put("Flag1", new Flag(new Point(50.0f, 50.0f)));
        this.gameObjects.put("Flag2", new Flag(new Point(350.0f, 550.0f)));
        this.gameObjects.put("Flag3", new Flag(new Point(400.0f, 900.0f)));
        this.gameObjects.put("Flag4", new Flag(new Point(700.0f, 200.0f)));
        this.gameObjects.get("Flag1").setName("Flag1");
        this.gameObjects.get("Flag2").setName("Flag2");
        this.gameObjects.get("Flag3").setName("Flag3");
        this.gameObjects.get("Flag4").setName("Flag4");

        this.numOfFlags = 4;

        this.gameObjects.put(antTag, new Ant(gameObjects.get("Flag1").getLocation()));
        this.gameObjects.get(antTag).setName(antTag);

        for (int i = 1; i < 3; i++) {
            this.gameObjects.put((spiderTag + i), new Spider());
            this.gameObjects.get((spiderTag + i)).setName((spiderTag + i));
        }

        for (int i = 1; i < 5; i++) {
            this.gameObjects.put((foodStationTag + i), new FoodStation());
            this.gameObjects.get((foodStationTag + i)).setName((foodStationTag + i));
        }
    }

    public void restartInitObjects() {
        this.lives -= 1;
        this.gameObjects.put("Flag1", new Flag(new Point(50.0f, 50.0f)));
        this.gameObjects.put("Flag2", new Flag(new Point(350.0f, 550.0f)));
        this.gameObjects.put("Flag3", new Flag(new Point(400.0f, 900.0f)));
        this.gameObjects.put("Flag4", new Flag(new Point(700.0f, 200.0f)));
        this.gameObjects.get("Flag1").setName("Flag1");
        this.gameObjects.get("Flag2").setName("Flag2");
        this.gameObjects.get("Flag3").setName("Flag3");
        this.gameObjects.get("Flag4").setName("Flag4");

        this.numOfFlags = 4;

        this.gameObjects.put(antTag, new Ant(gameObjects.get("Flag1").getLocation()));

        for (int i = 1; i < 3; i++) {
            this.gameObjects.put((spiderTag + i), new Spider());
        }
    }

    // word states

    /**
     * Bottom Left position
     * Min position for bottom and left
     * 
     * @return bottom left pos
     */
    public static Point getWorldMin() {
        return new Point(0.0f, 0.0f);
    }

    /**
     * Top Right Border
     * Max position for top and right
     * 
     * @return Top Right Position
     */
    public static Point getWorldMax() {
        return new Point(1000.0f, 1000.0f);
    }

    public boolean winCondition() {
        return ((Ant) this.gameObjects.get(antTag)).getLastFlag() == this.numOfFlags;
    }

    // Game states

    public boolean looseCondition() {
        return this.lives == 0;
    }

    public boolean restartCondition() {
        return ((Ant) this.gameObjects.get(antTag)).isAntDead();
    }

    public void win() {
        System.out.println(("\nGame over, you win!\n" +
                "Total time: " + winClock));
    }

    public void loose() {
        System.out.println("\nGame over, you failed!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        this.exit();
    }

    public void restart() {
        this.restartInitObjects();
    }

    // key presses //

    public void accelerate() {
        ((Ant) this.gameObjects.get(antTag)).increaseSpeed(3);
    }

    public void brake() {
        ((Ant) this.gameObjects.get(antTag)).increaseSpeed(-3);
    }

    public void left() {
        ((Ant) this.gameObjects.get(antTag)).increaseHeading(-15);
    }

    public void right() {
        ((Ant) this.gameObjects.get(antTag)).increaseHeading(15);
    }

    public void setFoodConsumptionRate(int rate) {
        ((Ant) this.gameObjects.get(antTag)).setFoodConsumptionRate(rate);
    }

    public void collideFlag1() {
        ((Ant) this.gameObjects.get(antTag)).setNextFlag(1);
    }

    public void collideFlag2() {
        ((Ant) this.gameObjects.get(antTag)).setNextFlag(2);
    }

    public void collideFlag3() {
        ((Ant) this.gameObjects.get(antTag)).setNextFlag(3);
    }

    public void collideFlag4() {
        ((Ant) this.gameObjects.get(antTag)).setNextFlag(4);
    }

    public void collideFoodStation() {
        for (int i = 1; i <= this.numOfFoodStations; i++) {
            if (((FoodStation) gameObjects.get(foodStationTag + i)).getCapacity() != 0) {
                ((Ant) this.gameObjects.get(antTag))
                        .increaseFoodLevel(((FoodStation) gameObjects.get(foodStationTag + i))
                                .getCapacity());
            }
        }
        this.numOfFoodStations++;
        this.gameObjects.put((foodStationTag + this.numOfFoodStations), new FoodStation());
    }

    public void collideSpider() {
        ((Ant) this.gameObjects.get(antTag)).takeDamage(1);
    }

    public void tick() {
        for (GameObject go : gameObjects.values()) {
            go.tick();
        }
        if (this.winCondition()) {
            this.win();
        }
        if (this.looseCondition()) {
            this.loose();
        }
        if (this.restartCondition()) {
            this.restartInitObjects();
        }

        this.clock++;
    }

    public void display() {
        System.out.println();
        System.out.println("Lives: " + this.lives);
        System.out.println("Clock: " + this.clock);
        System.out.println("Ant's Highest Flag: " + ((Ant) this.gameObjects.get(antTag)).getLastFlag());
        System.out.println("Ant's Current Food Level: " + ((Ant) this.gameObjects.get(antTag)).getFoodLevel());
        System.out.println("Ant's Current Health Level: " + ((Ant) this.gameObjects.get(antTag)).getHealthLevel());
        System.out.println("Current number of loaded objects: " + this.gameObjects.size());
    }

    public void map() {
        System.out.println("\nDisplaying Map");

        // Stupid way to get all the values to sort
        // TODO: redo this using maps or something,
        // there has to be a faster or cleaner way
        ArrayList<String> tmpList = new ArrayList<String>();
        for (GameObject go : this.gameObjects.values()) {
            tmpList.add(go.toString());
        }
        Collections.sort(tmpList);

        for (String tmp : tmpList) {
            System.out.println(tmp);
        }
    }

    // Exit condition

    private boolean inExit = false;

    public void exit() {
        if (this.inExit) {
            System.out.println("\nKey press denied\nIn exit mode.");
        }

        this.inExit = true;
        System.out.println("\nDo you wish to exit?\ny/n");
    }

    public void confirm() {
        if (this.inExit) {
            System.exit(0);
        }
    }

    public void deny() {
        System.out.println("\nn confirmed\nreturning to game world");
        this.inExit = false;
    }

    public boolean inExit() {
        return this.inExit;
    }
}
