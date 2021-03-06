package com.recursivecorruption.thelastblue;

import com.badlogic.gdx.Preferences;
import com.recursivecorruption.thelastblue.graphics.Graphics;
import com.recursivecorruption.thelastblue.graphics.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private static int highScore;
    private Player player;
    private List<Entity> entities;
    private int score;
    private Random rand;

    public World() {
        reset();
        score = -1;
    }

    public static void init(Preferences prefs) {
        highScore = prefs.getInteger("highScore", 0);
    }

    public static void pause(Preferences prefs) {
        prefs.putInteger("highScore", highScore);
    }

    public static int getHighScore() {
        return highScore;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void updateHighScore() {
        if (score > highScore)
            highScore = score;
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean addPlayer) {
        entities = new ArrayList<Entity>();
        player = new Player(Graphics.getSX() / 2f, Graphics.getSY() / 2f);
        if (addPlayer)
            entities.add(player);
        rand = new Random();
        updateHighScore();
        score = 0;
    }

    public GameState update(GameState state) {
        Enemy.refresh();
        List<Entity> remove = new ArrayList<Entity>();
        List<Entity> create = new ArrayList<Entity>();
        boolean hasDied = false;
        for (Entity i : entities) {
            Entity j = i.update(this, state);
            if (j instanceof Player) {
                hasDied = true;
            }
            if (j != null) {
                create.addAll(j.createParticles());
                remove.add(j);
            }
        }
        entities.removeAll(remove);
        entities.addAll(create);
        if (hasDied) {
            score += (int) Math.pow((double) (Enemy.getMaxRad() - Enemy.BEGIN_SIZE), 2f);
            return GameState.MENU;
        }

        if (Enemy.getCount() < 50 && rand.nextInt(2 + ((100 * 1000) / (1000 + score + (int) Math.pow((double) Enemy.getMaxRad(), 2f)))) == 1) {
            int width = rand.nextInt(Graphics.getSX());
            int height = rand.nextInt(Graphics.getSY());
            if (rand.nextInt(2) == 1)
                width = Graphics.getSX() * rand.nextInt(2);
            else
                height = Graphics.getSY() * rand.nextInt(2);
            entities.add(new Enemy(width, height, player));
        }
        return null;
    }

    public void render(Renderer renderer) {
        for (Entity i : entities)
            i.draw(renderer);
    }
}
