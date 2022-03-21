package com.btl.ttltmang.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Object.Player;
import com.btl.ttltmang.Tool.FormValidator;
import com.btl.ttltmang.Tool.HttpManager;
import com.btl.ttltmang.Tool.PagedScrollPane;
import com.btl.ttltmang.Tool.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PlayGame extends AbstractScreen {
    public static int CLOSE_FORM = 0;
    public static  String USER_NAME = "";
    public static  String USER_PHONE = "";
    private FormValidator formInfor;
    private Texture bg_Sanh =Main.manager.get("bg_sanh.jpg",Texture.class);
    private float mTime =0;
    private int mTimes;
    public static final int PORT = 2000;
    //Toast
    public static Toast.ToastFactory toastFactory;
    public static Toast toast;
    private Label lblCoin;
    private Label  lblName;
    // client
    public static Socket socket;
    public static HashMap<String,Player> otherPlayers;
    public static Player currentPlayer ;

    //database
    public static Preferences pres;
    //tạo room
    private Skin skin;
    private Table container;
    public static int room[];
    //
    private Texture imgLoad;


    public PlayGame(Main game) {
        super(game);
        toastFactory = new Toast.ToastFactory.Builder()
                .font(Main.myFont)
                .build();

;

        // tạo object
        otherPlayers = new HashMap<>();
        currentPlayer = new Player();




        //nhập tt
        formInfor= new FormValidator();
        formInfor.setName("formInfor");
        formInfor.setPosition(Main.APP_WIDTH/2-Main.APP_WIDTH/6,Main.APP_HEIGHT/2-Main.APP_HEIGHT/6);
        //tạo database
        pres = Gdx.app.getPreferences("data");
        String _name = pres.getString("name","");
        if(_name.isEmpty() || _name ==null){
            stage.addActor(formInfor);
        }
        else{
            CLOSE_FORM = 1;
            USER_NAME = _name;
            String _phone = pres.getString("phone","");
            long _coin = pres.getLong("coin",2000000);
            currentPlayer.setCoin(_coin);
            USER_PHONE = _phone;
        }


        //room
        room = new int[37];
        //img load
        imgLoad = game.manager.get("imageLobby/loadingText.png",Texture.class);

        // tạo câu chào
        toast = toastFactory.create("Chào mừng ########", Toast.Length.SHORT);







    }

    @Override
    public void show() {
        super.show();
        if(CLOSE_FORM ==5){
            createLobby();
            stage.addActor(lblCoin);
            stage.addActor(lblName);

        }


    }


    @Override
    public void render(float delta) {

        // cam
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // kiểm tra kết nối mạng


        //
        Gdx.gl.glClearColor(1,0,0,1);// xoá mh
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        game.batch.draw(bg_Sanh,0,0,Main.APP_WIDTH,Main.APP_HEIGHT);

        // vẽ người chơi khác
        /*for(Map.Entry<String,Player> entry : otherPlayers.entrySet()){
            game.batch.draw(entry.getValue().getTexture(),100,200,100,100);

        }*/

        //ẩn form register

        if(CLOSE_FORM == 5){
            createRoom(1);
        }
        if((CLOSE_FORM ==1 || mTimes ==1)  ){
            WelcomeUser(delta);
            //vẽ tên



        }
        else if(CLOSE_FORM ==2){
            onClickConnectFail(delta);

        }
         if(CLOSE_FORM ==3|| CLOSE_FORM ==4|| CLOSE_FORM ==5){
            game.batch.setProjectionMatrix(camera.combined);
            if(room[0]!=11)
                game.batch.draw(imgLoad,Main.APP_WIDTH/2-250,Main.APP_HEIGHT/2-100,500,200);
            //updateServer(delta);
            createLobby();
             lblCoin.setText(currentPlayer.getCoin()+"");
        }


        game.batch.end();



        stage.act(delta);
        stage.draw();

        new HttpManager();
        if(HttpManager.LOI==1){
            createToast(delta);

        }
        if(CLOSE_FORM == 4){
            toast.render(delta);
            if(mTime>5){
                mTime=0;
                CLOSE_FORM =3;
            }
            mTime +=delta;
        }
        //Gdx.app.log("ROOM",room[5]+"");











    }
    //xử lý coin
    @Override
    public void dispose() {
        super.dispose();
        stage.clear();
        Gdx.app.log("OUT","!123");
        pres.putLong("coin", currentPlayer.getCoin());
        pres.flush();
    }


    private void onClickConnectFail(float delta) {
        game.batch.setProjectionMatrix(camera.combined);
        if(mTime>3.6)
            Gdx.app.exit();
        toast.render(delta);
        mTime+=delta;

    }

    private void WelcomeUser(float delta) {
        game.batch.setProjectionMatrix(camera.combined);
        if(mTime>3.5){
            mTime =0;
            mTimes = 0;
            return;
        }
        if(mTime==0f ){
            toast.setMsg("Chào mừng "+USER_NAME);
            formInfor.remove();
            // kết nối tới server
            ConnectSocket();
            ConfigSocket();

            // tạo tên và coin
            Label.LabelStyle font = new Label.LabelStyle(Main.myFont_24, Color.WHITE);
             lblName = new Label(USER_NAME,font);

            lblCoin = new Label("$" + chuyeDoiTien(currentPlayer.getCoin()),font);
            lblName.setPosition(Main.APP_HEIGHT/10+Main.APP_HEIGHT/20,Main.APP_HEIGHT - Main.APP_HEIGHT/8);
            lblCoin.setPosition(Main.APP_HEIGHT/10+Main.APP_HEIGHT/20,Main.APP_HEIGHT - Main.APP_HEIGHT/5.8f);

            stage.addActor(lblName);
            stage.addActor(lblCoin);

            //tạo room ui
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
            skin.add("top", skin.newDrawable("default-round", Color.RED), Drawable.class);
            skin.add("star-filled", skin.newDrawable("white", Color.YELLOW), Drawable.class);
            skin.add("star-unfilled", skin.newDrawable("white", Color.GRAY), Drawable.class);
            container = new Table();

        }
        CLOSE_FORM = 3;
        mTimes = 1;
        toast.render(delta);
        mTime+=delta;
    }

    private void ConnectSocket() {
        try {
            socket = IO.socket("https://safe-citadel-28701.herokuapp.com/");
            socket.connect();

        } catch(Exception e){
            toast = toastFactory.create("Connect to server fail!", Toast.Length.LONG);
            CLOSE_FORM =2;
            System.out.println(e);
        }


    }

    private void ConfigSocket() {
       socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
           @Override
           public void call(Object... args) {

           }
       }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                try {

                    String id = object.getString("id");
                    currentPlayer.setId(id);
                    currentPlayer.setName(USER_NAME);
                    currentPlayer.setPhone(USER_PHONE);
                    currentPlayer.setRoom(0);
                    Gdx.app.log("socketID",id);
                    //lưu vào database
                    pres.putString("id",currentPlayer.getId());
                    pres.putString("name",USER_NAME);
                    pres.putString("phone",USER_PHONE);
                    pres.putLong("coin",currentPlayer.getCoin());
                    pres.flush();

                    //put player lên server
                    JSONObject data = new JSONObject();
                    try{
                        data.put("id",currentPlayer.getId());
                        data.put("name",USER_NAME);
                        data.put("phone",USER_PHONE);
                        data.put("coin",currentPlayer.getCoin());
                        data.put("room",currentPlayer.getRoom());
                        socket.emit("info_players",data);

                    }
                    catch (JSONException e){
                        Gdx.app.log("LOI",e.toString());
                    }

                }
                catch (JSONException e){
                    Gdx.app.log("LOI_socketID",e.toString());
                }


            }
        }).on("info_players", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                try {
                    String _id = object.getString("id");
                    String _name = object.getString("name");
                    long _coin = object.getLong("coin");
                    int _room = object.getInt("room");
                    Player player = new Player(_id,_name,_room,_coin);
                    otherPlayers.put(_id,player);
                    Gdx.app.log("newPlayer ID",_coin+"");
                }
                catch (JSONException e){
                    Gdx.app.log("LOI_info_players",e.toString());
                }
            }
        }).on("getPlayer", new Emitter.Listener() {// lấy tất cả tt của người tham gia trước
            @Override
            public void call(Object... args) {
                JSONArray array = (JSONArray) args[0];
                Gdx.app.log("LEN",array.length()+"");
                try {
                    for(int i =0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        String _id = object.getString("id");
                        String _name = object.getString("name");
                        String _phone = object.getString("phone");
                        long _coin = object.getLong("coin");
                        Player otherPlayer = new Player(_id,_name,_phone,_coin);
                        otherPlayers.put(_id,otherPlayer);
                        Gdx.app.log("LOI_getPlayer",_coin+"");

                    }

                }
                catch (Exception e){
                    Gdx.app.log("LOI_getPlayer",e.toString());
                }
            }
        }).on("getRoom", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONArray array = (JSONArray) args[0];
               try {
                   for(int i =1;i<array.length();i++){
                       int object = array.getInt(i);
                       room[i] = object;

                   }
                   //tạo room
                   createRoom(0);
                   room[0] = 11;
               }
               catch (Exception e){
                   Gdx.app.log("LOI_getRoom",e.toString());
               }
           }
       }).on("join_room", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   int stt = data.getInt("stt");
                   String _id = data.getString("id");
                   otherPlayers.get(_id).setRoom(stt);
                   room[stt] ++;
                   createRoom(1);
               } catch (JSONException e) {
                   Gdx.app.log("LOI_JOINROOM",e.toString());
               }
               Gdx.app.log("ROOM 1",room[1]+"");
           }
       }).on("out_room", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   int stt = data.getInt("stt");
                   String _id = data.getString("id");
                   otherPlayers.get(_id).setRoom(0);
                   room[stt] --;
                   /*for(int  i=1;i<=7;i++){
                       if(playerInRoom.get(i).getId().equals(_id)){
                           player[i-1] = false;
                           playerInRoom.remove(_id);

                       }
                   }*/

                   createRoom(1);
               } catch (JSONException e) {
                   Gdx.app.log("LOI_OUTROOM",e.toString());
               }
               Gdx.app.log("ROOM 1",room[1]+"");
           }
       }).on("dongbocoin", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   String _id = data.getString("id");
                   long _coin = data.getLong("coin");
                   if(_id.equals(currentPlayer.getId()))
                       return;
                   otherPlayers.get(_id).setCoin(_coin);
                   for(int i=1;i<=Room.playerInRoom.size();i++){
                       if(Room.playerInRoom.get(i).getId().equals(_id)){
                           Room.playerInRoom.get(i).setCoin(_coin);
//                           Room.lblCoinOther[i].setText(chuyeDoiTien(_coin));
                       }
                   }



               } catch (JSONException e) {
                   Gdx.app.log("LOI_CLIENT_DONG_BO_COIN",e.toString());
               }
           }
       }).on("playerDisconnect", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject object = (JSONObject) args[0];
               try {
                   String id = object.getString("id");
                   int sRoom = object.getInt("room");
                   if(sRoom!=0)
                       room[sRoom]--;
                   otherPlayers.remove(id);
                   //chung room thì sao?
                   //ngoài sảnh
                   createRoom(1);

               }
               catch (JSONException e){
                   Gdx.app.log("LOI_playerDisconnect",e.toString());
               }
           }
       });

    }

    private void createLobby() {
        game.batch.setProjectionMatrix(camera.combined);
        //vẽ avt
        Texture avt=  Main.manager.<Texture>get("avt.png",com.badlogic.gdx.graphics.Texture .class);
        Texture imgKhung = Main.manager.get("imageLobby/khung.png",Texture.class);
        game.batch.draw(avt,0 ,Main.APP_HEIGHT - Main.APP_HEIGHT/6,Main.APP_HEIGHT/10,Main.APP_HEIGHT/10);
        if(currentPlayer.getCoin()<=100000000)
            game.batch.draw(imgKhung,Main.APP_HEIGHT/10+5 ,Main.APP_HEIGHT - 2*Main.APP_HEIGHT/10,Main.APP_HEIGHT/4,Main.APP_HEIGHT/10+Main.APP_HEIGHT/20);
        else
            game.batch.draw(imgKhung,Main.APP_HEIGHT/10+5 ,Main.APP_HEIGHT - 2*Main.APP_HEIGHT/10,Main.APP_HEIGHT/3,Main.APP_HEIGHT/10+Main.APP_HEIGHT/20);
    }

    private String chuyeDoiTien(long tien) {
        int dem =0;
        StringBuilder kq = new StringBuilder("");
        while (tien>0){
            kq.insert(0,tien%10);
            dem++;
            tien/=10;
            if(dem==3 &&tien>0 ){
                kq.insert(0,',');
                dem=0;
            }

        }
        return  kq.toString();
    }
    private void createToast(float delta) {
        Gdx.app.log("TIME",mTime+"");
        game.batch.setProjectionMatrix(camera.combined);

        toast  = toastFactory.create("Please check the internet again", Toast.Length.LONG);


        toast.render(delta);
        if(mTime >6){
            game.setScreen(new MhLogin(game));
            dispose();
        }
        mTime +=delta;
        System.out.println(mTime);


    }
    public void createRoom(int dk){
        if(CLOSE_FORM == 5)
            CLOSE_FORM =3;
        PagedScrollPane scroll = new PagedScrollPane();
        scroll.setFlingTime(0.1f);
        scroll.setPageSpacing(25);
        int c = 1;
        for (int l = 0; l < 3; l++) {
            Table levels = new Table().pad(50);
            levels.defaults().pad(20, 40, 20, 40);
            for (int y = 0; y < 3; y++) {
                levels.row();
                for (int x = 0; x < 4; x++) {
                    levels.add(getLevelButton(c++)).expand().fill();
                }
            }
            scroll.addPage(levels);
        }
        if(dk !=0)
            container.remove();
        container.clear();
        stage.addActor(container);
        container.setFillParent(true);
        container.add(scroll).expand().padTop(Main.APP_HEIGHT/5).fill();

    }

    public Button getLevelButton(int level) {
        Button button = new Button(skin);
        Button.ButtonStyle style = button.getStyle();
        style.up = 	style.down = null;

        Label label = new Label(Integer.toString(level), skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);

        button.stack(new Image(skin.getDrawable("top")), label).expand().fill();
        Table starTable = new Table();
        starTable.defaults().pad(5);

        int star =0;
        if (room[level]!=0) {
            for (; star < room[level]; star++) {
                starTable.add(new Image(skin.getDrawable("star-filled"))).width(20).height(20);
            }
        }
        for ( ; star < 7; star++) {
            starTable.add(new Image(skin.getDrawable("star-unfilled"))).width(20).height(20);

        }
        // }

        button.row();
        button.add(starTable).height(30);

        button.setName( String.valueOf(level) );
        button.addListener(levelClickListener);
        return button;
    }
    public ClickListener levelClickListener = new ClickListener() {
        @Override
        public void clicked (InputEvent event, float x, float y) {
            Gdx.app.log("Click: " , event.getListenerActor().getName());
            int soRoom = Integer.parseInt(event.getListenerActor().getName());
            if(room[soRoom]>=7){
                toast = toastFactory.create("Phòng đã đầy", Toast.Length.LONG);
                CLOSE_FORM =4;

                return;
            }
            JSONObject data = new JSONObject();
            currentPlayer.setRoom(soRoom);
            try {
                data.put("id",currentPlayer.getId());
                data.put("stt",soRoom);
                data.put("coin",currentPlayer.getCoin());
                socket.emit("join_room",data);
                room[soRoom]++;
                //chuyển mh
                dispose();
                game.setScreen(new Room(game));

            } catch (JSONException e) {
                Gdx.app.log("LOIROOM: " , e.toString());
            }

        }

    };


    private void updateServer(float delta) {
        mTime += delta;
        /*if(mTime >= UPDATE_TIME && currentPlayer !=null){
            JSONObject data = new JSONObject();
            try{
                //data.put("id")
                socket.emit("update",data);

            }
            catch (JSONException e){

            }
        }*/

    }
}
