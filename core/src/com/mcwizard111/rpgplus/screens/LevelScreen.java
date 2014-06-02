package com.mcwizard111.rpgplus.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.mcwizard111.rpgplus.enums.Level;
import com.mcwizard111.rpgplus.grid.AbstractGrid;
import com.mcwizard111.rpgplus.grid.Grid;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public class LevelScreen extends ScreenAdapter implements GestureDetector.GestureListener, InputProcessor {
    private static Level level;
    private static World world;
    private static OrthographicCamera camera;
    private static Box2DDebugRenderer renderer;
    private static Grid grid;
    private static InputMultiplexer inputMultiplexer;
    private boolean movingRight, movingLeft;
    private boolean jumping = false;
    private static SpriteBatch batch;
    private static BitmapFont font;

    private static ShapeRenderer shapeRenderer;

    private boolean mouseScroll = false;

    public LevelScreen(Level level) {
        this.level = level;
        world = new World(new Vector2(0, -10), false);
        world.setGravity(new Vector2(0, -100));
        world.setContactListener(new MyContactListener());
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.update();
        renderer = new Box2DDebugRenderer();
        inputMultiplexer = new InputMultiplexer();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        font = new BitmapFont();

        grid = new Grid();
        grid.setCamera(camera);
        grid.setWorld(world);
        grid.generate(world);
        grid.generatePlayer(world);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(25, 2, 60, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!mouseScroll) {
            camera.position.set(grid.player.getPosition().x + 32 / 2, grid.player.getPosition().y + 32 / 2, 0);
            camera.update();
        }

        grid.checkPlayerGrounded(jumping);

        if (movingLeft) {
            grid.player.applyForceToCenter(new Vector2(-50000, 0), true);
        }

        if (movingRight) {
            grid.player.applyForceToCenter(new Vector2(50000, 0), true);
        }

        world.step(1 / 32f, 6, 2);

        if (grid.slotToBeRemoved != null) {
            System.out.println("Slot ready to be removed");
            grid.getGrid()[(int) grid.slotToBeRemoved.y][(int) grid.slotToBeRemoved.x] = null;
            grid.slotToBeRemoved = null;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int y = 0; y < grid.getGrid().length; y++) {
            for (int x = 0; x < grid.getGrid()[y].length; x++) {
                if (grid.getGrid()[y][x] != null) {
                    grid.getGrid()[y][x].render(batch);
                }
            }
        }
        batch.end();

        batch.begin();
        grid.updateAndRenderPlayer(batch);
        batch.end();

        if (grid.slotIsSelected()) {
            System.out.println("Selected: " + grid.getSelected().getName());
        }
    }

    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    // INPUT PROCESSOR METHODS //

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        grid.destroySlot((int) x, (int) y);
        /*switch (button) {
            case Input.Buttons.LEFT:
                grid.destroySlot((int) x, (int) y);
                break;
            case Input.Buttons.RIGHT:
                grid.selectSlot((int) x, (int) y);
                break;
        }*/

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (mouseScroll) {
            float translateX = -deltaX;
            float translateY = deltaY;

            camera.translate(translateX, translateY);

            this.camera.update();
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.X:
                mouseScroll = !mouseScroll;
                break;
            case Input.Keys.D:
                movingRight = true;
                break;
            case Input.Keys.RIGHT:
                movingRight = true;
                break;
            case Input.Keys.A:
                movingLeft = true;
                break;
            case Input.Keys.LEFT:
                movingLeft = true;
                break;
            case Input.Keys.SPACE:
                if (!jumping) {
                    jumping = true;
                    grid.player.applyForceToCenter(new Vector2(0, 10000000), true);
                }
                break;
            case Input.Keys.UP:
                if (!jumping) {
                    jumping = true;
                    grid.player.applyForceToCenter(new Vector2(0, 10000000), true);
                }
                break;
            case Input.Keys.W:
                if (!jumping) {
                    jumping = true;
                    grid.player.applyForceToCenter(new Vector2(0, 10000000), true);
                }
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
                movingRight = false;
                break;
            case Input.Keys.RIGHT:
                movingRight = false;
                break;
            case Input.Keys.A:
                movingLeft = false;
                break;
            case Input.Keys.LEFT:
                movingLeft = false;
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public class MyContactListener implements ContactListener {

        @Override
        public void beginContact(Contact contact) {

        }

        @Override
        public void endContact(Contact contact) {
            /*Body player = contact.getFixtureA().getBody();
            Body terrain = contact.getFixtureB().getBody();

            if (player.getUserData().equals("Player") && terrain.getUserData().equals("Terrain") && jumping) {
                jumping = false;
            } else {
                player.setAwake(false);
                terrain.setAwake(false);
            }*/
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }
}
