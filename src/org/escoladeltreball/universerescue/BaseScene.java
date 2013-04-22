package org.escoladeltreball.universerescue;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.SceneManager.SceneType;

public abstract class BaseScene extends Scene {

	// Attributes
	protected Engine engine;
	protected Camera camera;
	protected GameActivity activity;
	protected VertexBufferObjectManager vbom;
	protected ResourcesManager manager;

	// Constructor
	public BaseScene() {
		this.manager = ResourcesManager.getInstance();
		this.engine = manager.engine;
		this.camera = manager.camera;
		this.activity = manager.activity;
		this.vbom = manager.vbom;
		createScene();
	}

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
}
