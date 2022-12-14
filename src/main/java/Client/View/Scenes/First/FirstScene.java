package Client.View.Scenes.First;

import Client.View.Scenes.Behaviors.Exit.ExcitableLayer;
import Client.View.Scenes.Layer;
import Client.View.Config.ViewConfig;
import imgui.ImGui;

public class FirstScene implements Layer, ExcitableLayer
{
    private final FirstSceneView firstSceneView;

    public FirstScene(FirstSceneView firstSceneView) {
        this.firstSceneView = firstSceneView;
    }

    @Override public void layer()
    {
        ImGui.styleColorsLight();

        exitButton();
        mainLayer();
    }

    public void mainLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Welcome",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

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

    @Override public void exitButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ExitButton.Position.x(), ViewConfig.ExitButton.Position.y());
        ImGui.begin("Exit",ViewConfig.ExitButton.WindowFlag);
        if (ImGui.button("Exit", ViewConfig.ExitButton.Size.x(), ViewConfig.ExitButton.Size.y()))
            firstSceneView.onButtonExit();
        ImGui.end();
    }
}
