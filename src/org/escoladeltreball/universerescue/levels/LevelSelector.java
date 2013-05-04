package org.escoladeltreball.universerescue.levels;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

public class LevelSelector extends Scene {

	// CONSTANTS
	private final ResourcesManager MANAGER = ResourcesManager.getInstance();
	private final VertexBufferObjectManager VBOM = ResourcesManager
			.getInstance().vbom;
	private final Camera CAMERA = ResourcesManager.getInstance().camera;
	// Layout properties
	/** variable for a number of rows */
	private final int mROWS = 1;
	/** variable for a number of columns */
	private final int mCOLUMNS = 3;
	/** Variable of max levels */
	private final int maxLevelIndex = 3;

	// Tile properties
	/** Variable for dimension */
	public final float mTILE_DIMENSION = new Float(64.0);
	/** Variable for padding */
	public final float mTILE_PADDING = new Float(15.0);

	// VARIABLES

	// Initial tile positions
	/** Variable for initial position X */
	private float mInitialX;
	/** Variable for initial position Y */
	private float mInitialY;

	/** ArrayList for load the level's buttons */
	public final ArrayList<LevelSelectorButtons> mLevelButtons = new ArrayList<LevelSelectorButtons>();

	// CONSTRUCTOR

	public LevelSelector(final float pX, final float pY,
			final ITextureRegion textureRegion, final Font font) {
		// Set background for our scene
		createBackground();

		// Set the initial position in order to properly center the tiles

		// FOR X
		final float halfLevelSelectorWidth = ((mTILE_DIMENSION * mCOLUMNS) + mTILE_PADDING
				* (mCOLUMNS));
		this.mInitialX = (CAMERA.getWidth() * 0.5f)
				- halfLevelSelectorWidth;

		// FOR Y
		this.mInitialY = (CAMERA.getHeight() * 0.5f);

		// Build the levels icons
		createTiles(textureRegion, font);
	}

	private void createTiles(final ITextureRegion textureRegion, final Font font) {

		// Temporary coordinates
		float tempX = this.mInitialX + mTILE_DIMENSION + mTILE_PADDING;
		float tempY = this.mInitialY; 

		// Temporary level integer
		int currentTileLevel = 1;

		// Loop through, adjust COLUMN positions and
		// create sprite/text objects
		for (int j = 0; j < mCOLUMNS; j++) {

			// Create the level tile/button
			final LevelSelectorButtons levelButton = new LevelSelectorButtons(
					currentTileLevel, tempX, tempY, mTILE_DIMENSION,
					textureRegion, VBOM);

			// Register and attach the level tile/button to the scene
			this.registerTouchArea(levelButton);
			this.attachChild(levelButton);
			mLevelButtons.add(levelButton);

			// Increment the temporary X position of the level tile
			tempX = tempX + (mTILE_DIMENSION * 2.f) + mTILE_PADDING;

			// Increment the current tile count/level
			currentTileLevel++;
			if (currentTileLevel > maxLevelIndex) {
				return;
			}
		}

		// Reset the temporary x position back to the first column position
		tempX = mInitialX;
		// Reposition the height to the next row's position
		tempY = tempY - mTILE_DIMENSION - mTILE_PADDING;
	}

	/**
	 * Method that refresh level's stars
	 */

	public void refreshAllButtonStars() {
		for (LevelSelectorButtons levels : mLevelButtons)
			levels.refreshStars();
	}

	private void createBackground() {
		attachChild(new Sprite(CAMERA.getWidth() / 2f, CAMERA.getHeight() / 2f,
				MANAGER.menu_background_region, VBOM) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
}
