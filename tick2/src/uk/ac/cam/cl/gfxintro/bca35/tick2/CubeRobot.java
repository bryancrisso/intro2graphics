package uk.ac.cam.cl.gfxintro.bca35.tick2;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

;

public class CubeRobot {
	
    // Filenames for vertex and fragment shader source code
    private final static String VSHADER_FN = "resources/cube_vertex_shader.glsl";
    private final static String FSHADER_FN = "resources/cube_fragment_shader.glsl";

	private HashMap<String, Component> components = new HashMap<>();
	private ArrayList<String> sceneGraph = new ArrayList<>();
    
    // Reference to the skybox of the scene
    public SkyBox skybox;

	
/**
 *  Constructor
 *  Initialize all the CubeRobot components
 */
	public CubeRobot() {
//		// Create body node
//
//		// Initialise Geometry
//		body_mesh = new CubeMesh();
//
//		// Initialise Shader
//		body_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
//		// Tell vertex shader where it can find vertex positions. 3 is the dimensionality of vertex position
//		// The prefix "oc_" means object coordinates
//		body_shader.bindDataToShader("oc_position", body_mesh.vertex_handle, 3);
//		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
//		body_shader.bindDataToShader("oc_normal", body_mesh.normal_handle, 3);
//		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
//		body_shader.bindDataToShader("texcoord", body_mesh.tex_handle, 2);
//
//		// Initialise texturing
//		body_texture = new Texture();
//		body_texture.load("resources/cubemap.png");
		
		// Build Transformation Matrix
		Matrix4f body_t = new Matrix4f();
		
		// TODO: Scale the body transformation matrix

		Matrix4f body_e = new Matrix4f().scale(0.75f, 1.5f, 0.75f);

		addComponent("", "body", body_t, body_e, "resources/cubemap.png");

		// TODO: Create right arm node



		// TODO: Initialise Texturing



		// TODO: Build Right Arm's Transformation Matrix (rotate the right arm around its end)

		Matrix4f rarm_t = new Matrix4f();
		Matrix4f rarm_e = new Matrix4f();
		//always scale -> rotate -> translate
		//if we need to rotate around a hinge
		//translate by -(hinge) -> rotate around origin -> translate by (hinge)
		rarm_t = rarm_t
				.translate(1, 0.75f, 0.125f)
				.rotateAffineXYZ(0,0,0.3f);

		rarm_e = rarm_e
				.translate(0,-1,0)
				.scale(0.25f, 1.25f, 0.25f);

		addComponent("body", "rarm", rarm_t, rarm_e, "resources/cubemap.png");

		// TODO: Complete robot

		Matrix4f larm_t = new Matrix4f();
		Matrix4f larm_e = new Matrix4f();

		larm_t = larm_t
				.translate(-1, 0.75f, 0.125f)
				.rotateAffineXYZ(0,0,-0.3f);
		larm_e = larm_e
				.translate(0,-1,0)
				.scale(0.25f, 1.25f, 0.25f);

		addComponent("body", "larm", larm_t, larm_e, "resources/cubemap.png");

		Matrix4f rleg_t = new Matrix4f();
		Matrix4f rleg_e = new Matrix4f();

		rleg_t = rleg_t
				.translate(0.4f, -2.5f, 0);
		rleg_e = rleg_e
				.scale(0.25f, 1.25f, 0.25f);

		addComponent("body", "rleg", rleg_t, rleg_e, "resources/cubemap.png");

		Matrix4f lleg_t = new Matrix4f();
		Matrix4f lleg_e = new Matrix4f();
		lleg_t = lleg_t
				.translate(-0.4f, -2.5f, 0);
		lleg_e = lleg_e
				.scale(0.25f, 1.25f, 0.25f);

		addComponent("body", "lleg", lleg_t, lleg_e, "resources/cubemap.png");

		Matrix4f head_t = new Matrix4f();
		Matrix4f head_e = new Matrix4f();
		head_t = head_t.translate(0,1.9f,0);
		head_e = head_e.scale(0.4f,0.4f,0.4f);

		addComponent("body", "head", head_t, head_e, "resources/cubemap_head.png");
	}
	

	/**
	 * Updates the scene and then renders the CubeRobot
	 * @param camera - Camera to be used for rendering
	 * @param deltaTime		- Time taken to render this frame in seconds (= 0 when the application is paused)
	 * @param elapsedTime	- Time elapsed since the beginning of this program in millisecs
	 */
	public void render(Camera camera, float deltaTime, long elapsedTime) {
		
		// TODO: Animate Body. Translate the body as a function of time
		//body.t = body.t.rotateAffineXYZ(0, deltaTime*bod_speed, 0);
		
		// TODO: Animate Arm. Rotate the left arm around its end as a function of time

		renderGraph(camera, "body", new Matrix4f(), deltaTime, elapsedTime);
		//renderGraph(camera, "body", new Matrix4f(), deltaTime, elapsedTime);

		// TODO: Chain transformation matrices of the arm and body (Scene Graph)

		//rarm_transform = body_transform.mul(rarm_transform, rarm_transform);

		// TODO: Render Arm.
		
		//TODO: Render rest of the robot
	}

