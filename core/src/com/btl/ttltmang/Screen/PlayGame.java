package com.btl.ttltmang.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Object.Player;
import com.btl.ttltmang.Tool.FormValidator;
import com.btl.ttltmang.Tool.PagedScrollPane;
import com.btl.ttltmang.Tool.Toast;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

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
    private Table tabelRoom;
    public static int room[];
    //
    private Texture imgLoad;
    //avt
    public static Array<Texture> arrayAvtFull;
    public static Array<Texture> arrayAvt50;
    public static Array<Texture> arrayAvtChecked;

    public static  int avatarChoose =0;
    private ImageButton btnDoiAvatar;


    public PlayGame(Main game) {
        super(game);
        toastFactory = new Toast.ToastFactory.Builder()
                .font(Main.myFont)
                .build();

        //avt
        loadAvatar();

;

        // tạo object
        otherPlayers = new HashMap<>();
        currentPlayer = new Player();




        //nhập tt
        formInfor= new FormValidator();
        formInfor.setName("formInfor");
        formInfor.setPosition(Main.APP_WIDTH/2-Main.APP_WIDTH/7,Main.APP_HEIGHT/2-Main.APP_HEIGHT/5);

        //dôi avt
        btnDoiAvatar = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(Main.manager.get("doi_avt.png",Texture.class))));
        btnDoiAvatar.setPosition(28,Main.APP_HEIGHT-145);
        stage.addActor(btnDoiAvatar);
        btnDoiAvatar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClickChangeAvatar();
            }
        });
        btnDoiAvatar.setVisible(false);

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
            int _avt = pres.getInteger("avt");
            currentPlayer.setCoin(_coin);
            currentPlayer.setAvatar(_avt);
            USER_PHONE = _phone;
            avatarChoose = _avt;
        }


        //room
        room = new int[37];
        //img load
        imgLoad = game.manager.get("imageLobby/loadingText.png",Texture.class);

        // tạo câu chào
        toast = toastFactory.create("Chào mừng ########", Toast.Length.SHORT);
        //tao tabel room
        tabelRoom = new Table();









    }

    private void onClickChangeAvatar() {
        tabelRoom.setVisible(false);
        btnDoiAvatar.setVisible(false);
        ImageButton btnClose = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(Main.manager.get("btn_exit.png",Texture.class))));
        final Table table = new Table(VisUI.getSkin());
        table.columnDefaults(0).left();
        final VisValidatableTextField txtName = new VisValidatableTextField(currentPlayer.getName());
        TextButton btnOK = new TextButton("OK",VisUI.getSkin());
        Label errorLabel = new Label("",VisUI.getSkin());
        errorLabel.setColor(Color.RED);
        SimpleFormValidator validator;
        validator = new SimpleFormValidator(btnOK, errorLabel, "smooth");

        final Array<VisImageButton> btnImgAvatar = new Array<>();
        for(int i=0;i<8;i++){
            VisImageButton.VisImageButtonStyle styles = new VisImageButton.VisImageButtonStyle();
            styles.imageChecked = new TextureRegionDrawable(new TextureRegion(PlayGame.arrayAvtChecked.get(i)));
            styles.imageUp = new TextureRegionDrawable(new TextureRegion(PlayGame.arrayAvt50.get(i)));
            // style.add(styles);
            VisImageButton img_avt = new VisImageButton(styles);
            btnImgAvatar.add(img_avt);

        }
        Label.LabelStyle lblStyle = new Label.LabelStyle(Main.myFont_24,Color.WHITE);
        table.add(new Label("Thông Tin",lblStyle)).colspan(4).center();
        table.add(btnClose);
        table.row();
        for(int i =0;i<8;i++){
            table.add(btnImgAvatar.get(i)).pad(5);
            if(i==3)
                table.row();

        }
        table.row();
        table.add(new Label("Name:",VisUI.getSkin())).bottom();
        table.add(txtName).padTop(10).fill().colspan(3);
        table.row();
        table.add(errorLabel).fill().colspan(3).padTop(10);
        table.add(btnOK).right().padTop(10);


        validator.notEmpty(txtName, "Name cannot be empty");


        //table.background(new TextureRegionDrawable(new TextureRegion(Main.manager.get("test.png",Texture.class))));
        table.setPosition(Main.APP_WIDTH/2-140,Main.APP_HEIGHT/2-105);
        table.setSize(280,210);
        stage.addActor(table);


        final int[] soAvt = {avatarChoose};

        btnImgAvatar.get(avatarChoose).setChecked(true);
        btnImgAvatar.get(0).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 0)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(0).setChecked(true);
                soAvt[0] = 0;
            }
        });

        btnImgAvatar.get(1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 1)
                    return;


                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(1).setChecked(true);
                soAvt[0] = 1;
            }
        });

        btnImgAvatar.get(2).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 2)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(2).setChecked(true);
                soAvt[0] = 2;
            }
        });

        btnImgAvatar.get(3).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 3)
                    return;


                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(3).setChecked(true);
                soAvt[0] = 3;
            }
        });

        btnImgAvatar.get(5).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 5)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(5).setChecked(true);
                soAvt[0] = 5;
            }
        });

        btnImgAvatar.get(6).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 6)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(6).setChecked(true);
                soAvt[0] = 6;
            }
        });

        btnImgAvatar.get(7).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 7)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(7).setChecked(true);
                soAvt[0] = 7;
            }
        });

        btnImgAvatar.get(4).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(soAvt[0] == 4)
                    return;

                btnImgAvatar.get(soAvt[0]).setChecked(false);
                btnImgAvatar.get(4).setChecked(true);
                soAvt[0] = 4;
            }
        });

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.remove();
                table.remove();
                tabelRoom.setVisible(true);
                btnDoiAvatar.setVisible(true);
            }
        });



        btnOK.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.remove();
                tabelRoom.setVisible(true);
                btnDoiAvatar.setVisible(true);
                avatarChoose = soAvt[0];
                currentPlayer.setName(txtName.getText().trim());
                lblName.setText(txtName.getText().trim());
                //luu tt lại ở local
                pres.putString("name",txtName.getText().trim());
                pres.putInteger("avt",avatarChoose);
                pres.flush();

                //đồng bộ với server
                JSONObject data = new JSONObject();
                try {
                    data.put("name",txtName.getText().trim());
                    data.put("id",currentPlayer.getId());
                    data.put("avt",avatarChoose);
                    socket.emit("doi_thong_tin",data);

                } catch (JSONException e) {
                   Gdx.app.log("LOI_DOI_AVT",e.toString());
                }


            }
        });


    }

    private void loadAvatar() {
        Texture img = Main.manager.get("avatar/avt_50.png",Texture.class);
        Texture img_checked = Main.manager.get("avatar/avt_checked.png",Texture.class);
        Texture img2 = Main.manager.get("avatar/avt2_50.png",Texture.class);
        Texture img2_checked = Main.manager.get("avatar/avt2_checked.png",Texture.class);
        Texture img3 = Main.manager.get("avatar/avt3_50.png",Texture.class);
        Texture img3_checked = Main.manager.get("avatar/avt3_checked.png",Texture.class);
        Texture img4 = Main.manager.get("avatar/avt4_50.png",Texture.class);
        Texture img4_checked = Main.manager.get("avatar/avt4_checked.png",Texture.class);
        Texture img5 = Main.manager.get("avatar/avt5_50.png",Texture.class);
        Texture img5_checked = Main.manager.get("avatar/avt5_checked.png",Texture.class);
        Texture img6 = Main.manager.get("avatar/avt6_50.png",Texture.class);
        Texture img6_checked = Main.manager.get("avatar/avt6_checked.png",Texture.class);
        Texture img7 = Main.manager.get("avatar/avt7_50.png",Texture.class);
        Texture img7_checked = Main.manager.get("avatar/avt7_checked.png",Texture.class);
        Texture img8 = Main.manager.get("avatar/avt8_50.png",Texture.class);
        Texture img8_checked = Main.manager.get("avatar/avt8_checked.png",Texture.class);
        Texture img11 = Main.manager.get("avatar/avt.png",Texture.class);
        Texture img22 = Main.manager.get("avatar/avt2.png",Texture.class);
        Texture img33 = Main.manager.get("avatar/avt3.png",Texture.class);
        Texture img44 = Main.manager.get("avatar/avt4.png",Texture.class);
        Texture img55 = Main.manager.get("avatar/avt5.png",Texture.class);
        Texture img66 = Main.manager.get("avatar/avt6.png",Texture.class);
        Texture img77 = Main.manager.get("avatar/avt7.png",Texture.class);
        Texture img88 = Main.manager.get("avatar/avt8.png",Texture.class);
        arrayAvtFull = new Array<>();
        arrayAvtFull.add(img11);
        arrayAvtFull.add(img22);
        arrayAvtFull.add(img33);
        arrayAvtFull.add(img44);
        arrayAvtFull.add(img55);
        arrayAvtFull.add(img66);
        arrayAvtFull.add(img77);
        arrayAvtFull.add(img88);

        arrayAvt50 = new Array<>();
        arrayAvt50.add(img);
        arrayAvt50.add(img2);
        arrayAvt50.add(img3);
        arrayAvt50.add(img4);
        arrayAvt50.add(img5);
        arrayAvt50.add(img6);
        arrayAvt50.add(img7);
        arrayAvt50.add(img8);

        arrayAvtChecked = new Array<>();
        arrayAvtChecked.add(img_checked);
        arrayAvtChecked.add(img2_checked);
        arrayAvtChecked.add(img3_checked);
        arrayAvtChecked.add(img4_checked);
        arrayAvtChecked.add(img5_checked);
        arrayAvtChecked.add(img6_checked);
        arrayAvtChecked.add(img7_checked);
        arrayAvtChecked.add(img8_checked);

    }

    @Override
    public void show() {
        super.show();
        if(CLOSE_FORM ==5){
            createLobby();
            stage.addActor(lblCoin);
            stage.addActor(lblName);
            stage.addActor(btnDoiAvatar);

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
             lblCoin.setText("$" + chuyeDoiTien(currentPlayer.getCoin()));
        }
        if(!tabelRoom.isVisible())
            game.batch.draw(Main.manager.get("khung_doi_avt.png",Texture.class),
                    Main.APP_WIDTH/2-155,Main.APP_HEIGHT/2-95,260,212);




        game.batch.end();



        stage.act(delta);
        stage.draw();

       /* new HttpManager();
        if(HttpManager.LOI==1){
            createToast(delta);

        }*/
        /*if(!socket.connected())
            createToast(delta);
        Gdx.app.log("CHECK",socket.connected()+"");*/

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
       // Gdx.app.log("OUT","!123");
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


        }
        CLOSE_FORM = 3;
        mTimes = 1;
        toast.render(delta);
        mTime+=delta;
    }

    private void ConnectSocket() {
        try {
            socket = IO.socket("https://server-gamebaucua.herokuapp.com/");
            socket.connect();

        } catch(Exception e){
            toast = toastFactory.create("Connect to server fail!", Toast.Length.LONG);
            CLOSE_FORM =2;
            System.out.println(e);
        }


    }

    private void ConfigSocket() {
       socket.on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                try {

                    String id = object.getString("id");
                    currentPlayer.setId(id);
                    currentPlayer.setName(USER_NAME);
                    currentPlayer.setPhone(USER_PHONE);
                    currentPlayer.setRoom(0);
                    //Gdx.app.log("socketID",id);
                    //lưu vào database
                    pres.putString("id",currentPlayer.getId());
                    pres.putString("name",USER_NAME);
                    pres.putString("phone",USER_PHONE);
                    pres.putLong("coin",currentPlayer.getCoin());
                    pres.putInteger("avt",avatarChoose);
                    pres.flush();

                    //put player lên server
                    JSONObject data = new JSONObject();
                    try{
                        data.put("id",currentPlayer.getId());
                        data.put("name",USER_NAME);
                        data.put("phone",USER_PHONE);
                        data.put("coin",currentPlayer.getCoin());
                        data.put("room",currentPlayer.getRoom());
                        data.put("avt",avatarChoose);
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
                    int _avt = object.getInt("avt");
                    Player player = new Player(_id,_name,_room,_coin,_avt);
                    otherPlayers.put(_id,player);
                   // Gdx.app.log("newPlayer ID",_coin+"");
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
                        int _room = object.getInt("room");
                        int _avt = object.getInt("avt");
                        Player otherPlayer = new Player(_id,_name,_phone,_room,_coin,_avt);
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
                   btnDoiAvatar.setVisible(true);
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
               //Gdx.app.log("ROOM 1",room[1]+"");
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
              // Gdx.app.log("ROOM 1",room[1]+"");
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



               } catch (Exception e) {
                   Gdx.app.log("LOI_CLIENT_DONG_BO_COIN",e.toString());
               }
           }
       }).on("doi_thong_tin", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   String _name = data.getString("name");
                   int _avt = data.getInt("avt");
                   String _id = data.getString("id");
                   for(int i =0;i<otherPlayers.size();i++){
                       otherPlayers.get(_id).setAvatar(_avt);
                       otherPlayers.get(_id).setName(_name);
                   }
               } catch (JSONException e) {
                   Gdx.app.log("LOI_DOITT",e.toString());
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
       // Texture avt=  Main.manager.<Texture>get("avt.png",com.badlogic.gdx.graphics.Texture .class);
        Texture imgKhung = Main.manager.get("imageLobby/khung.png",Texture.class);
        game.batch.draw(arrayAvtFull.get(avatarChoose),5 ,Main.APP_HEIGHT - 110,Main.APP_HEIGHT/10,Main.APP_HEIGHT/10);
        if(currentPlayer.getCoin()<=100000000)
            game.batch.draw(imgKhung,Main.APP_HEIGHT/10+10 ,Main.APP_HEIGHT - 2*Main.APP_HEIGHT/10,Main.APP_HEIGHT/4,Main.APP_HEIGHT/10+Main.APP_HEIGHT/20);
        else
            game.batch.draw(imgKhung,Main.APP_HEIGHT/10+10 ,Main.APP_HEIGHT - 2*Main.APP_HEIGHT/10,Main.APP_HEIGHT/3,Main.APP_HEIGHT/10+Main.APP_HEIGHT/20);
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
            tabelRoom.remove();
        tabelRoom.clear();
        stage.addActor(tabelRoom);
        tabelRoom.setFillParent(true);
        tabelRoom.add(scroll).expand().padTop(Main.APP_HEIGHT/5).fill();

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
           // Gdx.app.log("Click: " , event.getListenerActor().getName());
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



}
