package com.me.framework.event.listener;

import com.me.framework.event.TouchEvent;

public interface TouchUpListener extends InputListener {
	public boolean onTouchUp(TouchEvent e);
}
