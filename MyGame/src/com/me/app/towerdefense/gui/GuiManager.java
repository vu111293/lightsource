package com.me.app.towerdefense.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.app.towerdefense.Player;
import com.me.app.towerdefense.weapon.Weapon;
import com.me.framework.BaseManager;
import com.me.framework.interfaces.IGameService;
import com.me.framework.ui.UIManager;

public class GuiManager extends BaseManager<String, BaseGUI> {

	//BuildTowerGUI buildTowerGUI;

	public GuiManager(Skin skin, UIManager manager) {
		super();
	}

	public void show(String key, IGameService service, int screenX, int screenY, int buildX, int buildY) {

		for (BaseGUI gui : collection.values()) {
			gui.hide();
		}
		BaseGUI gui = collection.get(key);
		gui.setScreen(screenX, screenY);
		gui.addListener(service, buildX, buildY);

		Player p = service.getService(Player.class);
		gui.setPlayer(p);
		gui.show();
	}
	
	public void setWeapon(String key,Weapon w)
    {
    	collection.get(key).setWeapon(w);
    }

	public void hide() {
		for (BaseGUI gui : collection.values()) {
			gui.hide();
		}
	}
}
