package Client.View.Scenes.First;

import Client.View.Scenes.Behaviors.Exit.ExcitableLayer;
import Client.View.Scenes.Layer;
import Client.View.Config.ViewConfig;
import Server.Entities.StaticEnumerable.Position;
import imgui.ImColor;
import imgui.ImGui;
import imgui.type.ImInt;

public class FirstScene implements Layer, ExcitableLayer
{
    private final FirstSceneView firstSceneView;
    private float[] color = new float[3];

    public FirstScene(FirstSceneView firstSceneView) {
        this.firstSceneView = firstSceneView;
    }

    @Override public void layer()
    {
        colorSchemeLayer();
        exitButton();
        mainLayer();
    }

    public void mainLayer()
    {
        ViewConfig.setColorSchema();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Welcome",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 400);

        ImGui.sameLine(50);
        if (ImGui.button("Sign-In", 200, 100))
            firstSceneView.onSignInButtonClick();
        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("Sign-Up", 200, 100))
            firstSceneView.onSignUpButtonClick();
        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("CreateCompany", 200, 100))
            firstSceneView.onCreateCompanyButtonClick();

        ImGui.end();
    }

    public void colorSchemeLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.ExitButton.Position.x() + 200, ViewConfig.ExitButton.Position.y());
        ImGui.begin("Set Color",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 100);

        ImInt imInt = new ImInt(ViewConfig.COLOR_SCHEMA);
        if (ImGui.combo(
                "Color Scheme",
                imInt,
                new String[]{
                        "styleColorsLight",
                        "styleColorsDark",
                        "styleColorsClassic"
                }))
        {
            if (imInt.get() == 0) ViewConfig.COLOR_SCHEMA = 0;
            if (imInt.get() == 1) ViewConfig.COLOR_SCHEMA = 1;
            if (imInt.get() == 2) ViewConfig.COLOR_SCHEMA = 2;
        }

        ImGui.newLine();
        if(ImGui.colorEdit3("Color", color))
        {
            color[0] /= 225;
            color[1] /= 225;
            color[2] /= 225;

            firstSceneView.onSetColorButtonDown(color);
        }
        ImGui.end();
    }

    @Override public void exitButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ExitButton.Position.x(), ViewConfig.ExitButton.Position.y());
        ImGui.begin("Exit",10);
        ImGui.setWindowSize(100, 110);
        if (ImGui.button("Exit", ViewConfig.ExitButton.Size.x(), ViewConfig.ExitButton.Size.y()))
            firstSceneView.onButtonExit();
        ImGui.end();
    }
}
