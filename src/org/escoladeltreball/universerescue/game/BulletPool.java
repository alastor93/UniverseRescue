package org.escoladeltreball.universerescue.game;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.pool.GenericPool;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

public class BulletPool extends GenericPool<Sprite> {

	private ITextureRegion bullet = null;
	private GameScene scene;

	public BulletPool(final ITextureRegion pTextureRegion, final GameScene pScene) {
		super();
		bullet = pTextureRegion;
		scene = pScene;
		
	}

	/**
	 * Called if there is not a Bullet to shot
	 */
	@Override
	protected Sprite onAllocatePoolItem() {
		Sprite newSprite = new Sprite(10, 10, ResourcesManager.getInstance().bulletSprite, ResourcesManager.getInstance().vbom); 
		scene.attachChild(newSprite);
		scene.bulletList.add(newSprite);
		return newSprite;
	}

	/**
	 * Called when a Bullet is sent to the pool
	 */
	@Override
	protected void onHandleRecycleItem(final Sprite pBullet) {
		pBullet.setVisible(true);
		pBullet.setIgnoreUpdate(true);
	}

	/**
	 * Called just before a Bullet is returned to the caller, this is where you
	 */
	@Override
	protected void onHandleObtainItem(final Sprite pBullet) {
		pBullet.reset();
	}
}