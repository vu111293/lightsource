package com.me.app.towerdefense.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.app.towerdefense.Player;
import com.me.app.towerdefense.weapon.Weapon;
import com.me.framework.interfaces.IGameService;

public abstract class BaseGUI extends Table {

    TextButton btnCancel;
    Color defaultColor;
    Player player;
    Weapon weapon;
    public BaseGUI(Skin skin){
	super(skin);
	btnCancel = new TextButton("Cancel", skin);
	btnCancel.addListener(new ChangeListener() {
	    
	    @Override
	    public void changed(ChangeEvent event, Actor actor) {
		hide();
		Player.hidePreBuild();
	    }
	});
	if(defaultColor == null)
		defaultColor = new Color(btnCancel.getColor());
		onBuild(skin);
    }
    
    public abstract void addListener(IGameService service,int x, int y);
    
    protected void onBuild(Skin skin){
		row().fill().expandX();
		add(btnCancel).expandX().align(Align.left);
		pack();
		setVisible(false);
	    }
	    public void checkUI(TextButton ui, String text, int gold){
		if(player.gold >= gold)
		{
		    ui.setColor(defaultColor);
		    ui.setText(text + " ($" + gold + ")");
		}
		else
		{
		    ui.setColor(Color.RED);
		    ui.setText(text + " ($" + gold + ") - Not enough $");
		}
		pack();
    }
    public void show(){
    	setVisible(true);
    }
    public void hide(){
    	setVisible(false);
    }

    public void setScreen(int x, int y) {
        this.setPosition(x, y);
    }

    public TextButton getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(TextButton btnCancel) {
        this.btnCancel = btnCancel;
    }
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }    
    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon w) {
        weapon=w;
    }
    
    
}
