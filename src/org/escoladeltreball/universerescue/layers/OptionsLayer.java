package org.escoladeltreball.universerescue.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SFXManager;
import org.escoladeltreball.universerescue.managers.SceneManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;

public class OptionsLayer extends Layer {

	private static OptionsLayer obj = null;

	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (OptionsLayer.getInstance().getY() > GameActivity.getHeight() / 2f) {
				OptionsLayer.getInstance().setPosition(
						OptionsLayer.getInstance().getX(),
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
			if (OptionsLayer.getInstance().getY() < GameActivity.getHeight() / 2f + 480f) {
				OptionsLayer.getInstance().setPosition(
						OptionsLayer.getInstance().getX(),
						Math.min(OptionsLayer.getInstance().getY()
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

	// Singleton
	public static OptionsLayer getInstance() {
		if (obj == null) {
			obj = new OptionsLayer();
		}
		return obj;
	}

	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		final float BackgroundX = 0f, BackgroundY = 0f;
		final float BackgroundWidth = 760f, BackgroundHeight = 440f;
		Rectangle smth = new Rectangle(BackgroundX, BackgroundY,
				BackgroundWidth, BackgroundHeight,
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager());
		smth.setColor(0f, 0f, 0f, 0.85f);
		this.attachChild(smth);

		ResourcesManager.getInstance().loadFonts();
		// Create the OptionsLayerTitle text for the Layer.
		Sprite OptionsLayerTitle = new Sprite(0, 0,
				ResourcesManager.getInstance().options_region,
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager());
		OptionsLayerTitle.setScale(2f);
		OptionsLayerTitle.setPosition(0f, BackgroundHeight / 2f
				- OptionsLayerTitle.getHeight());
		this.attachChild(OptionsLayerTitle);

		Text OptionsLayerBrightness = new Text(0, 0,
				ResourcesManager.getInstance().defaultFont, "Brightness",
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
				}
				return true;
			}
		};

		this.registerTouchArea(OptionsLayerBrightness);
		OptionsLayerBrightness.setPosition(0, OptionsLayerTitle.getY()
				- OptionsLayerBrightness.getHeight() - 20);

		Text OptionsLayerSound = new Text(0, 0,
				ResourcesManager.getInstance().defaultFont, "Sound",
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					SFXManager.toggleMusicMuted();
				}
				return true;
			}
		};

		this.registerTouchArea(OptionsLayerSound);
		OptionsLayerSound.setPosition(0f, OptionsLayerBrightness.getY()
				- OptionsLayerSound.getHeight() - 20);

		Text OptionsLayerReset = new Text(0, 0,
				ResourcesManager.getInstance().defaultFont, "Reset Data",
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					showMessageReset();
				}
				return true;
			}
		};
		this.registerTouchArea(OptionsLayerReset);
		OptionsLayerReset.setPosition(0f, OptionsLayerSound.getY()
				- OptionsLayerReset.getHeight() - 20);

		this.attachChild(OptionsLayerBrightness);
		this.attachChild(OptionsLayerSound);
		this.attachChild(OptionsLayerReset);

		this.setPosition(GameActivity.getWidth() / 2f,
				GameActivity.getHeight() / 2f + 480f);
	}

	/**
	 * Show an ask if you want reset the game if you select "no" continue the
	 * game, reset the game otherwhise
	 */
	public void showMessageReset() {
		ResourcesManager.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						ResourcesManager.getActivity())
						.setTitle("Universe Rescue")
						.setMessage(
								Html.fromHtml("Are you sure you want to reset the game?"))
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										GameActivity
												.writeIntToSharedPreferences(
														GameActivity.SHARED_PREFS_LEVEL_MAX_REACHED,
														0);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
									}
								});

				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
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

	@Override
	public void removeNext() {
	}
}
