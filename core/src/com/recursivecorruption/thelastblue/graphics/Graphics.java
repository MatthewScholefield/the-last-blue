package com.recursivecorruption.thelastblue.graphics;

import com.badlogic.gdx.Gdx;

public class Graphics {
    private static float SCREEN_SCALE = 1.5f;

    public static float getScaleConstant()
    {
        return SCREEN_SCALE/((Gdx.graphics.getDensity()+(Gdx.graphics.getHeight()/630f))/2f);
    }

    public static int getX()
    {
        return (int)(getScaleConstant()*Gdx.input.getX());
    }

    public static int getY()
    {
        return (int)(getScaleConstant()*Gdx.input.getY());
    }

    public static int getSX()
    {
        return (int)(getScaleConstant()*Gdx.graphics.getWidth());
    }

    public static int getSY()
    {
        return (int)(getScaleConstant()*Gdx.graphics.getHeight());
    }
}