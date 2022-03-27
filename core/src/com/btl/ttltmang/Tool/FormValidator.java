package com.btl.ttltmang.Tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Screen.PlayGame;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class FormValidator extends VisWindow {
    public FormValidator() {
        super("Register User");
        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton acceptButton = new VisTextButton("OK");


        final Array<VisImageButton> btnImgAvatar = new Array<>();
        for(int i=0;i<8;i++){
           // style.add(new VisImageButton.VisImageButtonStyle().imageUp = new TextureRegionDrawable(new TextureRegion(PlayGame.arrayAvt50.get(0))));
            VisImageButton.VisImageButtonStyle styles = new VisImageButton.VisImageButtonStyle();
            styles.imageChecked = new TextureRegionDrawable(new TextureRegion(PlayGame.arrayAvtChecked.get(i)));
            styles.imageUp = new TextureRegionDrawable(new TextureRegion(PlayGame.arrayAvt50.get(i)));
           // style.add(styles);
            VisImageButton img_avt = new VisImageButton(styles);
            btnImgAvatar.add(img_avt);

        }






        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        final VisValidatableTextField firstNameField = new VisValidatableTextField();
        final VisValidatableTextField phone = new VisValidatableTextField();



        VisTable buttonTable = new VisTable(true);
        VisTable imgTable = new VisTable(true);

        buttonTable.add(errorLabel).expand().fill();
        buttonTable.add(acceptButton);
        for(int i =0;i<8;i++){
            imgTable.add(btnImgAvatar.get(i)).pad(5).padTop(10).left();
            if(i==3)
                imgTable.row();

        }
        add(imgTable).colspan(2).center();
        row();
        add(new VisLabel("Name: "));
        add(firstNameField).expand().fill();
        row();
        add(new VisLabel("Phone: "));
        add(phone).expand().fill();
        row();

        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
        validator.notEmpty(firstNameField, "Name cannot be empty");
        //validator.notEmpty(lastNameField, "last name cannot be empty");
        validator.notEmpty(phone, "Phone cannot be empty");
        validator.floatNumber(phone, "Phone must be a number");
        validator.valueGreaterThan(phone,"Phone must have 10 number", 100000000, true);
        phone.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(phone.getText().length() >10){
                    event.cancel();
                    //Gdx.app.log("asd", phone.getText().substring(0,10));

                }
            }
        });


        btnImgAvatar.get(0).setChecked(true);
        btnImgAvatar.get(0).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 0)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(0).setChecked(true);
                PlayGame.avatarChoose = 0;
            }
        });

        btnImgAvatar.get(1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 1)
                    return;


                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(1).setChecked(true);
                PlayGame.avatarChoose = 1;
            }
        });

        btnImgAvatar.get(2).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 2)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(2).setChecked(true);
                PlayGame.avatarChoose = 2;
            }
        });

        btnImgAvatar.get(3).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 3)
                    return;


                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(3).setChecked(true);
                PlayGame.avatarChoose = 3;
            }
        });

        btnImgAvatar.get(5).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 5)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(5).setChecked(true);
                PlayGame.avatarChoose = 5;
            }
        });

        btnImgAvatar.get(6).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 6)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(6).setChecked(true);
                PlayGame.avatarChoose = 6;
            }
        });

        btnImgAvatar.get(7).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 7)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(7).setChecked(true);
                PlayGame.avatarChoose = 7;
            }
        });

        btnImgAvatar.get(4).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(PlayGame.avatarChoose == 4)
                    return;

                btnImgAvatar.get( PlayGame.avatarChoose).setChecked(false);
                btnImgAvatar.get(4).setChecked(true);
                PlayGame.avatarChoose = 4;
            }
        });





        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                PlayGame.CLOSE_FORM =1;
                PlayGame.USER_NAME = firstNameField.getText();
                if(phone.getText().length()>10)
                     PlayGame.USER_PHONE = phone.getText().substring(0,10);
                else
                    PlayGame.USER_PHONE = phone.getText();
                //PlayGame.toast = PlayGame.toastFactory.create("Chào mừng "+firstNameField.getText(), Toast.Length.LONG);
                close();



            }
        });

        /*img_avt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {




            }
        });*/


        setColor(Color.YELLOW);
        pack();
        setSize(Main.APP_WIDTH/3.5f,Main.APP_HEIGHT/2.5f);
    }

}
