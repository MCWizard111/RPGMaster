package com.mcwizard111.rpgplus.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public class Grid {
    private AbstractGrid[][] grid;
    private OrthographicCamera camera;
    private Vector2 selectedSlot;
    private World world;
    public Body player;
    private BodyDef playerDef;
    private Sprite playerSprite;
    public Vector2 slotToBeRemoved;

    public Grid() {
        grid = new AbstractGrid[100][100];
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void generate(World world) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new StoneGrid();
                grid[y][x].setCoords(x, y);
                grid[y][x].createBoundingBox();
                grid[y][x].create(world);

                System.out.println("Generated at x: " + x + ", y: " + y);
            }
        }

        int limit = MathUtils.random(grid.length);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if ((MathUtils.random(limit)) % 2 == 0) {
                    if (y < grid.length - 2) {
                        grid[y][x] = null;
                        /*grid[y][x] = new AirGrid();
                        grid[y][x].setCoords(x, y);
                        grid[y][x].createBoundingBox();
                        grid[y][x].create(world);*/
                    }
                }
            }
        }
    }

    public void checkCollisions(int screenX, int screenY) {
        Vector3 mousePos = new Vector3(screenX, screenY, 0);
        camera.unproject(mousePos);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].getBoundingBox().contains(mousePos.x, mousePos.y)) {
                    grid[y][x] = new AirGrid();
                }
            }
        }
    }

    public void selectSlot(int screenX, int screenY) {
        Vector3 mousePos = new Vector3(screenX, screenY, 0);
        camera.unproject(mousePos);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] != null && grid[y][x].getBoundingBox().contains(mousePos.x, mousePos.y) && !(grid[y][x] instanceof AirGrid)) {
                    selectedSlot = new Vector2(x, y);
                }
            }
        }
    }

    public boolean slotIsSelected() {
        if (selectedSlot != null) {
            return true;
        } else {
            return false;
        }
    }

    public Vector2 getSelectedSlot() {
        return selectedSlot;
    }

    public IGrid getSelected() {
        return grid[(int) selectedSlot.y][(int) selectedSlot.x];
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void generatePlayer(World world) {
        playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(100, 3200);

        player = world.createBody(playerDef);
        player.setFixedRotation(true);

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(20, 32);

        FixtureDef playerFix = new FixtureDef();
        playerFix.density = 0.06f;
        playerFix.friction = 0.25f;
        playerFix.restitution = 0.0f;
        playerFix.shape = playerShape;

        player.createFixture(playerFix);
        player.setUserData("Player");

        playerShape.dispose();

        TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal("player.png")), 0, 0, 32, 64);
        region.flip(true, false);

        playerSprite = new Sprite(region);
        playerSprite.setPosition(playerDef.position.x, playerDef.position.y);
        playerSprite.setSize(32, 64);

    }

    public void checkPlayerGrounded(boolean jumping) {

    }

    public void debug(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();

        System.out.println(grid[(int) body2.getPosition().y / (int) body2.getPosition().y][(int) body2.getPosition().x / (int) body2.getPosition().x].getName());
    }

    public void destroySlot(int screenX, int screenY) {
        Vector3 mousePos = new Vector3(screenX, screenY, 0);
        camera.unproject(mousePos);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] != null && grid[y][x].getBoundingBox().contains(mousePos.x, mousePos.y)) {
                    grid[y][x].getBody().setActive(false);
                    grid[y][x].delete(world);
                    grid[y][x] = null;
                }
            }
        }
    }

    public AbstractGrid[][] getGrid() {
        return grid;
    }

    public void updateAndRenderPlayer(SpriteBatch batch) {
        playerSprite.setPosition(player.getPosition().x - 32 / 2, player.getPosition().y - 32 / 2);
        playerSprite.draw(batch);
    }
}
