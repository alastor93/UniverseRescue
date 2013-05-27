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

public class GameOverLayer extends Layer implements OnClickListener {
	//Attributes
	private Sprite arrow;
	private Sprite background;
	private ButtonSprite continueSprite;
	private ButtonSprite exitSprite;
	private boolean pressedContinue;
	private boolean pressedExit;
	//variable to use in the singleton 
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
				ResourcesManager.getInstance().engine
						.unregisterUpdateHandler(this);
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
		background = new Sprite(0, 0, ResourcesManager.getInstance().gameOver,
				ResourcesManager.getInstance().vbom);
		background.setHeight(440f);
		background.setWidth(760f);
		//Create the option continue
		continueSprite = new ButtonSprite(GameActivity.getWidth() * 0.5f,
				GameActivity.getHeight() * 0.5f - 80,
				ResourcesManager.getInstance().continueGame,
				ResourcesManager.getInstance().vbom, this);
		continueSprite.setScale(3f);
		//create the exit option
		exitSprite = new ButtonSprite(GameActivity.getWidth() * 0.5f,
				continueSprite.getY() - 80,
				ResourcesManager.getInstance().exitGame,
				ResourcesManager.getInstance().vbom, this);
		exitSprite.setScale(3f);
		this.registerTouchArea(continueSprite);
		this.registerTouchArea(exitSprite);
		this.setTouchAreaBindingOnActionDownEnabled(true);
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

	/**
	 * Create the arrow in the selected option
	 * @param pX
	 * @param pY
	 */
	private void createArrow(float pX, float pY) {
		arrow = new Sprite(pX, pY, ResourcesManager.getInstance().arrow,
				ResourcesManager.getInstance().vbom);
		arrow.setScale(3f);
		background.attachChild(arrow);
	}

	@Override
	public void onHideLayer() {
	}

	@Override
	public void onUnloadLayer() {
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pButtonSprite.equals(continueSprite)) {
			if (!pressedContinue) {
				background.detachChild(arrow);
				createArrow(
						pButtonSprite.getX() - pButtonSprite.getWidth() * 2,
						pButtonSprite.getY());
				pressedContinue = true;
				pressedExit = false;
			} else {
				background.detachChild(arrow);
				SceneManager.getInstance().hideLayer();
				SceneManager.getInstance().unloadGameScene();
				SceneManager.getInstance().createTempGameScene(
						ResourcesManager.getInstance().engine,
						SceneManager.getInstance().getCurrentlevel());
				pressedContinue = false;
			}
		} else {
			if (!pressedExit) {
				background.detachChild(arrow);
				createArrow(continueSprite.getX() - continueSprite.getWidth()
						* 2, pButtonSprite.getY());
				pressedExit = true;
				pressedContinue = false;
			} else {
				background.detachChild(arrow);
				SceneManager.getInstance().hideLayer();
				SceneManager.getInstance().backToLevelMenu();
				pressedExit = false;
			}
		}

	}

	@Override
	public void removeNext() {
	}
}
