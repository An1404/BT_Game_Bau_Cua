package com.btl.ttltmang.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Object.Player;
import com.btl.ttltmang.Tool.HttpManager;
import com.btl.ttltmang.Tool.Toast;
import com.kotcrab.vis.ui.VisUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;

public class Room extends AbstractScreen {
    public static final int GA_NAI_X = 130;
    public static final int B_C_GA_Y = 200;
    public static final int BAU_X = 400;
    public static final int CA_X = 680;
    public static final int CUA_TOM_X = 960;
    public static final int NAI_TOM_Y = 450;
    public static final int BTN_WIDTH_HEIGHT = 180;
    public static int EVENT = 0;
    //tạo giao diện
    private final Label lblCoin;
    private final boolean[] dk_click_coin;
    private Label[] lblNameOther;
    public static Label[] lblCoinOther;
    float test = 0;
    //graphic
    public static  HashMap<Integer, Player> playerInRoom;
    private final boolean[] player;
    private  Array<Texture> arrTextureCoin;
    private final int[][] countClickThu;
    private final boolean[][] chooseThu;
    public  Array<Label> lblCountCoin;
    private final int[] arrCoin;
    private int[][] arrTextureWin;
    private  Music music_bg;
    //chat
    private final Table tableChat;
    private TextButton send_button;
    private TextArea message_field;
    private ScrollPane chat_scroll;
    private Label chat_label;
    private  ImageButton btnClose;
    //thông tin
    private Label message_ThongTin;
    private final Table tableThongTin;
    Texture imgBowl = Main.manager.get("bauCua/bowl.png", Texture.class);
    private int BOWL_Y;
    //progressbar
    private int TIME = 99;
    private int demTime =0;
    private ProgressBar bar;
    private final Label lblTime;

    //toast
    private Toast.ToastFactory factory;
    private Toast toast;

    //mảng hiện xx
    private Array<Texture> arrTextureXX;
    private Array<Integer> arrXX;
    private boolean tgChoi = false;

    //tien cược
    private int[] tienCuoc;
    private int tienAn = 0;
    //kt mang
    private float ktMang;
    //
    private String idLap;

    public Room(Main game) {
        super(game);
        //create
        dk_click_coin = new boolean[6];
        //create nv
        playerInRoom = new HashMap<>();
        player = new boolean[6];
        //create you
        Label.LabelStyle font = new Label.LabelStyle(Main.myFont, Color.WHITE);
        Label lblName = new Label(PlayGame.currentPlayer.getName(), font);// PlayGame.currentPlayer.getName()
        lblName.setPosition(300 - lblName.getWidth() / 2, 60);
        lblCoin = new Label(PlayGame.currentPlayer.getCoin() + "", font);
        lblCoin.setPosition(300 - lblCoin.getWidth() / 2, 10);
        Label lblRoom = new Label("ROOM: " + PlayGame.currentPlayer.getRoom(), font);// PlayGame.currentPlayer.getRoom()
        lblRoom.setPosition(Main.APP_WIDTH - 320, Main.APP_HEIGHT - 40);
        stage.addActor(lblRoom);
        stage.addActor(lblCoin);
        stage.addActor(lblName);
        // listen event to server
        ConfigSocket();
        //lấy id người trong phòng nếu có
        PlayGame.socket.emit("getRoomActive", "room" + PlayGame.currentPlayer.getRoom());
        //create chat
        tableChat = new Table();
        tableChat.setFillParent(true);
        tableChat.setBackground(new TextureRegionDrawable(new TextureRegion(Main.manager.get("room/bg_room_chat.png", Texture.class))));
        buildChatRoomTable();
        //progressbar
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        Pixmap pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        progressBarStyle.background = drawable;

        pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        progressBarStyle.knob = drawable;

        pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        progressBarStyle.knobBefore = drawable;

        bar = new ProgressBar(0, 20, 1, false, progressBarStyle);
        bar.setValue(20);
        bar.setAnimateDuration(0.25f);
        bar.setBounds(Main.APP_WIDTH/2-150, 370, 300, 100);

        stage.addActor(bar);
        lblTime = new Label("Đang Chuẩn bị",font);
        lblTime.setPosition(Main.APP_WIDTH/2 -lblTime.getWidth()/2, 395);
        stage.addActor(lblTime);
        bar.setVisible(false);
        //toast
        factory = new Toast.ToastFactory.Builder()
                    .font(Main.myFont_24)
                    .build();

       loadGraphic();
       //count click
        countClickThu = new int[6][6];
        chooseThu = new boolean[6][6];

        //add label count
        for(int i =0;i<lblCountCoin.size;i++){
            stage.addActor(lblCountCoin.get(i));
        }
        //label name and coin player in room
        lblNameOther = new Label[6];
        lblCoinOther = new Label[6];
        //coin
        arrCoin = new int[6];
        arrCoin[0]=10000;
        arrCoin[1]=20000;
        arrCoin[2]=50000;
        arrCoin[3]=100000;
        arrCoin[4] = 200000;
        arrCoin[5] = 500000;

        BOWL_Y = 470;

        //btn thông tin
        font = new Label.LabelStyle(Main.myFont, Color.BLACK);
        message_ThongTin = new Label(" GIỚI THIỆU\n" +
                "  1 Trò chơi sử dụng bát và 3 viên xúc xắc in hình 6 linh vật: bầu, cua, cá, tôm, gà, nai.\n" +
                "  2 Nhà cái sẽ đặt 3 viên xúc xắc vào bát đậy kín và lắc mạnh.\n" +
                "  3 Nhà chơi sẽ chọn mức cược đặt tiền và 1 ô hoặc nhiều ô linh vật  mình thích mà không bị giới hạn.\n" +
                " LUẬT TÍNH TIỀN\n" +
                "  1 Trúng 1 linh vật x1 tiền cược.\n" +
                "  2 Trúng 2 linh vật x2 tiền cược.\n" +
                "  3 Trúng 3 linh vật x3 tiền cược.", font);

        message_ThongTin.setWrap(true);
        message_ThongTin.setAlignment(Align.topLeft);
        tableThongTin = new Table();
        tableThongTin.setFillParent(true);
        tableThongTin.setBackground(new TextureRegionDrawable(new TextureRegion(Main.manager.get("room/bg_thong_tin.png", Texture.class))));
        tableThongTin.add(message_ThongTin).expand().fillX().top().left();
        ktMang = 0;

        music_bg  = Main.manager.get("sound/music_bg_room.mp3", Music.class);
        music_bg.setLooping(true);
        music_bg.play();


    }

