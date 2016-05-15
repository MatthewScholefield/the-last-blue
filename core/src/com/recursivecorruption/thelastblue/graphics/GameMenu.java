package com.recursivecorruption.thelastblue.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.recursivecorruption.thelastblue.GameState;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class GameMenu {
    class Button extends UIElement {
        private Color color;
        private GameState onClick;
        private boolean isActive;
        private boolean wasTouched;
        // pos from top left corner

        public Button(String label, GameState onClick, Color color) {
            this.label = label;
            this.onClick = onClick;
            this.color = color;
            reset();
        }

        public void reset() {
            isActive = false;
            wasTouched = false;
        }

        public void draw(Renderer renderer) {
            renderer.rectangle(color, pos, size);
            renderer.printCentered((int) pos.y + (int) size.y / 2, label, true);
        }

        private boolean isTouched() {
            if (!Gdx.input.isTouched())
                return false;
            int dx = Graphics.getInputX() - (int) pos.x;
            int dy = Graphics.getInputY() - (int) pos.y;
            return dx > 0 && dx < size.x && dy > 0 && dy < size.y;
        }

        public GameState update() {
            boolean touched = isTouched();
            if (isActive) {
                if (wasTouched && !touched)
                    return onClick;
            } else if (touched && Gdx.input.justTouched()) {
                isActive = true;
            }
            wasTouched = touched;
            return null;
        }
    }

    abstract class UIElement {
        protected String label;
        protected Vector2 pos, size;

        public abstract void draw(Renderer renderer);

        public abstract GameState update();

        public abstract void reset();
    }

    private List<UIElement> elements;
    private static final float ELEMENT_GAP = 0.05f;

    public GameMenu() {
        elements = new ArrayList<UIElement>();
    }

    public void addButton(String label, GameState resultState, Color color) {
        elements.add(new Button(label, resultState, color));
    }

    // Must be called after adding all elements
    public void calcPositions() {
        int margin = Graphics.getMarginPx();
        Vector2 size = new Vector2();
        size.x = Graphics.getSX() - 2*margin;
        float ySpace = Graphics.getSY() - 2*margin;
        int numElements = elements.size();
        ySpace -= (numElements - 1) * ELEMENT_GAP*Graphics.getSY();
        size.y = ySpace / numElements;

        Vector2 pos = new Vector2((Graphics.getSX()-size.x)/2, margin);
        for (UIElement i : elements) {
            i.size = size.cpy();
            i.pos = pos.cpy();
            System.out.println(pos.y+","+Graphics.getSY());
            pos.y += Graphics.getSY() * ELEMENT_GAP + size.y;
        }
    }

    public void draw(Renderer renderer) {
        for (UIElement i : elements)
            i.draw(renderer);
    }

    public GameState update() {
        for (UIElement i : elements) {
            GameState state = i.update();
            if (state != null)
                return state;
        }
        return null;
    }

    public void reset() {
        for (UIElement i : elements)
            i.reset();
    }
}
