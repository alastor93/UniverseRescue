package org.escoladeltreball.universerescue.levels;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

public class LevelSelectorButtons extends Sprite {

	// Variables
	private final ResourcesManager manager = ResourcesManager.getInstance();
	private int maxLevelIndex;
	private Text mButtonText;
	private Sprite mLockedSprite;
//	private final TiledSprite mStarsEnt;
	private boolean levelIsLocked = true;
	private boolean mIsTouched = false;
	private boolean mIsClicked = false;

	// CONSTRUCTOR
	public LevelSelectorButtons(final int pLevelIndex, final float pX,
			final float pY, final float dimension, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY,dimension,dimension, pTextureRegion, pVertexBufferObjectManager);
		// Set level's index
		this.maxLevelIndex = pLevelIndex;
		// Set button's text, position and color
		this.mButtonText = new Text(0f, 0f, manager.levelsFont,
				String.valueOf(this.maxLevelIndex), manager.vbom);
		this.mButtonText.setPosition((this.getWidth() / 3f),
				(this.getHeight() / 2f));
		this.mButtonText.setColor(0.7f, 0.7f, 0.7f);
		// Create Sprite for locked levels
//		this.mLockedSprite = new Sprite(this.getWidth() / 2f,
//				this.getHeight() / 2f, manager.menuLevelLocked, manager.vbom);
		// Set level's stars
//		this.mStarsEnt = new TiledSprite((this.getWidth() / 3f) * 2f,
//				(this.getHeight() / 2f), manager.menuLevelStar, manager.vbom);
		// Check if level should be locked
//		this.levelIsLocked = (this.maxLevelIndex > (GameActivity
//				.getIntFromSharedPreferences(GameActivity.SHARED_PREFS_LEVEL_MAX_REACHED) + 1));
		this.attachChild(this.mButtonText);
		this.setScale(1.5f);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.mIsTouched = true;
		} else if ((pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
				&& this.mIsTouched) {
			this.mIsTouched = false;

			// Touch event response
			if (this.levelIsLocked) {
				// NOTHING TO DO BECAUSE LEVEL IS LOCKED
			} else {
				// action is the level is unlocked
				this.mIsClicked = true;
			}
		}
		return true;
	}

//	@Override
//	protected void onManagedUpdate(final float pSecondsElapsed) {
//		super.onManagedUpdate(pSecondsElapsed);
//		if(!this.levelIsLocked) {
//			if(!this.mButtonText.hasParent()) {
//				this.attachChild(this.mButtonText);
////				this.attachChild(this.mStarsEnt);
////				this.mStarsEnt.setCurrentTileIndex(MagneTankActivity.getLevelStars(this.mLevelIndex));
//			}
//			if(this.mLockedSprite.hasParent()) {
//				this.mLockedSprite.detachSelf();
//			}
//		} else {
//			if(!this.mLockedSprite.hasParent()) {
//				this.attachChild(this.mLockedSprite);
//			}
//			if(this.mButtonText.hasParent()) {
//				this.mButtonText.detachSelf();
////				this.mStarsEnt.detachSelf();
//			}
//		}
//	}
	
//	public void refreshStars() {
//		this.levelIsLocked = (this.maxLevelIndex > (GameActivity
//				.getIntFromSharedPreferences(GameActivity.SHARED_PREFS_LEVEL_MAX_REACHED) + 1));
//		this.mStarsEnt.setCurrentTileIndex(GameActivity
//				.getLevelStars(this.maxLevelIndex));
//	}
}
