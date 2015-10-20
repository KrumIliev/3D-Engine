package engine_tester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import render_engine.DisplayManager;
import render_engine.Loader;
import render_engine.MasterRenderer;
import render_engine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();

		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0, 0.5f, 0);
			camera.move();

			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);

			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
