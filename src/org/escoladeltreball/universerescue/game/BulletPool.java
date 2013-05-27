package org.escoladeltreball.universerescue.game;

import java.util.LinkedList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.pool.GenericPool;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

public class BulletPool extends GenericPool<Sprite> {
	//Attributes
	private ITextureRegion bullet;
	private GameScene scene;
	private LinkedList<Sprite> list;

	//Constructor
	public BulletPool(final ITextureRegion pTextureRegion,
			final LinkedList<Sprite> bulletList, final GameScene pScene) {
		super();
		bullet = pTextureRegion;
		list = bulletList;
		scene = pScene;

	}

	/**
	 * Called if there is not a Bullet to shot
	 */
	@Override
	protected Sprite onAllocatePoolItem() {
		Sprite newSprite = new Sprite(10, 10, bullet.deepCopy(),
				ResourcesManager.getInstance().vbom);
		scene.attachChild(newSprite);
		list.add(newSprite);
		return newSprite;
	}
	
	/**
	 * Get a recycled Sprite from the pool
	 */
	@Override
	public synchronized Sprite obtainPoolItem() {
		Sprite bulletItem = super.obtainPoolItem();
		bulletItem.setIgnoreUpdate(false);
		bulletItem.setVisible(true);
		return bulletItem;
	}

	/**
	 * Called when a Bullet is sent to the pool
	 */
	@Override
	protected void onHandleRecycleItem(Sprite pItem) {
		super.onHandleRecycleItem(pItem);
		pItem.setVisible(false);
		pItem.setIgnoreUpdate(true);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setCullingEnabled(true);
	}
}