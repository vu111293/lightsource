package com.me.framework.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.me.app.towerdefense.sceens.PauseScene;
import com.me.framework.interfaces.IGameService;



public class UIManager extends Stage {

	boolean blockOtherInputs = false;
	
	private Window mainMenu;
	
	SpriteBatch batch;
	private Group components;
	
	IGameService service;

	public UIManager(String title, Skin skin, IGameService service) {
		super();
		setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		mainMenu = new Window(title, skin);
		addActor(mainMenu);
		
		this.components = getRoot();
		this.batch = getSpriteBatch();
		
		this.service = service;
		
		Gdx.input.setCatchMenuKey(true);
		
		
	}
	
	public boolean isMenuVisible(){
		return mainMenu.isVisible();
	}
	
	public void setMenuVisible(boolean visible){
		if(visible)
			//always make main menu on top when it's visible
			mainMenu.setZIndex(100);
		
		for(Actor actor : getActors()){
			if(actor != mainMenu){
				if(visible)
					actor.setTouchable(Touchable.disabled);
				else
					actor.setTouchable(Touchable.enabled);
			}
			else
			{
				mainMenu.setVisible(visible);
				mainMenu.setTouchable(Touchable.enabled);
				// make the windows in screen center
				float windowX = (Gdx.graphics.getWidth() - mainMenu.getWidth())/2f;
				float windowY = (Gdx.graphics.getHeight() - mainMenu.getHeight())/2f;
				mainMenu.setPosition(windowX, windowY);
			}
		}
	}

	
	@Override
	public boolean keyDown(int keyCode) {
		if (Keys.ESCAPE == keyCode || Keys.MENU == keyCode) {
			service.getScene(PauseScene.class).setInputProcessor();
			service.getScene(PauseScene.class).setState(true, true);
			service.changeScene(PauseScene.class);
		}
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return super.touchDown(screenX, screenY, pointer, button);		
	}
	
	public Window getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(Window mainMenu) {
		this.mainMenu = mainMenu;
	}

	public Window getWindows() {
		return mainMenu;
	}
	
	@Override
	public void draw(){

		components.draw(batch, 1);
		Table.drawDebug(this);
	}

	public IGameService getGameServices() {
	    return service;
	}
}