	private void renderGraph(Camera camera, String c, Matrix4f parentTransform, float deltaTime, long elapsedTime)
	{
		Component comp = components.get(c);
		Matrix4f m = new Matrix4f(parentTransform);
		m.mul(comp.t);

		Matrix4f pt = new Matrix4f(parentTransform);
		pt.mul(comp.t);
		if(c.equals("body"))
		{
			m = m.rotateAffineXYZ(0, elapsedTime/1000f,0);
			pt = pt.rotateAffineXYZ(0, elapsedTime/1000f,0);
		}
		if(c.equals("rarm"))
		{
			m = m.rotateAffineXYZ(0, 0, 0.75f+0.75f*(float)Math.sin(elapsedTime/800f));
		}
		if(c.equals("larm"))
		{
			m = m.rotateAffineXYZ(0, 0, -0.75f-0.75f*(float)Math.sin(elapsedTime/800f));
		}
		m = m.mul(comp.e);
		renderMesh(camera, comp.mesh, m, comp.shader, comp.texture);
		ArrayList<String> ar = c.equals("body") ? sceneGraph : new ArrayList<>();
		for (String s : ar)
		{
			renderGraph(camera, s, pt, deltaTime, elapsedTime);
		}

	}
	
	/**
	 * Draw mesh from a camera perspective
	 * @param camera		- Camera to be used for rendering
	 * @param mesh			- mesh to render
	 * @param modelMatrix	- model transformation matrix of this mesh
	 * @param shader		- shader to colour this mesh
	 * @param texture		- texture image to be used by the shader
	 */
	public void renderMesh(Camera camera, Mesh mesh , Matrix4f modelMatrix, ShaderProgram shader, Texture texture) {
		// If shaders modified on the disc, reload them
		shader.reloadIfNeeded(); 
		shader.useProgram();

		// Step 2: Pass relevant data to the vertex shader
		
		// compute and upload MVP
		Matrix4f mvp_matrix = new Matrix4f(camera.getProjectionMatrix()).mul(camera.getViewMatrix()).mul(modelMatrix);
		shader.uploadMatrix4f(mvp_matrix, "mvp_matrix");
		
		// Upload Model Matrix and Camera Location to the shader for Phong Illumination
		shader.uploadMatrix4f(modelMatrix, "m_matrix");
		shader.uploadVector3f(camera.getCameraPosition(), "wc_camera_position");
		
		// Transformation by a nonorthogonal matrix does not preserve angles
		// Thus we need a separate transformation matrix for normals
		Matrix3f normal_matrix = new Matrix3f(modelMatrix);
		//TODO: Calculate normal transformation matrix
		Matrix3f transformed_normal_matrix = normal_matrix.invert().transpose();
		shader.uploadMatrix3f(transformed_normal_matrix, "normal_matrix");
		
		// Step 3: Draw our VertexArray as triangles
		// Bind Textures
		texture.bindTexture();
		shader.bindTextureToShader("tex", 0);
		skybox.bindCubemap();
		shader.bindTextureToShader("skybox", 1);
		// draw
		glBindVertexArray(mesh.vertexArrayObj); // Bind the existing VertexArray object
		glDrawElements(GL_TRIANGLES, mesh.no_of_triangles, GL_UNSIGNED_INT, 0); // Draw it as triangles
		glBindVertexArray(0);             // Remove the binding
		
        // Unbind texture
		texture.unBindTexture();
		skybox.unBindCubemap();
	}

	private void addComponent(String parent, String name, Matrix4f t, Matrix4f e, String tex)
	{
		Component comp = new Component();
		comp.mesh = new CubeMesh();

		// Initialise Shader
		comp.shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		// Tell vertex shader where it can find vertex positions. 3 is the dimensionality of vertex position
		// The prefix "oc_" means object coordinates
		comp.shader.bindDataToShader("oc_position", comp.mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		comp.shader.bindDataToShader("oc_normal", comp.mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		comp.shader.bindDataToShader("texcoord", comp.mesh.tex_handle, 2);

		comp.t = t;
		comp.e = e;

		comp.texture = new Texture();
		comp.texture.load(tex);

		components.put(name, comp);
		if(!parent.isEmpty())
		{
			sceneGraph.add(name);
		}
	}

	static class Component
	{
		Mesh mesh;
		ShaderProgram shader;
		Texture texture;
		Matrix4f t;
		Matrix4f e;
	}
}
