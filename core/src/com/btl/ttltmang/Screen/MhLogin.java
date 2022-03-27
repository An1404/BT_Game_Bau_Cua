package com.btl.ttltmang.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Tool.HttpManager;
import com.btl.ttltmang.Tool.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MhLogin extends AbstractScreen{
    private Texture btnChoi;
    private Texture btnOnChoi;
    private Texture btnExit;
    private Texture btnOnExit;
    private Texture imgLogo;
    private Texture imgBackground;
    private Toast toast;
    private float mTime =0;
    public static final int BUTTON_WIDTH = 300;
    public static final int BUTTON_HEIGHT = 150;
    public static final int BUTTON_X = Main.APP_WIDTH/2-BUTTON_WIDTH/2;
    public static final int BUTTON_EXIT_Y =Main.APP_HEIGHT/2-240;
    public static final int BUTTON_PLAY_Y = Main.APP_HEIGHT/2-130;
    //


    public MhLogin( Main game) {
        super(game);
        imgBackground = Main.manager.get("btnLogin/bg.jpg",Texture.class);

        imgLogo = Main.manager.get("btnLogin/logo_baucua.png",Texture.class);
        //btn choi ngay
        btnChoi = Main.manager.get("btnLogin/btnPlay.png",Texture.class);
        btnOnChoi  =Main.manager.get("btnLogin/btnPlayOn.png",Texture.class);
        //btn thoat
        btnExit = Main.manager.get("btnLogin/btnExit.png",Texture.class);
        btnOnExit =Main.manager.get("btnLogin/btnExitOn.png",Texture.class);
        //viewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());












    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
       // Gdx.app.log("INPUT","X: " + Gdx.input.getX()*((float)Main.SCALE) + "\nY: " + Gdx.input.getY()*((float)Main.SCALE));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        //Gdx.gl.glViewport(0,0,Main.WIDTH,Main.HEIGHT);
        // xoá mh
        Gdx.gl.glClearColor(1,0,0,1);// xoá mh
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(imgBackground,0,0,Main.APP_WIDTH,Main.APP_HEIGHT);
        game.batch.draw(imgLogo,Main.APP_WIDTH/2-150,Main.APP_HEIGHT/2,300,300);
        /*new HttpManager();
        if(HttpManager.LOI ==1){
            createToast(delta);
        }*/



        game.batch.setProjectionMatrix(camera.combined);
        float x = BUTTON_X;
        if (Gdx.input.getX()*Main.SCALE_X< x+BUTTON_WIDTH && Gdx.input.getX()*Main.SCALE_X>x && Main.APP_HEIGHT-Gdx.input.getY()*Main.SCALE_Y<BUTTON_HEIGHT+BUTTON_PLAY_Y&&
                Main.APP_HEIGHT-Gdx.input.getY()*Main.SCALE_Y>BUTTON_PLAY_Y){
            game.batch.draw(btnOnChoi,BUTTON_X,BUTTON_PLAY_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
            if(Gdx.input.isTouched() && HttpManager.LOI ==0){
                if(PlayGame.currentPlayer.getId()!=null)
                    PlayGame.CLOSE_FORM = 5;
                game.setPlayGame();
            }

        }
        else
            game.batch.draw(btnChoi,BUTTON_X,BUTTON_PLAY_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
        if (Gdx.input.getX()*Main.SCALE_X < x+BUTTON_WIDTH && Gdx.input.getX()*Main.SCALE_X>x && Main.APP_HEIGHT-Gdx.input.getY()*Main.SCALE_Y<BUTTON_HEIGHT+BUTTON_EXIT_Y&&
                Main.APP_HEIGHT-Gdx.input.getY()*Main.SCALE_Y>BUTTON_EXIT_Y){
           game.batch.draw(btnOnExit,BUTTON_X,BUTTON_EXIT_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
           if(Gdx.input.isTouched())
               Gdx.app.exit();

       }
        else
            game.batch.draw(btnExit,BUTTON_X,BUTTON_EXIT_Y,BUTTON_WIDTH,BUTTON_HEIGHT);

        game.batch.end();



        // xử lý sự kiện


    }


    private void createToast(float delta) {
        game.batch.setProjectionMatrix(camera.combined);
        if(mTime ==0 || mTime >6){
            Toast.ToastFactory factory = new Toast.ToastFactory.Builder()
                    .font(Main.myFont)
                    .build();
            toast = factory.create("Please check the internet again", Toast.Length.SHORT);

            mTime =0;

        }
        else
            toast.render(delta);
        mTime +=delta;
        System.out.println(mTime);


    }
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("SIZE", "'h: " + width + " \nh: " +height);
       // viewport.update(width,height);




    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
