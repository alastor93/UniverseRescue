package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

public class OptionsLayer extends Layer{
	
	private static final OptionsLayer INSTANCE = new OptionsLayer();
	private Sprite layer;
	
	IUpdateHandler mSlideInUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(OptionsLayer.this.layer.getY() > 0f) {
				OptionsLayer.this.layer.setY(Math.max(OptionsLayer.this.layer.getY() - (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), 0f));
			} else {
				ResourcesManager.getInstance().engine.unregisterUpdateHandler(this);
			}
		}
		@Override
		public void reset() {}
	};
	
	IUpdateHandler mSlideOutUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(OptionsLayer.this.layer.getY() < ((GameActivity.getHeight()/ 2f) + (OptionsLayer.this.layer.getHeight() / 2f))) {
				OptionsLayer.this.layer.setY(Math.min(OptionsLayer.this.layer.getY() + (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), (GameActivity.getWidth() / 2f) + (OptionsLayer.this.layer.getHeight() / 2f)));
			} else {
				ResourcesManager.getInstance().engine.unregisterUpdateHandler(this);
				//SceneManager.getInstance().hideLayer();
			}
		}
		
		@Override
		public void reset() {}
	};

	public static OptionsLayer getInstance() {
		return INSTANCE;
	}

	@Override
	public void onLoadLayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowLayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideLayer() {
		
	}

	@Override
	public void onUnloadLayer() {
		
	}

}
