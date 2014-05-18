package com.me.framework.event.listener;

import com.me.framework.event.TouchEvent;


public interface TouchListener extends InputListener, TouchUpListener, TouchDownListener {
	public boolean onTouchDragged(TouchEvent e);
}
