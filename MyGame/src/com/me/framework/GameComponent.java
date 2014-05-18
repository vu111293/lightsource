package com.me.framework;

import com.me.framework.interfaces.IComponent;
import com.me.framework.interfaces.IGameService;


public abstract class GameComponent extends CollectionItem implements IComponent {

	protected IGameService services;
	
	public GameComponent(IGameService services)
	{
	    super();
		this.services = services;	
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGameService getGameService() {
		// TODO Auto-generated method stub
		return services;
	}
}
