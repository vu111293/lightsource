package com.me.framework.event.listener;

import com.me.framework.event.TouchEvent;

public interface TouchDownListener extends InputListener {
	public boolean onTouchDown(TouchEvent e);
}
