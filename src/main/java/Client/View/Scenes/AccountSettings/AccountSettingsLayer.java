package Client.View.Scenes.AccountSettings;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImInt;
import imgui.type.ImString;

public class AccountSettingsLayer extends NotificationLayer implements Layer, ReturnableLayer
{
    private final AccountSettingsView accountSettingsView;
    private final Container container;


    public AccountSettingsLayer(AccountSettingsView accountSettingsView)
    {
        this.accountSettingsView = accountSettingsView;
        container = new Container();
    }

    @Override
    public void layer()
    {
        mainLayer();
        notificationLayer();
        returnButton();
    }

    public void mainLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Account Settings",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.sameLine(50);
        if (ImGui.button("Change password", 200, 100))
            container.passFlag = true;
        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("Change login", 200, 100))
            container.loginFlag = true;
        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("Change age", 200, 100))
            container.ageFlag = true;
        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("Change name", 200, 100))
            container.nameFlag = true;


        if(container.loginFlag) loginEditLayer();
        if(container.passFlag) passEditLayer();
        if(container.ageFlag) ageEditLayer();
        if(container.nameFlag) nameEditLayer();

        ImGui.end();
    }

    public void loginEditLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Login Edit",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.setNextItemWidth(100);
        ImGui.inputText("New Login", container.Login, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            accountSettingsView.onEditLoginButtonClick(container, this);

        ImGui.end();
    }

    public void passEditLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Password Edit",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.setNextItemWidth(100);
        ImGui.inputText("New password", container.Password, ImGuiInputTextFlags.Password);

        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            accountSettingsView.onEditPasswordButtonClick(container, this);

        ImGui.end();
    }

    public void ageEditLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Age Edit",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.setNextItemWidth(100);
        ImGui.inputInt("New age", container.Age);

        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            accountSettingsView.onEditAgeNButtonClick(container, this);

        ImGui.end();
    }

    public void nameEditLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Name Edit",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.setNextItemWidth(100);
        ImGui.inputText("New first name", container.FirstName, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.setNextItemWidth(100);
        ImGui.inputText("New middle name", container.MiddleName, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.setNextItemWidth(100);
        ImGui.inputText("New last name", container.LastName, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            accountSettingsView.onEditNameButtonClick(container, this);

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
            accountSettingsView.onReturnButtonClick();
        ImGui.end();
    }

    public static class Container
    {
        public boolean loginFlag = false;
        public boolean passFlag = false;
        public boolean nameFlag = false;
        public boolean ageFlag = false;

        public ImString Login = new ImString(20);
        public ImString Password = new ImString(20);

        public ImString FirstName = new ImString(20);
        public ImString MiddleName = new ImString(20);
        public ImString LastName = new ImString(20);
        public ImInt Age = new ImInt();
    }
}

