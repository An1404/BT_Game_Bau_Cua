package com.btl.ttltmang;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.btl.ttltmang.Screen.MhLogin;
import com.btl.ttltmang.Screen.PlayGame;
import com.kotcrab.vis.ui.VisUI;

public class Main extends Game {
	public SpriteBatch batch;
	public static final int APP_WIDTH =1280;
	public static final int APP_HEIGHT = 720;
	public static  int WIDTH;
	public static int HEIGHT ;
	public static float SCALE_X;
	public static float SCALE_Y;
	public static final String TITLE = "Game Bầu Cua";
	public static AssetManager manager;
	public static BitmapFont myFont;
	public static BitmapFont myFont_24;
	private PlayGame playGame;
	public static Skin skin;
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		loadResources();
		manager.finishLoading();
		VisUI.load();
		myFont  =new BitmapFont(Gdx.files.internal("Font/myFont.fnt"));
		myFont_24  =new BitmapFont(Gdx.files.internal("Font/myFont_24.fnt"));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		playGame = new PlayGame(this);
		//screen
		setScreen(new MhLogin(this));

	}
	public void setPlayGame(){
		setScreen(playGame);
	}

	private void loadResources() {
		manager.load("bg_sanh.jpg",Texture.class);
		manager.load("btnLogin/bg.jpg",Texture.class);
		manager.load("btnLogin/logo_baucua.png",Texture.class);
		manager.load("btnLogin/btnPlay.png",Texture.class);
		manager.load("btnLogin/btnPlayOn.png",Texture.class);
		manager.load("btnLogin/btnExit.png",Texture.class);
		manager.load("btnLogin/btnExitOn.png",Texture.class);
		manager.load("avt.png",Texture.class);
		manager.load("imageLobby/khung.png",Texture.class);
		manager.load("room/board.jpg",Texture.class);
		manager.load("room/btnBack.png",Texture.class);
		manager.load("room/thon_tin.png",Texture.class);
		manager.load("bauCua/bauBtn.png",Texture.class);
		manager.load("bauCua/caBtn.png",Texture.class);
		manager.load("bauCua/cuaBtn.png",Texture.class);
		manager.load("bauCua/gaBtn.png",Texture.class);
		manager.load("bauCua/naiBtn.png",Texture.class);
		manager.load("bauCua/tomBtn.png",Texture.class);
		manager.load("imageLobby/loadingText.png",Texture.class);
		manager.load("bauCua/win.png",Texture.class);
		manager.load("bauCua/disk.png",Texture.class);
		manager.load("bauCua/bowl.png",Texture.class);
		manager.load("bauCua/khung_avt.png",Texture.class);
		manager.load("bauCua/khung.png",Texture.class);
		manager.load("bauCua/khung_coin.png",Texture.class);
		manager.load("moneys/10k.png",Texture.class);
		manager.load("moneys/20k.png",Texture.class);
		manager.load("moneys/50k.png",Texture.class);
		manager.load("moneys/100k.png",Texture.class);
		manager.load("moneys/200k.png",Texture.class);
		manager.load("moneys/500k.png",Texture.class);
		manager.load("bauCua/imgChat.png",Texture.class);
		manager.load("moneys/overlay.png",Texture.class);
		manager.load("room/btnExit.png",Texture.class);
		manager.load("room/bg_room_chat.png",Texture.class);
		manager.load("loading/loading_5.png", Texture.class);
		manager.load("loading/bar_bg.png", Texture.class);
		manager.load("loading/loading_49.png", Texture.class);
		manager.load("loading/loading_19.png", Texture.class);
		manager.load("loading/loading_frame.png", Texture.class);
		manager.load("loading/loading.pack", TextureAtlas.class);
		manager.load("sound/bat_dau_dat_cuoc.mp3", Music.class);
		manager.load("sound/dung_dat_cuoc.mp3", Music.class);
		manager.load("sound/dem_tg_het.mp3", Sound.class);
		manager.load("sound/click_coin.mp3", Sound.class);
		manager.load("sound/diceshake.mp3", Sound.class);
		manager.load("bauCua/bauCube.png",Texture.class);
		manager.load("bauCua/tomCube.png",Texture.class);
		manager.load("bauCua/cuaCube.png",Texture.class);
		manager.load("bauCua/caCube.png",Texture.class);
		manager.load("bauCua/naiCube.png",Texture.class);
		manager.load("bauCua/gaCube.png",Texture.class);
		manager.load("room/vui_long_cho.png",Texture.class);
		manager.load("room/cho.png",Texture.class);
		manager.load("sound/win.mp3", Sound.class);
		manager.load("room/bg_thong_tin.png",Texture.class);
		manager.load("sound/music_bg_room.mp3", Music.class);


	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		WIDTH = width;
		HEIGHT = height;
		SCALE_X = (float) Main.APP_WIDTH/Main.WIDTH;
		SCALE_Y = (float) Main.APP_HEIGHT/Main.HEIGHT;


	}
}
