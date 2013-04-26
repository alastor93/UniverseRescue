package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
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
		if(this.mHasLoaded) {
			return;
		}
		this.mHasLoaded = true;
		
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		
		final Rectangle fadableBGRect = new Rectangle(0f, 0f, GameActivity.getHeight(), GameActivity.getWidth(), ResourcesManager.getActivity().getVertexBufferObjectManager());
		fadableBGRect.setColor(0f, 0f, 0f, 0.8f);
		this.attachChild(fadableBGRect);
		
		this.attachChild(this.layer = new Sprite(0f, (GameActivity.getWidth() / 2f) + (ResourcesManager.getInstance().options_region.getHeight() / 2f), ResourcesManager.getInstance().options_region, ResourcesManager.getActivity().getVertexBufferObjectManager()));
		this.layer.setScale(1.5f / 1);

		this.setPosition(GameActivity.getWidth() / 2f, GameActivity.getHeight() / 2f);
	}

	@Override
	public void onShowLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(this.mSlideInUpdateHandler);
	}

	@Override
	public void onHideLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(this.mSlideOutUpdateHandler);
	}

	@Override
	public void onUnloadLayer() {
		
	}

}