    private void loadGraphic() {
        //load thu
        arrTextureWin = new int[6][2];
        arrTextureWin[0][0] = BAU_X;
        arrTextureWin[0][1] = B_C_GA_Y;
        arrTextureWin[1][0] = CUA_TOM_X;
        arrTextureWin[1][1] = B_C_GA_Y;
        arrTextureWin[2][0] = CUA_TOM_X;
        arrTextureWin[2][1] = NAI_TOM_Y;
        arrTextureWin[3][0] = CA_X;
        arrTextureWin[3][1] = B_C_GA_Y;
        arrTextureWin[4][0] = GA_NAI_X;
        arrTextureWin[4][1] = B_C_GA_Y;
        arrTextureWin[5][0] = GA_NAI_X;
        arrTextureWin[5][1] = NAI_TOM_Y;

        //load xx
        arrTextureXX = new Array<>();
        arrXX = new Array<>();
        Texture xxCube= Main.manager.get("bauCua/bauCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        xxCube = Main.manager.get("bauCua/cuaCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        xxCube = Main.manager.get("bauCua/tomCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        xxCube = Main.manager.get("bauCua/caCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        xxCube = Main.manager.get("bauCua/gaCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        xxCube = Main.manager.get("bauCua/naiCube.png",Texture.class);
        arrTextureXX.add(xxCube);
        //load coin
        arrTextureCoin = new Array<>();
        Texture img = Main.manager.get("moneys/10k.png", Texture.class);
        arrTextureCoin.add(img);
        img = Main.manager.get("moneys/20k.png", Texture.class);
        arrTextureCoin.add(img);
        img = Main.manager.get("moneys/50k.png", Texture.class);
        arrTextureCoin.add(img);
        img = Main.manager.get("moneys/100k.png", Texture.class);
        arrTextureCoin.add(img);
        img = Main.manager.get("moneys/200k.png", Texture.class);
        arrTextureCoin.add(img);
        img = Main.manager.get("moneys/500k.png", Texture.class);
        arrTextureCoin.add(img);

        //label coin
        Label.LabelStyle fontBlue = new Label.LabelStyle(Main.myFont, Color.BLUE);
        lblCountCoin = new Array<>();
        //0:bau 1:cua 2:tom 3:ca 4:ga 5 nai
        lblCountCoin.add(new Label("0",fontBlue));
        lblCountCoin.add(new Label("0",fontBlue));
        lblCountCoin.add(new Label("0",fontBlue));
        lblCountCoin.add(new Label("0",fontBlue));
        lblCountCoin.add(new Label("0",fontBlue));
        lblCountCoin.add(new Label("0",fontBlue));
        setPositionLabelCountCoin();
        //tiền cược
        tienCuoc = new int[6];





    }

    private void setPositionLabelCountCoin(){
        lblCountCoin.get(0).setPosition(BAU_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(0).getWidth()/2,B_C_GA_Y+BTN_WIDTH_HEIGHT-40);
        lblCountCoin.get(1).setPosition(CUA_TOM_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(1).getWidth()/2,B_C_GA_Y+BTN_WIDTH_HEIGHT-40);
        lblCountCoin.get(2).setPosition(CUA_TOM_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(2).getWidth()/2,NAI_TOM_Y+BTN_WIDTH_HEIGHT-40);
        lblCountCoin.get(3).setPosition(CA_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(3).getWidth()/2,B_C_GA_Y+BTN_WIDTH_HEIGHT-40);
        lblCountCoin.get(4).setPosition(GA_NAI_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(4).getWidth()/2,B_C_GA_Y+BTN_WIDTH_HEIGHT-40);
        lblCountCoin.get(5).setPosition(GA_NAI_X+BTN_WIDTH_HEIGHT/2-lblCountCoin.get(5).getWidth()/2,NAI_TOM_Y+BTN_WIDTH_HEIGHT-40);

    }


    @Override
    public void show() {
        super.show();
    }

    private void ConfigSocket() {
        PlayGame.socket.on("VaoChungRoom", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String _id = data.getString("id");
                    if(_id.equals(idLap))
                        return;

                    /*if(_id.equals(PlayGame.currentPlayer.getId()))
                        return;*/
                    int vt = -1;
                    for (int i = 0; i < player.length; i++) {
                        if (!player[i]) {
                            if (  i >0&& playerInRoom.get(i)!=null &&playerInRoom.get(i).getId().equals(_id) ) {
                                break;
                            }

                            vt = i + 1;
                            player[i] = true;
                            Gdx.app.log("SIZE GET VAO", _id);
                            idLap = _id;
                            break;
                        }
                    }

                    Player player = PlayGame.otherPlayers.get(_id);
                    if (player != null && vt!=-1)
                        playerInRoom.put(vt, player);



                    // }


                } catch (JSONException e) {
                    Gdx.app.log("LOI_CHUNGROOM", e.toString());
                }

            }
        }).on("getRoomActive", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray array = (JSONArray) args[0];
                if(array.length()<1)
                    return;
                for (int i = 0; i < array.length(); i++) {
                    try {
                        String _id = array.getString(i);
                        if ( !_id.equals(PlayGame.currentPlayer.getId())) {
                            Player newPlayer = PlayGame.otherPlayers.get(_id);
                            playerInRoom.put((i + 1), newPlayer);
                            player[i] = true;
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("LOI_GET_PLAYER_IN_ROOM", e.toString());
                    }
                }



            }

        }).on("OutChungRoom", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String _id = data.getString("id");
                    deletePlayer(_id);
                } catch (JSONException e) {
                    Gdx.app.log("LOIOUTROOM", e.toString());
                }
            }
        }).on("user_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String _mes = data.getString("mes");
                    String _name = data.getString("name");
                    String _id = data.getString("id");
                    if(_id.equals(PlayGame.currentPlayer.getId()))
                        chat_label.setText(chat_label.getText() +"Me"+ _mes);

                    else
                         chat_label.setText(chat_label.getText() +_name+ _mes);
                    chat_scroll.layout();
                    chat_scroll.scrollTo(0, 0, 0, 0);
                } catch (JSONException e) {
                    Gdx.app.log("LOI_LAY_TN",e.toString());
                }
            }
        }).on("getTime", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int mTime = TIME;
                 TIME = (int) args[0];
                //kt time
                if(mTime==99&& TIME>=10 &&TIME<=35){
                    EVENT = 3;
                }

            }
        }).on("getXucXac", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject arr = (JSONObject) args[0];
                if(arrXX.size >0)
                    return;

                try {
                    int xx = arr.getInt("con1");
                    arrXX.add(xx);
                     xx = arr.getInt("con2");
                    arrXX.add(xx);
                     xx = arr.getInt("con3");
                    arrXX.add(xx);
                } catch (JSONException e) {
                    Gdx.app.log("LOI_GetXX",e.toString());
                }

            }
        }).on("coindat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject data = (JSONObject) args[0];
                Gdx.app.log("LOI_CLIENT_DAT_COIN","22");
                try {
                    int sttlbl = data.getInt("sttThu");
                    int _coin = data.getInt("coin");
                    String _id = data.getString("id");
                    if(_id.equals(PlayGame.currentPlayer.getId()))
                        return;

                   String coinNow= lblCountCoin.get(sttlbl).getText().toString();
                   long coinChuyenDoi = chuyenDoiTienNguoc(coinNow);
                   lblCountCoin.get(sttlbl).setText(chuyeDoiTien(coinChuyenDoi+_coin));




                } catch (JSONException e) {
                    Gdx.app.log("LOI_CLIENT_DAT_COIN",e.toString());
                }

            }
        });
    }


    @Override
    public void render(float dt) {
        super.render(dt);
        //camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //
        Gdx.gl.glClearColor(1, 1, 1, 1);// xoá mh
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);





        //draw
        game.batch.begin();


        //kt lỗi mạng
        /*new HttpManager();
        if(HttpManager.LOI==1){
            showToast("Please check the internet again");

            toast.render(dt);
            if(ktMang>2.5){
                //đồng bộ với server
                JSONObject data = new JSONObject();
                try {
                    data.put("stt", PlayGame.currentPlayer.getRoom());
                    data.put("id", PlayGame.currentPlayer.getId());
                    PlayGame.socket.emit("out_room", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PlayGame.room[PlayGame.currentPlayer.getRoom()]--;
                PlayGame.currentPlayer.setRoom(0);

                game.setScreen(new MhLogin(game));
                dispose();

            }

            ktMang+=dt;
            game.batch.draw(imgBowl, Main.APP_WIDTH / 2 - 170, BOWL_Y, 340, 250);
            game.batch.end();
            stage.act();
            stage.draw();

            return;

        }*/
        createRoom();
        //xu lý time
        if(30-TIME >20 ){
            if(BOWL_Y!=NAI_TOM_Y+20)
                 showXucXac();
            bar.setValue(20);
            if(30-TIME ==22){
                BOWL_Y = NAI_TOM_Y+20;
            }
            else if(30-TIME==21 && TIME- demTime ==1){
                Main.manager.get("sound/diceshake.mp3", Sound.class).play();
            }
            drawThuWin(dt);
            tgChoi = false;
            bar.setVisible(false);
            lblTime.setVisible(true);
            if(EVENT ==3)
                 EVENT = 0;


        }
        else if(30-TIME >=0){

            if(30-TIME ==20  && TIME- demTime ==1){
                Main.manager.get("sound/bat_dau_dat_cuoc.mp3", Music.class).play();
                arrXX.clear();
                test =0;

            }

            BOWL_Y = NAI_TOM_Y+20;
            tgChoi = true;
            bar.setVisible(true);
            lblTime.setVisible(false);
            bar.setValue(30-TIME);

        }
        else {
            if(30-TIME ==-1)
                Main.manager.get("sound/dung_dat_cuoc.mp3", Music.class).play();

            if(BOWL_Y <Main.APP_HEIGHT-70  &&TIME!=99 ){
                BOWL_Y +=5;
            }
            else{
                for(int i =0;i<6;i++){
                    for(int j =0;j<6;j++){
                        chooseThu[i][j] = false;
                        countClickThu[i][j] = 0;
                    }
                    lblCountCoin.get(i).setText("0");

                }
                drawThuWin(dt);



            }

            showXucXac();
            if(tienAn >0 && tgChoi){
                Main.manager.get("sound/win.mp3", Sound.class).play();
                showToast("+"+tienAn);
                long _coin = PlayGame.currentPlayer.getCoin() +tienAn;
                lblCoin.setText(_coin+"");
                PlayGame.currentPlayer.setCoin(_coin);
                EVENT = 2;
                for(int i =0;i<tienCuoc.length;i++){
                    tienCuoc[i] = 0;
                }
                tienAn = 0;
            }
            tgChoi = false;

        }

        if(TIME- demTime ==1){

            if(30-TIME <=5 && 30-TIME >=0){
                Main.manager.get("sound/dem_tg_het.mp3", Sound.class).play();
            }
            if(30-TIME ==5){
                if(EVENT==1){
                    if(EVENT == 3)
                        removeTable(3);
                    else
                        removeTable(0);

                }
            }


            JSONObject data = new JSONObject();
            try {
                data.put("id",PlayGame.currentPlayer.getId());
                data.put("coin",PlayGame.currentPlayer.getCoin());
                data.put("room","room"+PlayGame.currentPlayer.getRoom());
                PlayGame.socket.emit("dongbocoin",data);
            } catch (JSONException e) {
                Gdx.app.log("LOI_DONGBOCOIN",e.toString());
            }
            //cập nhật coin
            for(int i =0;i<playerInRoom.size();i++){
                if(lblCoinOther[i] ==null)
                    continue;
                lblCoinOther[i].setText(chuyeDoiTien(playerInRoom.get(i+1).getCoin()));

            }
            //save data
            PlayGame.pres.putLong("coin",PlayGame.currentPlayer.getCoin());
            PlayGame.pres.flush();

        }
        demTime = TIME;
        PlayGame.socket.emit("getTime",PlayGame.currentPlayer.getRoom());




        //hiện khung chat
        if (EVENT == 1) {
            stage.draw();
            game.batch.end();
            return;
        }



        //draw avt
        createAvatar();

        for(int i =0;i<6;i++){
            drawCoinChoose(i);

        }


        //check click out
        if (Gdx.input.getX() * Main.SCALE_X < 150 && Gdx.input.getX() * Main.SCALE_X > 0 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < Main.APP_HEIGHT - 2 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > Main.APP_HEIGHT - 82) {
            if (Gdx.input.justTouched()) {
                if(TIME<10 || EVENT ==3)
                    onClickOutRoom();
                else{
                    EVENT = 2;
                    showToast("Bạn không thể out phòng lúc này");
                }



            }
        }

        //check click chat
        if (Gdx.input.getX() * Main.SCALE_X < Main.APP_WIDTH - 90 && Gdx.input.getX() * Main.SCALE_X > Main.APP_WIDTH - 150 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < Main.APP_HEIGHT + 5 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > Main.APP_HEIGHT - 50) {
            if (Gdx.input.justTouched()) {
                //Gdx.app.log("CLICK","asd");
                if(EVENT ==3)
                    onClickChat(3);
                else
                    onClickChat(0);

            }
        }
        //check click thông tin
        if(Gdx.input.getX()*Main.SCALE_X<Main.APP_WIDTH - 10 && Gdx.input.getX()*Main.SCALE_X > Main.APP_WIDTH-60
                &&Main.APP_HEIGHT- Gdx.input.getY()*Main.SCALE_Y < Main.APP_HEIGHT &&Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > Main.APP_HEIGHT-50){
            if(Gdx.input.justTouched()){
                if(EVENT ==3)
                    onClickShowInforGame(3);
                else
                    onClickShowInforGame(0);
            }
        }

        //kt click coin
        clickCoin();
        // kt đặt coin vào con thú
        datCuoc();
        //

        //di chuyển đĩa
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(imgBowl, Main.APP_WIDTH / 2 - 170, BOWL_Y, 340, 250);



        if(EVENT ==3){
            Texture imgWaiting = Main.manager.get("room/vui_long_cho.png",Texture.class);
            game.batch.draw(imgWaiting,Main.APP_WIDTH/2-200,Main.APP_HEIGHT/2+60,400,80);
            tgChoi = false;

        }
        game.batch.end();

        stage.act();
        stage.draw();


        if(EVENT ==2 && toast!=null){
            toast.render(dt);
        }
        addLabel();

    }

    private void removeTable(final int oldEVENT) {
        tableThongTin.remove();
        tableChat.remove();
        btnClose.remove();
        EVENT = oldEVENT;
    }

    private void onClickShowInforGame(final int oldEVENT) {
        EVENT = 1;
        stage.addActor(tableThongTin);
        if(btnClose!=null)
            btnClose.clear();
        btnClose = new ImageButton(new TextureRegionDrawable(new TextureRegion(Main.manager.get("room/btnExit.png", Texture.class))));
        btnClose.setSize(60, 60);
        btnClose.setPosition(Main.APP_WIDTH - 80, Main.APP_HEIGHT - 60);
        stage.addActor(btnClose);
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnClose.remove();
                tableThongTin.remove();
                EVENT = oldEVENT;
            }
        });
    }


    @Override
    public void dispose() {
        super.dispose();
        stage.clear();
        playerInRoom.clear();
        tableChat.clear();
        music_bg.stop();


    }

    private void createAvatar() {
        Texture player = Main.manager.get("avt.png", Texture.class);
        int x = 10, y = 240;

        if (playerInRoom.containsKey(1)) {
            game.batch.draw(player, x, y, 100, 100);
        }

        if (playerInRoom.containsKey(2)) {
            game.batch.draw(player, x, y + 150, 100, 100);
        }

        if (playerInRoom.containsKey(3)) {
            game.batch.draw(player, x, y + 300, 100, 100);
        }

        if (playerInRoom.containsKey(4)) {
            x = Main.APP_WIDTH - 105;
            game.batch.draw(player, x, y, 100, 100);
        }

        if (playerInRoom.containsKey(5)) {
            x = Main.APP_WIDTH - 105;
            game.batch.draw(player, x, y + 150, 100, 100);
        }

        if (playerInRoom.containsKey(6)) {
            x = Main.APP_WIDTH - 105;
            game.batch.draw(player, x, y + 300, 100, 100);
        }


    }

    private void addLabel() {

        int x = 10, y = 240;
        Label.LabelStyle font = new Label.LabelStyle(Main.myFont_24, Color.WHITE);
        if (playerInRoom.containsKey(1) && lblNameOther[0] == null) {
            //name
            lblNameOther[0] = new Label(playerInRoom.get(1).getName(), font);
            lblNameOther[0].setPosition(x + 50 - lblNameOther[0].getWidth() / 2, y - 30);
            //coin
            lblCoinOther[0] = new Label(chuyeDoiTien(playerInRoom.get(1).getCoin()),font);
            lblCoinOther[0].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y - 52);

            stage.addActor(lblCoinOther[0]);
            stage.addActor(lblNameOther[0]);
            Gdx.app.log("NAMEEE",lblNameOther[0].getText().toString());
        }
        if (playerInRoom.containsKey(2) && lblNameOther[1] == null) {
            lblNameOther[1] = new Label(playerInRoom.get(2).getName(), font);
            lblNameOther[1].setPosition(x + 50 - lblNameOther[1].getWidth() / 2, y - 30 + 150);
            //
            //coin
            lblCoinOther[1] = new Label(chuyeDoiTien(playerInRoom.get(2).getCoin()),font);
            lblCoinOther[1].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y +98);

            stage.addActor(lblCoinOther[1]);
            stage.addActor(lblNameOther[1]);
        }
        if (playerInRoom.containsKey(3) && lblNameOther[2] == null) {
            lblNameOther[2] = new Label(playerInRoom.get(3).getName(), font);
            lblNameOther[2].setPosition(x + 50 - lblNameOther[2].getWidth() / 2, y - 30 + 300);
            //
            //coin
            lblCoinOther[2] = new Label(chuyeDoiTien(playerInRoom.get(3).getCoin()),font);
            lblCoinOther[2].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y +248);

            stage.addActor(lblCoinOther[2]);
            stage.addActor(lblNameOther[2]);
        }
        if (playerInRoom.containsKey(4) && lblNameOther[3] == null) {
            x = Main.APP_WIDTH - 105;
            lblNameOther[3] = new Label(playerInRoom.get(4).getName(), font);
            lblNameOther[3].setPosition(x + 50 - lblNameOther[3].getWidth() / 2, y - 30);
            //
            //coin
            lblCoinOther[3] = new Label(chuyeDoiTien(playerInRoom.get(4).getCoin()),font);
            lblCoinOther[3].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y -52);
            //
            stage.addActor(lblCoinOther[3]);
            stage.addActor(lblNameOther[3]);
        }
        if (playerInRoom.containsKey(5) && lblNameOther[4] == null) {
            x = Main.APP_WIDTH - 105;
            lblNameOther[4] = new Label(playerInRoom.get(5).getName(), font);
            lblNameOther[4].setPosition(x + 50 - lblNameOther[4].getWidth() / 2, y - 30 + 150);
            //
            //coin
            lblCoinOther[4] = new Label(chuyeDoiTien(playerInRoom.get(5).getCoin()),font);
            lblCoinOther[4].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y +98);
            //
            stage.addActor(lblCoinOther[4]);
            stage.addActor(lblNameOther[4]);
        }
        if (playerInRoom.containsKey(6) && lblNameOther[5] == null) {
            x = Main.APP_WIDTH - 105;
            lblNameOther[5] = new Label(playerInRoom.get(6).getName(), font);
            lblNameOther[5].setPosition(x + 50 - lblNameOther[5].getWidth() / 2, y - 30 + 300);
            //
            //coin
            lblCoinOther[5] = new Label(chuyeDoiTien(playerInRoom.get(6).getCoin()),font);
            lblCoinOther[5].setPosition(x + 50 - lblCoinOther[0].getWidth() / 2, y - 248);
            //
            stage.addActor(lblCoinOther[5]);
            stage.addActor(lblNameOther[5]);
        }
    }


    private void onClickOutRoom() {
        Gdx.app.log("MH", "CHUYEN MH");
        //đồng bộ với server
        JSONObject data = new JSONObject();
        try {
            data.put("stt", PlayGame.currentPlayer.getRoom());
            data.put("id", PlayGame.currentPlayer.getId());
            PlayGame.socket.emit("out_room", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //với chính mình7
        PlayGame.room[PlayGame.currentPlayer.getRoom()]--;
        PlayGame.currentPlayer.setRoom(0);
        PlayGame.CLOSE_FORM = 5;
        dispose();
        game.setPlayGame();

    }

    private void onClickChat(final int  oldEVENT) {
        EVENT = 1;
        stage.addActor(tableChat);
        if(btnClose!=null)
              btnClose.clear();
        btnClose = new ImageButton(new TextureRegionDrawable(new TextureRegion(Main.manager.get("room/btnExit.png", Texture.class))));
        btnClose.setSize(60, 60);
        btnClose.setPosition(Main.APP_WIDTH - 80, Main.APP_HEIGHT - 60);
        stage.addActor(btnClose);
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnClose.remove();
                tableChat.remove();
                EVENT = oldEVENT;
            }
        });

    }

    private void deletePlayer(String id) {
        if(id.equals(PlayGame.currentPlayer.getId()))
            return;
        idLap = "";
        try {
            Gdx.app.log("TRUOCDELETE", playerInRoom.size()+"");
            if (player[0] && id.equals(playerInRoom.get(1).getId())) {
                //name
                lblNameOther[0].remove();
                lblNameOther[0] = null;
                //coin
                lblCoinOther[0].remove();
                lblCoinOther[0] = null;
                player[0] = false;
                playerInRoom.remove(1);

            }
            if (player[1] && id.equals(playerInRoom.get(2).getId())) {
                lblNameOther[1].remove();
                lblNameOther[1] = null;
                //
                //coin
                lblCoinOther[1].remove();
                lblCoinOther[1] = null;
                player[1] = false;
                playerInRoom.remove(2);
            }
            if (player[2] && id.equals(playerInRoom.get(3).getId())) {
                lblNameOther[2].remove();
                lblNameOther[2] = null;
                //
                //coin
                lblCoinOther[2].remove();
                lblCoinOther[2] = null;
                player[2] = false;
                playerInRoom.remove(3);
            }
            if (player[3] && id.equals(playerInRoom.get(4).getId())) {
                lblNameOther[3].remove();
                lblNameOther[3] = null;
                //
                //coin
                lblCoinOther[3].remove();
                lblCoinOther[3] = null;
                player[3] = false;
                playerInRoom.remove(4);
            }
            if (player[4] && id.equals(playerInRoom.get(5).getId())) {
                lblNameOther[4].remove();
                lblNameOther[4] = null;
                //
                //coin
                lblCoinOther[4].remove();
                lblCoinOther[4] = null;
                player[4] = false;
                playerInRoom.remove(5);
            }
            if (player[5] && id.equals(playerInRoom.get(6).getId())) {
                lblNameOther[5].remove();
                lblNameOther[5] = null;
                //coin
                lblCoinOther[5].remove();
                lblCoinOther[5] = null;
                player[5] = false;
                playerInRoom.remove(6);
            }
        } catch (Exception e) {

            Gdx.app.log("SAUDELETE", playerInRoom.size()+"");
        }
        Gdx.app.log("SAUDELETE", playerInRoom.size()+"");

    }

    private void clickCoin() {
        if (Gdx.input.getX() * Main.SCALE_X < 670 && Gdx.input.getX() * Main.SCALE_X > 580 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() && tgChoi) {
                int vt = 0;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        } else if (Gdx.input.getX() * Main.SCALE_X < 765 && Gdx.input.getX() * Main.SCALE_X > 675 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() &&tgChoi) {
                int vt = 1;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        } else if (Gdx.input.getX() * Main.SCALE_X < 860 && Gdx.input.getX() * Main.SCALE_X > 770 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() && tgChoi) {
                int vt = 2;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        } else if (Gdx.input.getX() * Main.SCALE_X < 975 && Gdx.input.getX() * Main.SCALE_X > 865 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() &&tgChoi) {
                int vt = 3;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        } else if (Gdx.input.getX() * Main.SCALE_X < 1050 && Gdx.input.getX() * Main.SCALE_X > 960 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() && tgChoi) {
                int vt = 4;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        } else if (Gdx.input.getX() * Main.SCALE_X < 1145 && Gdx.input.getX() * Main.SCALE_X > 1055 &&
                Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y < 97 && Main.APP_HEIGHT - Gdx.input.getY() * Main.SCALE_Y > 12) {
            if (Gdx.input.justTouched() && tgChoi) {
                int vt = 5;
                dk_click_coin[vt] = !dk_click_coin[vt];

                for (int i = 0; i < dk_click_coin.length; i++) {
                    if (i != vt)
                        dk_click_coin[i] = false;
                }
                Main.manager.get("sound/click_coin.mp3", Sound.class).play();
            }
        }
        if(!tgChoi){
            for(int i =0;i<6;i++){
                dk_click_coin[i] = false;
            }
        }
        Texture overplay_coin = Main.manager.get("moneys/overlay.png", Texture.class);
        if (dk_click_coin[0])
            game.batch.draw(overplay_coin, 580, 12, 90, 85);
        else if (dk_click_coin[1])
            game.batch.draw(overplay_coin, 675, 12, 90, 85);
        else if (dk_click_coin[2])
            game.batch.draw(overplay_coin, 770, 12, 90, 85);
        else if (dk_click_coin[3])
            game.batch.draw(overplay_coin, 865, 12, 90, 85);
        else if (dk_click_coin[4])
            game.batch.draw(overplay_coin, 960, 12, 90, 85);
        else if (dk_click_coin[5])
            game.batch.draw(overplay_coin, 1055, 12, 90, 85);
    }

    private void createRoom() {
        if(EVENT ==1)
            return;
        game.batch.setProjectionMatrix(camera.combined);
        Texture imgBroad = Main.manager.get("room/board.jpg", Texture.class);
        Texture btnBack = Main.manager.get("room/btnBack.png", Texture.class);
        Texture imgThongTin = Main.manager.get("room/thon_tin.png", Texture.class);
        Texture btnBau = Main.manager.get("bauCua/bauBtn.png", Texture.class);
        Texture btnCua = Main.manager.get("bauCua/cuaBtn.png", Texture.class);
        Texture btnTom = Main.manager.get("bauCua/tomBtn.png", Texture.class);
        Texture btnCa = Main.manager.get("bauCua/caBtn.png", Texture.class);
        Texture btnNai = Main.manager.get("bauCua/naiBtn.png", Texture.class);
        Texture btnGa = Main.manager.get("bauCua/gaBtn.png", Texture.class);
        Texture imgDisk = Main.manager.get("bauCua/disk.png", Texture.class);
        Texture imgKhung_avt = Main.manager.get("bauCua/khung_avt.png", Texture.class);
        Texture imgKhung = Main.manager.get("bauCua/khung.png", Texture.class);
        Texture imgKhung_coin = Main.manager.get("bauCua/khung_coin.png", Texture.class);
        Texture avt = Main.manager.get("avt.png", Texture.class);
        Texture imgChat = Main.manager.get("bauCua/imgChat.png", Texture.class);

        game.batch.draw(imgBroad, 0, 110, Main.APP_WIDTH, Main.APP_HEIGHT - 110);
        game.batch.draw(btnBack, 0, Main.APP_HEIGHT - 82, 150, 80);
        game.batch.draw(imgThongTin, Main.APP_WIDTH - 60, Main.APP_HEIGHT - 50, 50, 50);
        game.batch.draw(imgChat, Main.APP_WIDTH - 150, Main.APP_HEIGHT - 50, 60, 55);
        //game.batch.draw(imgPanel,0,0,Main.APP_WIDTH,100);
        //vẽ con thú
        game.batch.draw(btnNai, GA_NAI_X, NAI_TOM_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
        game.batch.draw(btnGa, GA_NAI_X, B_C_GA_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
        game.batch.draw(btnBau, BAU_X, B_C_GA_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
        game.batch.draw(btnCa, CA_X, B_C_GA_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
        game.batch.draw(btnCua, CUA_TOM_X, B_C_GA_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
        game.batch.draw(btnTom, CUA_TOM_X, NAI_TOM_Y, BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);

        //vẽ đĩa
        game.batch.draw(imgDisk, Main.APP_WIDTH / 2 - 200, NAI_TOM_Y, 400, 250);
       // game.batch.draw(imgBowl, Main.APP_WIDTH / 2 - 170, BOWL_Y, 340, 250);

        //vẽ khung & avt
        game.batch.draw(imgKhung, 0, 0, Main.APP_WIDTH, 110);
        game.batch.draw(imgKhung_avt, 180, 5, 250, 100);
        game.batch.draw(imgKhung_coin, 555, 5, 615, 100);
        game.batch.draw(avt, 80, 5, 100, 100);

        //vẽ coin
        game.batch.draw(arrTextureCoin.get(0), 580, 12, 90, 85);
        game.batch.draw(arrTextureCoin.get(1), 675, 12, 90, 85);
        game.batch.draw(arrTextureCoin.get(2), 770, 12, 90, 85);
        game.batch.draw(arrTextureCoin.get(3), 865, 12, 90, 85);
        game.batch.draw(arrTextureCoin.get(4), 960, 12, 90, 85);
        game.batch.draw(arrTextureCoin.get(5), 1055, 12, 90, 85);







    }

    private void buildChatRoomTable() {
        final Label.LabelStyle font = new Label.LabelStyle(Main.myFont, Color.BLACK);
        chat_label = new Label("", font);

        chat_label.setWrap(true);
        chat_label.setAlignment(Align.topLeft);

        chat_scroll = new ScrollPane(chat_label, VisUI.getSkin());
        chat_scroll.setFadeScrollBars(false);
        chat_scroll.layout();
        chat_scroll.scrollTo(0, 0, 0, 0);
        tableChat.add(chat_scroll).width(Main.APP_WIDTH).height(Main.APP_HEIGHT - 50).colspan(2);

        message_field = new TextArea("", Main.skin);
        message_field.setPrefRows(2);

        tableChat.row();
        tableChat.add(message_field).width(Main.APP_WIDTH - 90);

        send_button = new TextButton("Send", Main.skin);
        tableChat.add(send_button).width(80).height(40).left();

        send_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                String text = message_field.getText();

                if (!text.isEmpty()) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("room","room"+PlayGame.currentPlayer.getRoom());
                        data.put("name",PlayGame.currentPlayer.getName());
                        data.put("id",PlayGame.currentPlayer.getId());
                        data.put("mes", ": " + text + "\n");
                        PlayGame.socket.emit("user_message", data);
                        message_field.setText("");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private void showToast(String msg) {
        game.batch.setProjectionMatrix(camera.combined);
        toast = factory.create(msg, Toast.Length.SHORT);

    }

    private void showXucXac() {

        game.batch.setProjectionMatrix(camera.combined);

        if(arrXX.size ==3){

           // Gdx.app.log("WIIn",tienCuoc[5]+"");
            //tính tiền win
           if(arrXX.get(0) == arrXX.get(1)){

               if(arrXX.get(1) == arrXX.get(2)){
                   //x3
                   tienCuoc[arrXX.get(0)]+=tienCuoc[arrXX.get(0)]*3;
               }
               else {
                   //x2
                   tienCuoc[arrXX.get(0)]+=tienCuoc[arrXX.get(0)]*2;
                   tienCuoc[arrXX.get(2)]+=tienCuoc[arrXX.get(2)];
               }
           }
           else if(arrXX.get(0)==arrXX.get(2)){
               //x2
               tienCuoc[arrXX.get(0)]+=tienCuoc[arrXX.get(0)]*2;
               tienCuoc[arrXX.get(1)]+=tienCuoc[arrXX.get(1)];
           }
           else {
               if(arrXX.get(1) == arrXX.get(2)){
                   //x2
                   tienCuoc[arrXX.get(1)]+=tienCuoc[arrXX.get(1)]*2;
                   tienCuoc[arrXX.get(0)]+=tienCuoc[arrXX.get(0)];
               }
               else {
                   //x1
                   tienCuoc[arrXX.get(1)]+=tienCuoc[arrXX.get(1)];
                   tienCuoc[arrXX.get(2)]+=tienCuoc[arrXX.get(2)];
                   tienCuoc[arrXX.get(0)]+=tienCuoc[arrXX.get(0)];
               }
           }

           for(int i =0;i<tienCuoc.length;i++){
               if(i != arrXX.get(0) && i != arrXX.get(1)  && i != arrXX.get(2) ){
                   tienCuoc[i] = 0;
               }
               tienAn+=tienCuoc[i];

           }
            if(EVENT!=1){
                game.batch.draw(arrTextureXX.get(arrXX.get(0)),Main.APP_WIDTH / 2 - 100,550,100,100);
                game.batch.draw(arrTextureXX.get(arrXX.get(1)),Main.APP_WIDTH / 2 +20,550,100,100);
                game.batch.draw(arrTextureXX.get(arrXX.get(2)),Main.APP_WIDTH / 2 - 40,490,100,100);
            }



        }



    }

    private void datCuoc() {
        int coinChoose = -1;
        for(int i=0;i< dk_click_coin.length;i++){
            if(dk_click_coin[i]){
                coinChoose = i;
                break;
            }
        }
        if(coinChoose ==-1)
            return;

        //click Nai
        if(Gdx.input.getX()*Main.SCALE_X < GA_NAI_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > GA_NAI_X
            && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > NAI_TOM_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < NAI_TOM_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[5][coinChoose]++;
                chooseThu[5][coinChoose] = true;

                String coinNow= lblCountCoin.get(5).getText().toString();
                long coinNai =chuyenDoiTienNguoc(coinNow);

                lblCountCoin.get(5).setText(chuyeDoiTien(coinNai+arrCoin[coinChoose]));
                tienCuoc[5]+=arrCoin[coinChoose];
                guiServerCoinDat(5,arrCoin[coinChoose]);

            }

        }

        if(Gdx.input.getX()*Main.SCALE_X < GA_NAI_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > GA_NAI_X
                && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > B_C_GA_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < B_C_GA_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[4][coinChoose]++;
                chooseThu[4][coinChoose] = true;

                String coinNow= lblCountCoin.get(4).getText().toString();
                long coinGa =chuyenDoiTienNguoc(coinNow);
                lblCountCoin.get(4).setText(chuyeDoiTien(coinGa+arrCoin[coinChoose]));
                tienCuoc[4]+=arrCoin[coinChoose];
                guiServerCoinDat(4,arrCoin[coinChoose]);
            }

        }

        if(Gdx.input.getX()*Main.SCALE_X < BAU_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > BAU_X
                && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > B_C_GA_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < B_C_GA_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[0][coinChoose]++;
                chooseThu[0][coinChoose] = true;

                String coinNow= lblCountCoin.get(0).getText().toString();
                long coinBau =chuyenDoiTienNguoc(coinNow);
                lblCountCoin.get(0).setText(chuyeDoiTien(coinBau+arrCoin[coinChoose]));
                tienCuoc[0]+=arrCoin[coinChoose];
                guiServerCoinDat(0,arrCoin[coinChoose]);
            }

        }

        if(Gdx.input.getX()*Main.SCALE_X < CA_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > CA_X
                && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > B_C_GA_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < B_C_GA_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[3][coinChoose]++;
                chooseThu[3][coinChoose] = true;

                String coinNow= lblCountCoin.get(3).getText().toString();
                long coinCa =chuyenDoiTienNguoc(coinNow);
                lblCountCoin.get(3).setText(chuyeDoiTien(coinCa+arrCoin[coinChoose]));
                tienCuoc[3]+=arrCoin[coinChoose];
                guiServerCoinDat(3,arrCoin[coinChoose]);
            }

        }

        if(Gdx.input.getX()*Main.SCALE_X < CUA_TOM_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > CUA_TOM_X
                && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > B_C_GA_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < B_C_GA_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[1][coinChoose]++;
                chooseThu[1][coinChoose] = true;

                String coinNow= lblCountCoin.get(1).getText().toString();
                long coinCua =chuyenDoiTienNguoc(coinNow);
                lblCountCoin.get(1).setText(chuyeDoiTien(coinCua+arrCoin[coinChoose]));
                tienCuoc[1]+=arrCoin[coinChoose];
                guiServerCoinDat(1,arrCoin[coinChoose]);
            }

        }

        if(Gdx.input.getX()*Main.SCALE_X < CUA_TOM_X+BTN_WIDTH_HEIGHT  && Gdx.input.getX()*Main.SCALE_X > CUA_TOM_X
                && Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y > NAI_TOM_Y &&
                Main.APP_HEIGHT -  Gdx.input.getY()*Main.SCALE_Y < NAI_TOM_Y+BTN_WIDTH_HEIGHT){
            if(Gdx.input.justTouched()){
                if(dongBoCoin(arrCoin[coinChoose])){
                    return;
                }

                countClickThu[2][coinChoose]++;
                chooseThu[2][coinChoose] = true;

                String coinNow= lblCountCoin.get(2).getText().toString();
                long coinTom =chuyenDoiTienNguoc(coinNow);
                lblCountCoin.get(2).setText(chuyeDoiTien(coinTom+arrCoin[coinChoose]));
                tienCuoc[2]+=arrCoin[coinChoose];
                guiServerCoinDat(2,arrCoin[coinChoose]);
            }

        }



    }

    private boolean dongBoCoin(int i) {
        long _coin = PlayGame.currentPlayer.getCoin();
        if(_coin-i<0){
            showToast("Không đủ tiền để đặt cược");
            EVENT =2;
            //bị lỗi
            return true;
        }
        _coin-=i;
        lblCoin.setText(_coin+"");
        PlayGame.currentPlayer.setCoin(_coin);
        //ko lỗi
        return false;
    }

    private void drawCoinChoose(int l){
        //draw coin choose
        game.batch.setProjectionMatrix(camera.combined);
        int x ,y ;
        if(chooseThu[5][l]) {
            y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
            x = GA_NAI_X + 10;
            for (int i = 0; i < countClickThu[5][l]; i++) {
                if (x > GA_NAI_X + BTN_WIDTH_HEIGHT - 70) {
                    x = GA_NAI_X + 10;
                    y -= 20;
                }
                if (y < NAI_TOM_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = GA_NAI_X + 15;
                    y = NAI_TOM_Y + BTN_WIDTH_HEIGHT / 2;
                }

                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }


        if(chooseThu[4][l]) {
            y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
            x = GA_NAI_X + 10;
            for (int i = 0; i < countClickThu[4][l]; i++) {
                if (x > GA_NAI_X + BTN_WIDTH_HEIGHT - 70) {
                    x = GA_NAI_X + 10;
                    y -= 20+l;
                }
                if (y < B_C_GA_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = GA_NAI_X + 15;
                    y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
                }
                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }

        if(chooseThu[0][l]) {
            y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
            x = BAU_X + 10;
            for (int i = 0; i < countClickThu[0][l]; i++) {
                if (x > BAU_X + BTN_WIDTH_HEIGHT - 70) {
                    x = BAU_X + 10;
                    y -= 20;
                }
                if (y < B_C_GA_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = BAU_X + 15;
                    y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
                }
                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }



        if(chooseThu[3][l]) {
            y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
            x = CA_X + 10;
            for (int i = 0; i < countClickThu[3][l]; i++) {
                if (x > CA_X + BTN_WIDTH_HEIGHT - 70) {
                    x = CA_X + 10;
                    y -= 20;
                }
                if (y < B_C_GA_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = CA_X + 15;
                    y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
                }
                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }

        if(chooseThu[1][l]) {
            y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
            x = CUA_TOM_X + 10;
            for (int i = 0; i < countClickThu[1][l]; i++) {
                if (x > CUA_TOM_X + BTN_WIDTH_HEIGHT - 70) {
                    x = CUA_TOM_X + 10;
                    y -= 20;
                }
                if (y < B_C_GA_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = CUA_TOM_X + 15;
                    y = B_C_GA_Y + BTN_WIDTH_HEIGHT / 2;
                }
                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }

        if(chooseThu[2][l]) {
            y = NAI_TOM_Y + BTN_WIDTH_HEIGHT / 2;
            x = CUA_TOM_X + 10;
            for (int i = 0; i < countClickThu[2][l]; i++) {
                if (x > CUA_TOM_X + BTN_WIDTH_HEIGHT - 70) {
                    x = CUA_TOM_X + 10;
                    y -= 20;
                }
                if (y < NAI_TOM_Y + BTN_WIDTH_HEIGHT / 2 - 80) {
                    x = CUA_TOM_X + 15;
                    y = NAI_TOM_Y + BTN_WIDTH_HEIGHT / 2;
                }
                game.batch.draw(arrTextureCoin.get(l), x, y-15*l, 40, 40);
                x += 10;
            }
        }

    }

    private String chuyeDoiTien(long _coin){
        //K
        _coin/=1000;
        //M
        if(_coin>=1000){
            int duM = (int)_coin%1000;
            int duT = (int)_coin%1000000;
            _coin/=1000;
            if(_coin>=1000){
                _coin/=1000;
                while (duT%10==0){
                    duT/=10;
                }
                return _coin+"."+duT +"T";
            }
            else{
                while (duM%10==0 && duM!=0){
                    duM/=10;
                } return _coin+"."+duM  +"M";

            }



        }
        else
            return _coin+"K";

    }
    private long chuyenDoiTienNguoc(String _coin){
        String donVi = _coin.substring(_coin.length()-1).trim();
        float coinNow = 0;
        if(_coin.length()>1)
                 coinNow =Float.parseFloat(_coin.substring(0,_coin.length()-1));
        Gdx.app.log("COIi",coinNow+"");
        if(donVi .equals("K")){
            return (long) (coinNow*1000);
        }
        else if(donVi.equals("M")){
            return (long) (coinNow*1000000);
        }
        else if(donVi.equals("T")){
            return (long) (coinNow*1000000000);
        }
        else
            return (long) coinNow;

    }
    private void drawThuWin(float delta){
        game.batch.setProjectionMatrix(camera.combined);
        if (arrXX.size !=3 || EVENT ==1)
            return;
        Texture imgWin = Main.manager.get("bauCua/win.png", Texture.class);
        if (test > 0.4f) {
            game.batch.draw(imgWin, arrTextureWin[arrXX.get(0)][0], arrTextureWin[arrXX.get(0)][1], BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
            game.batch.draw(imgWin, arrTextureWin[arrXX.get(1)][0], arrTextureWin[arrXX.get(1)][1], BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);
            game.batch.draw(imgWin, arrTextureWin[arrXX.get(2)][0], arrTextureWin[arrXX.get(2)][1], BTN_WIDTH_HEIGHT, BTN_WIDTH_HEIGHT);

            if (test > 1)
                test = 0;
        }
        test += delta;
    }
    private void guiServerCoinDat(int thu,int coin){
        JSONObject data = new JSONObject();
        try {
            data.put("room","room"+PlayGame.currentPlayer.getRoom());
            data.put("sttThu",thu);
            data.put("coin",coin);
            data.put("id",PlayGame.currentPlayer.getId());
            PlayGame.socket.emit("coindat",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
