package com.mcwizard111.rpgplus.grid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public abstract class AbstractGrid implements IGrid {
    private Rectangle boundingBox;
    private int x, y;
    private Sprite sprite;
    private Body boxBody;

    public String getName() {
        return "ERROR";
    }

    public void createBoundingBox() {
        boundingBox = new Rectangle(32 * x, 32 * y, 32, 32);
    }

    public boolean isSolid() {
        return true;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void create(World world) {
        BodyDef boxDef = new BodyDef();
        boxDef.position.set(32 * x, 32 * y);

        boxBody = world.createBody(boxDef);
        boxBody.setUserData("terrain");

        if (!isSolid()) {
            boxBody.setActive(false);
        }

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(32, 32);
        boxBody.createFixture(boxShape, 0.0f);

        boxShape.dispose();

        sprite = new Sprite(getTexture());
        sprite.setPosition(boxDef.position.x, boxDef.position.y);
        sprite.setSize(32, 32);
    }

    public abstract TextureRegion getTexture();

    public void update() {
        sprite.setPosition(boxBody.getPosition().x, boxBody.getPosition().y);
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch batch) {
        sprite.setPosition(boxBody.getPosition().x, boxBody.getPosition().y);
        sprite.draw(batch);
    }

    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.box(boundingBox.x, boundingBox.y, 0, boundingBox.width, boundingBox.height, 1);
    }

    public void delete(World world) {
        System.out.println("Deleting");
        world.destroyBody(boxBody);
    }

    public Body getBody() {
        return boxBody;
    }
}
