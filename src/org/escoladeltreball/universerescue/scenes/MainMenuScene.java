package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.modifier.IModifier;
import org.escoladeltreball.universerescue.helpers.GrowToggleButton;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SFXManager;
import org.escoladeltreball.universerescue.managers.SceneManager;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener,IEntityModifierListener {

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
		
		// MUSIC
				final GrowToggleButton MusicToggleButton = new GrowToggleButton(ResourcesManager.music_region.getWidth() / 2f, ResourcesManager.music_region.getHeight() / 2f, ResourcesManager.music_region, !SFXManager.isMusicMuted()) {
					@Override
					public boolean checkState() {
						return !SFXManager.isMusicMuted();
					}
					
					@Override
					public void onClick() {
						SFXManager.toggleMusicMuted();
					}
				};
				
		// Add menu items to the MenuScene
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(optionMenuItem);
		menuChildScene.addMenuItem(exitMenuItem);
		menuChildScene.attachChild(MusicToggleButton);
		menuChildScene.registerTouchArea(MusicToggleButton);

		// Gives animation to item when get clicked and block the background
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		// Put the menu items on specific position 
		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 30);
		optionMenuItem.setPosition(optionMenuItem.getX(),
				optionMenuItem.getY() + 50);
		exitMenuItem.setPosition(exitMenuItem.getX(), exitMenuItem.getY() + 70);
		MusicToggleButton.setPosition(MusicToggleButton.getX(), MusicToggleButton.getY()+50);

		playMenuItem.registerEntityModifier(new MoveModifier(1, camera.getWidth(), playMenuItem.getY(), camera.getWidth()/2, playMenuItem.getY()));
		optionMenuItem.registerEntityModifier(new MoveModifier(1, 0, optionMenuItem.getY(), camera.getWidth()/2, optionMenuItem.getY()));
		exitMenuItem.registerEntityModifier(new MoveModifier(1, camera.getWidth(), exitMenuItem.getY(), camera.getWidth()/2, exitMenuItem.getY(),this));

		// Finally, put the MenuScene
		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			SceneManager.getInstance().createLevelScene();
			return true;
		case MENU_OPTIONS:
			SceneManager.getInstance().showOptionsLayer(false);
			return true;
		case MENU_EXIT:
			ResourcesManager.getActivity().showMessageExit();
		default:
			return false;
		}
	}

	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		// Sets the MainMenuScene as a listener on menu's item click
		menuChildScene.setOnMenuItemClickListener(this);		
	}
}