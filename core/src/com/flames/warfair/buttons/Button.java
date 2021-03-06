package com.flames.warfair.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;

/**
 * The button items on the windows extend this class.
 * Sprites are also considered buttons.
 */
public class Button implements java.io.Serializable{

    protected Rectangle rect;
    private GlyphLayout glyphLayout;
    private transient Color shapeColor;

    private String text;
    protected transient Texture texture;

    private boolean highlighted;

    public Button(String text, Rectangle rect) {
        this.rect = rect;
        this.text = text;
        glyphLayout = new GlyphLayout();
        glyphLayout.setText(MyGdxGame.smallFont, text);
        highlighted = false;
        shapeColor = Color.RED;
    }

    public Button(Texture texture, Rectangle rect) {
        this.rect = rect;
        this.texture = texture;
        highlighted = false;
    }

    /**
     * Render the shape of the button (rectangle).
     * @param sr -> ShapeRenderer, renders shapes
     */
    public void drawShape(ShapeRenderer sr) {
        sr.rect(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Draw the image of the button.
     * @param sb -> SpriteBatch, renders the image
     */
    public void drawImage(SpriteBatch sb) {
        sb.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Draw the image rotated horizontally.
     * @param sb -> SpriteBatch, renders the image
     */
    public void drawImageRotatedHorizontally(SpriteBatch sb) {
        sb.draw(texture, rect.x, rect.y, rect.width, rect.height, 0, 0, (int)rect.width, (int)rect.height, true, false);
    }

    /**
     * Draw the highlighted shape of the button.
     * @param sr -> ShapeRenderer, renders shapes
     */
    public void drawHighlight(ShapeRenderer sr) {
        if(highlighted) {
            sr.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    /**
     * Draw the text of the button.
     * @param sb -> SpriteBatch, renders the text
     */
    public void drawFont(SpriteBatch sb) {
        MyGdxGame.smallFont.draw(sb, text, rect.x + rect.width/2 - glyphLayout.width/2, rect.y + rect.height/2 + glyphLayout.height/2);
    }

    /**
     * Draw the text of the button. (medium font)
     * @param sb -> SpriteBatch, renders the text
     * @param s -> the text to be written
     */
    public void drawMediumFont(SpriteBatch sb, String s) {
        MyGdxGame.mediumFont.draw(sb, s, rect.x + rect.width/2 - glyphLayout.width/2, rect.y + rect.height/2 + glyphLayout.height/2);
    }

    /**
     * Draw the text of a text field. (used for text fields)
     * @param sb -> SpriteBatch, renders the text
     */
    public void drawFieldFont(SpriteBatch sb) {
        MyGdxGame.smallFont.draw(sb, text, rect.x + 5, rect.y+40);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setY(float y) {
        rect.y = y;
    }

    public void setX(float x) {
        rect.x = x;
    }

    public float getX() {
        return rect.x;
    }

    public String getText() {
        return text;
    }

    public void setHeight(int height) {
        rect.height = height;
    }

    public void setWidth(int width) {
        rect.width = width;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Color getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(Color color) {
        shapeColor = color;
    }

    public void setText(String s) {
        text = s;
    }

    public Texture getTexture() {
        return texture;
    }

    public GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }
}
