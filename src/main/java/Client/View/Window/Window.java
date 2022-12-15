package Client.View.Window;

import Client.View.Scenes.Layer;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.awt.*;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window
{
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private Layer layer;
    private String glslVersion = null;
    protected long windowPrt;

    private Color background = new Color(1, 1,0,0);


    public void initialize(Layer firstLayer) throws Exception
    {
        layer = firstLayer;
        initWindow();
        initImGui();
        imGuiGl3.init(glslVersion);
        imGuiGlfw.init(windowPrt, true);
    }

    public void run() throws Exception
    {
        while (!glfwWindowShouldClose(windowPrt)) {
            glClearColor(background.getRed(), background.getGreen(),background.getBlue(), background.getAlpha());
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            layer.layer();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

            GLFW.glfwSwapBuffers(windowPrt);
            GLFW.glfwPollEvents();
        }
    }

    public void switchLayer(Layer layer)
    {
        this.layer = layer;
    }

    public void switchColor(float[] color)
    {
        background = new Color(color[0], color[1], color[2], 0);
    }

    public void destroy()
    {
        imGuiGlfw.dispose();
        imGuiGl3.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPrt);
        glfwDestroyWindow(windowPrt);
        glfwTerminate();
    }

    private void initWindow() throws Exception
    {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glslVersion = "#version 130"; // openGL #3.0
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        windowPrt = glfwCreateWindow(1920, 1080, "Application", NULL, NULL);

        if (windowPrt == NULL) throw new Exception("Window init failed");

        glfwMakeContextCurrent(windowPrt);
        glfwSwapInterval(1);
        glfwShowWindow(windowPrt);

        GL.createCapabilities();
    }

    private void initImGui() {
        ImGui.createContext();
    }
}
