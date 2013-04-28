package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SceneManager;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	// Attributes //

	/** Sprite for build menu and resources */
	private Sprite menuScene;
	/** A MainMenuScene, class from andEngine library */
	private MenuScene menuChildScene;
	/** An int associated with Menu's play option */
	private final int MENU_PLAY = 0;
	/** An int associated with Menu's option 'option' */
	private final int MENU_OPTIONS = 1;
	/** An int associated with Menu's option 'exit' */
	private final int MENU_EXIT = 2;

	// Methods

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();

	}

	@Override
	public void onBackKeyPressed() {
		// WHAT GAME SHOULD DO WHEN BACK KEY PRESS
		System.exit(0);

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MAINMENU;
	}

	@Override
	public void disposeScene() {
		menuScene.detachSelf();
		menuScene.dispose();
		this.detachSelf();
		this.dispose();

	}

	/**
	 * Build a background screen for our menu scene
	 */

	private void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				manager.menu_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	/**
	 * Create the menu options. play game, options, exit.
	 * 
	 */

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(this.camera); // Use the constructor of
														// andEngine for set a
														// camera
		menuChildScene.setPosition(0, -50);
		// Build a menu item's
		// PLAY
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, manager.play_region, vbom), 1.2f,
				1);
		// OPTIONS
		final IMenuItem optionMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_OPTIONS, manager.options_region, vbom),
				1.2f, 1);
		// EXIT
		final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_EXIT, manager.exit_region, vbom), 1.2f,
				1);
		// Add menu items to the MenuScene
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(optionMenuItem);
		menuChildScene.addMenuItem(exitMenuItem);

		// Gives animation to item when get clicked and block the background
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		// Put the menu items on specific position
		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 30);
		optionMenuItem.setPosition(optionMenuItem.getX(),
				optionMenuItem.getY() + 50);
		exitMenuItem.setPosition(exitMenuItem.getX(), exitMenuItem.getY() + 70);

		// Sets the MainMenuScene as a listener on menu's item click
		menuChildScene.setOnMenuItemClickListener(this);

		// Finally, put the MenuScene
		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			// Load Game Scene!
			return true;
		case MENU_OPTIONS:
			SceneManager.getInstance().showOptionsLayer(false);
			return true;
		case MENU_EXIT:
			ResourcesManager.getActivity().runOnUiThread(new Runnable() {
				
				
				@Override
				public void run() {
					final AlertDialog.Builder builder = new AlertDialog.Builder(ResourcesManager.getActivity()).setTitle("Universe Rescue").setMessage(Html.fromHtml("Estas seguro que desea salir?")).setPositiveButton("Si", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(final DialogInterface dialog, final int id) {
										System.exit(0);

									}
								}).setNegativeButton("No", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(final DialogInterface dialog, final int id) {
									}
								});
					
					final AlertDialog alert = builder.create();
					alert.show();
				}
			});
		default:
			return false;
		}
	}
}