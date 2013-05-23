package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SceneManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

public class GameOverLayer extends Layer implements OnClickListener {

	private Sprite arrow;
	private Sprite background;
	private ButtonSprite continueSprite;
	private ButtonSprite exitSprite;
	private GameScene currentScene;

	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (getY() > GameActivity.getHeight() / 2f) {
				setPosition(
						getX(),
						Math.max(OptionsLayer.getInstance().getY()
								- (3600 * (pSecondsElapsed)),
								GameActivity.getHeight() / 2f));
			} else {
				ResourcesManager.getInstance().engine
						.unregisterUpdateHandler(this);
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
			if (getY() < GameActivity.getHeight() / 2f + 480f) {
				setPosition(
						getX(),
						Math.min(getY()
								+ (3600 * (pSecondsElapsed)),
								GameActivity.getHeight() / 2f + 480f));
			} else {
				ResourcesManager.getInstance().engine
						.unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}

		@Override
		public void reset() {
		}
	};


	public GameOverLayer(GameScene scene) {
		super();
		this.currentScene = scene;
	}

	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		background = new Sprite(0, 0, ResourcesManager.getInstance().gameOver,
				ResourcesManager.getInstance().vbom);
		background.setHeight(440f);
		background.setWidth(760f);
		continueSprite = new ButtonSprite(GameActivity.getWidth() * 0.5f,
				GameActivity.getHeight() * 0.5f - 80,
				ResourcesManager.getInstance().continueGame,
				ResourcesManager.getInstance().vbom, this);
		continueSprite.setScale(3f);
		exitSprite = new ButtonSprite(GameActivity.getWidth() * 0.5f,
				continueSprite.getY() - 80,
				ResourcesManager.getInstance().exitGame,
				ResourcesManager.getInstance().vbom, this);
		exitSprite.setScale(3f);
		this.registerTouchArea(continueSprite);
		this.registerTouchArea(exitSprite);
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
		background.attachChild(continueSprite);
		background.attachChild(exitSprite);
		this.attachChild(background);
		this.setPosition(GameActivity.getWidth() / 2f,
				GameActivity.getHeight() / 2f + 480f);
	}

	@Override
	public void onShowLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(SlideIn);
	}

	private void createArrow(float pX, float pY) {
		arrow = new Sprite(pX, pY, ResourcesManager.getInstance().arrow,
				ResourcesManager.getInstance().vbom);
		arrow.setScale(3f);
		background.attachChild(arrow);
	}

	@Override
	public void onHideLayer() {
		ResourcesManager.getInstance().engine.registerUpdateHandler(SlideOut);
	}

	@Override
	public void onUnloadLayer() {
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pButtonSprite.equals(continueSprite)) {
			createArrow(pButtonSprite.getX() - pButtonSprite.getWidth() * 2,
					pButtonSprite.getY());
			SceneManager.getInstance().createTempGameScene(
					ResourcesManager.getInstance().engine,
					SceneManager.getInstance().getCurrentlevel());
		} else {
			createArrow(pButtonSprite.getX() - pButtonSprite.getWidth() * 2,
					pButtonSprite.getY());
			currentScene.disposeScene();
			SceneManager.getInstance().createMenuScene();
		}

	}
}
