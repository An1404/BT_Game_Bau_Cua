package com.btl.ttltmang.Tool;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.btl.ttltmang.Main;
import com.btl.ttltmang.Screen.PlayGame;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
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
        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        final VisValidatableTextField firstNameField = new VisValidatableTextField();
        final VisValidatableTextField phone = new VisValidatableTextField();



        VisTable buttonTable = new VisTable(true);

        buttonTable.add(errorLabel).expand().fill();
        buttonTable.add(acceptButton);

        add(new VisLabel("Name: "));
        add(firstNameField).expand().fill().padTop(15);
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
        validator.integerNumber(phone, "Phone must be a number");

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                PlayGame.CLOSE_FORM =1;
                PlayGame.USER_NAME = firstNameField.getText();
                PlayGame.USER_PHONE = phone.getText();
                //PlayGame.toast = PlayGame.toastFactory.create("Chào mừng "+firstNameField.getText(), Toast.Length.LONG);
                close();



            }
        });


        setColor(Color.YELLOW);
        pack();
        setSize(Main.APP_WIDTH/4,Main.APP_HEIGHT/4);
    }

}
