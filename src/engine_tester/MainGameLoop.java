package engine_tester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import render_engine.DisplayManager;
import render_engine.Loader;
import render_engine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = { 
				-0.5f, 0.5f, 0f,	// V0
				-0.5f, -0.5f, 0f,	// V1
				0.5f, -0.5f, 0f,	// V2
				0.5f, 0.5f, 0f };	// V3
		
		int[] indices = {
				0, 1, 3,			// Top left triangle (V0, V1, V3)
				3, 1, 2				// Bottom right triangle (V3, V1, V2)
		};
		
		float[] textureCoords = {
				0, 0,				// V0
				0, 1,				// V1
				1, 1,				// V2
				1, 0				// V3
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		while (!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
