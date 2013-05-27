package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SceneManager;

public class ControlsLayer extends Layer {
	
	//Attributes
	private static ControlsLayer obj = null;
	private Sprite controlinformation;

	// Animates the layer to slide in from the top.
		IUpdateHandler SlideIn = new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (ControlsLayer.getInstance().getY() > GameActivity.getHeight() / 2f) {
					ControlsLayer.getInstance().setPosition(
							ControlsLayer.getInstance().getX(),
							Math.max(ControlsLayer.getInstance().getY()
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
				if (ControlsLayer.getInstance().getY() < GameActivity.getHeight() / 2f + 480f) {
					ControlsLayer.getInstance().setPosition(
							ControlsLayer.getInstance().getX(),
							Math.min(ControlsLayer.getInstance().getY()
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
		public static ControlsLayer getInstance() {
			if (obj == null) {
				obj = new ControlsLayer();
			}
			return obj;
		}

		@Override
		public void onLoadLayer() {
			// Create and attach the info of the controls in the game that hides the Layer when touched.
			controlinformation = new Sprite(0, 0, ResourcesManager.getInstance().controlinfo,
					ResourcesManager.getInstance().vbom);
			controlinformation.setHeight(440f);
			controlinformation.setWidth(760f);

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
			//Set a semi-black background and attack the intro img
			smth.setColor(0f, 0f, 0f, 0.85f);
			this.attachChild(smth);
			this.registerTouchArea(smth);
			this.attachChild(controlinformation);
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
