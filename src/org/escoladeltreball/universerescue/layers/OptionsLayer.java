package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SceneManager;

public class OptionsLayer extends Layer{
	
	private static final OptionsLayer INSTANCE = new OptionsLayer();
	
	public static OptionsLayer getInstance(){
		return INSTANCE;
	}
	
	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if(OptionsLayer.getInstance().getY()>GameActivity.getHeight()/2f) {
				OptionsLayer.getInstance().setPosition(OptionsLayer.getInstance().getX(), Math.max(OptionsLayer.getInstance().getY()-(3600*(pSecondsElapsed)),GameActivity.getHeight()/2f));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
			}
		}
		@Override public void reset() {}
	};
	
	// Animates the layer to slide out through the top and tell the SceneManager to hide it when it is off-screen;
	IUpdateHandler SlideOut = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if(OptionsLayer.getInstance().getY()<GameActivity.getHeight()/2f+480f) {
				OptionsLayer.getInstance().setPosition(OptionsLayer.getInstance().getX(), Math.min(OptionsLayer.getInstance().getY()+(3600*(pSecondsElapsed)),GameActivity.getHeight()/2f+480f));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}
		@Override public void reset() {}
	};
	
	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		final float BackgroundX = 0f, BackgroundY = 0f;
		final float BackgroundWidth = 760f, BackgroundHeight = 440f;
		Rectangle smth = new Rectangle(BackgroundX,BackgroundY,BackgroundWidth,BackgroundHeight,ResourcesManager.getInstance().engine.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp() && pTouchAreaLocalX < this.getWidth() && pTouchAreaLocalX > 0 && pTouchAreaLocalY < this.getHeight() && pTouchAreaLocalY > 0) {
					onHideLayer();
				}
				return true;
			}
		};
		smth.setColor(0f, 0f, 0f, 0.85f);
		this.attachChild(smth);
		this.registerTouchArea(smth);
		
		ResourcesManager.getInstance().loadFonts();
		//Create the OptionsLayerTitle text for the Layer.
		Text OptionsLayerTitle = new Text(0,0,ResourcesManager.getInstance().defaultFont,"OPCIONES",ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
		OptionsLayerTitle.setPosition(0f,BackgroundHeight/2f-OptionsLayerTitle.getHeight());
		this.attachChild(OptionsLayerTitle);
		
		this.setPosition(GameActivity.getWidth()/2f, GameActivity.getHeight()/2f+480f);
	}

	@Override
	public void onShowLayer() {
		this.registerUpdateHandler(SlideIn);
	}

	@Override
	public void onHideLayer() {
		this.registerUpdateHandler(SlideOut);
	}
	@Override
	public void onUnloadLayer() {
	}
}
