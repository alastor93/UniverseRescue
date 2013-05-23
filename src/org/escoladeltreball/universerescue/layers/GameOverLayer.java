package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SceneManager;

public class GameOverLayer extends Layer {

	private static GameOverLayer obj = null;

	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (GameOverLayer.getInstance().getY() > GameActivity.getHeight() / 2f) {
				GameOverLayer.getInstance().setPosition(
						GameOverLayer.getInstance().getX(),
						Math.max(OptionsLayer.getInstance().getY()
								- (3600 * (pSecondsElapsed)),
								GameActivity.getHeight() / 2f));
			} else {
				ResourcesManager.getInstance().engine.unregisterUpdateHandler(this);
			}
		}

		@Override
		public void reset() {
		}
	};

	// Animates the layer to slide out through the top and tell the SceneManager
	// to hide it when it is off-screen;
	IUpdateHandler SlideOut = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (GameOverLayer.getInstance().getY() < GameActivity.getHeight() / 2f + 480f) {
				GameOverLayer.getInstance().setPosition(
						GameOverLayer.getInstance().getX(),
						Math.min(GameOverLayer.getInstance().getY()
								+ (3600 * (pSecondsElapsed)),
								GameActivity.getHeight() / 2f + 480f));
			} else {
				ResourcesManager.getInstance().engine.unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}

		@Override
		public void reset() {
		}
	};

	// Singleton
	public static GameOverLayer getInstance() {
		if (obj == null) {
			obj = new GameOverLayer();
		}
		return obj;
	}

	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		Sprite sprite = new Sprite(0, 0,ResourcesManager.getInstance().gameOver,ResourcesManager.getInstance().vbom );
		sprite.setHeight(440f);
		sprite.setWidth(760f);
		final float BackgroundX = 0f, BackgroundY = 0f;
		final float BackgroundWidth = 760f, BackgroundHeight = 440f;
		Rectangle smth = new Rectangle(BackgroundX, BackgroundY,
				BackgroundWidth, BackgroundHeight,
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()
						&& pTouchAreaLocalX < this.getWidth()
						&& pTouchAreaLocalX > 0
						&& pTouchAreaLocalY < this.getHeight()
						&& pTouchAreaLocalY > 0) {
					onHideLayer();
				}
				return true;
			}
		};
		smth.setColor(0f, 0f, 0f, 0.85f);
		this.attachChild(smth);
		this.registerTouchArea(smth);
		this.attachChild(sprite);
		this.setPosition(GameActivity.getWidth() / 2f,
				GameActivity.getHeight() / 2f + 480f);
	}

	@Override
	public void onShowLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(SlideIn);
	}

	@Override
	public void onHideLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(SlideOut);
	}

	@Override
	public void onUnloadLayer() {
	}
}
