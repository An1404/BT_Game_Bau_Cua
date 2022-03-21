package com.btl.ttltmang.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.btl.ttltmang.Main;

public abstract class AbstractScreen implements Screen {
    protected  Main game;
    protected  Stage stage;

    protected  OrthographicCamera camera;

    protected AbstractScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,Main.APP_WIDTH,Main.APP_HEIGHT);
        camera.position.set(Main.APP_WIDTH/2,Main.APP_HEIGHT/2,0);

        this.stage = new Stage(new StretchViewport(Main.APP_WIDTH,Main.APP_HEIGHT,camera));



    }
    @Override
    public void render(float dt) {
        stage.act(dt);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

    }

    public Stage getStage() {
        return stage;
    }
    public SpriteBatch getBatch() {
        return game.batch;
    }
    public Main getGame() { return game; }

}
