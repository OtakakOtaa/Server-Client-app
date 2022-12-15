package Client.View.Scenes.SignIn;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Client.View.Config.ViewConfig;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;

public class SignInScene extends NotificationLayer implements Layer, ReturnableLayer
{
    private static final int ITEM_WIDTH = 200;

    private final SignInSceneView signInSceneView;
    private final Container container;

    public SignInScene(SignInSceneView signInSceneView)
    {
        this.signInSceneView = signInSceneView;
        this.container = new Container();
    }

    @Override
    public void layer()
    {
        returnButton();
        notificationLayer();
        mainLayer();
    }

    public void mainLayer()
    {
        ViewConfig.setColorSchema();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Sign-In", 10);
        ImGui.setWindowSize(300, 200);

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Login", container.Login, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Password", container.Password, ImGuiInputTextFlags.Password);

        ImGui.newLine();
        if (ImGui.button("enter", 200, 70)) signInSceneView.onEndButtonClick(container, this);

        ImGui.end();
    }

    @Override
    public void notificationLayer()
    {
        if (NotificationFlag)
        {
            ImGui.setNextWindowPos(ViewConfig.NotificationWindow.Position.x(), ViewConfig.NotificationWindow.Position.y());
            ImGui.begin(Message, ViewConfig.NotificationWindow.WindowFlag);
            ImGui.setWindowSize(ViewConfig.NotificationWindow.Size.x(), ViewConfig.NotificationWindow.Size.y());
            if (ImGui.button("okay", 200, 70))
                NotificationFlag = false;
            ImGui.end();
        }
    }

    @Override
    public void returnButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ReturnButton.Position.x(), ViewConfig.ReturnButton.Position.y());
        ImGui.begin("Return",ViewConfig.ReturnButton.WindowFlag);
        if (ImGui.button("Return", ViewConfig.ReturnButton.Size.x(), ViewConfig.ReturnButton.Size.y()))
            signInSceneView.onReturnButtonClick();
        ImGui.end();
    }

    public static class Container
    {
        public ImString Login = new ImString(20);
        public ImString Password = new ImString(20);
    }
}
