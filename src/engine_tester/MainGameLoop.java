package engine_tester;

import models.RawModel;
import models.TexturedModel;
import render.engine.DisplayManager;
import render.engine.Loader;
import render.engine.MasterRenderer;
import render.obj.ModelData;
import render.obj.OBJFileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();

		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));

		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData lowTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		ModelData fernData = OBJFileLoader.loadOBJ("fern");

		RawModel treeRaw = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(),
				treeData.getIndices());
		RawModel grassRaw = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(),
				grassData.getNormals(), grassData.getIndices());
		RawModel fernRaw = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(),
				fernData.getIndices());
		RawModel lowTreeRaw = loader.loadToVAO(lowTreeData.getVertices(), lowTreeData.getTextureCoords(), lowTreeData.getNormals(),
				lowTreeData.getIndices());

		TexturedModel tree = new TexturedModel(treeRaw, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel lowTree = new TexturedModel(lowTreeRaw, new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel grass = new TexturedModel(grassRaw, new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel(fernRaw, new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(tree, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 3));
			entities.add(new Entity(lowTree, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 0.3f));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 0.6f));
		}

		while (!Display.isCloseRequested()) {
			camera.move();

			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
