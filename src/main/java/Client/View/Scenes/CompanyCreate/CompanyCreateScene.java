package Client.View.Scenes.CompanyCreate;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.StaticEnumerable.Gender;
import Server.Entities.StaticEnumerable.Position;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;

public class CompanyCreateScene extends NotificationLayer implements Layer, ReturnableLayer
{
    private static final int ITEM_WIDTH = 200;

    private final CompanyCreateView companyCreateView;
    private final Container container;

    public CompanyCreateScene(CompanyCreateView companyCreateView)
    {
        this.companyCreateView = companyCreateView;
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
        ViewConfig.setColorSchema();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Create Company", 10);
        ImGui.setWindowSize(350, 500);

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Login", container.Login, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Password", container.Password, ImGuiInputTextFlags.Password);

        ImGui.newLine();
        ImGui.newLine();

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("First name", container.FirstName, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Middle name", container.MiddleName);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Last name", container.LastName, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        if (ImGui.combo("Gender", container.selectedGender, new String[]{Gender.Man.toString(), Gender.Women.toString()}))
        {
            if (container.selectedGender.get() == 0) container.Gender = Gender.Man;
            if (container.selectedGender.get() == 1) container.Gender = Gender.Women;
        }

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputInt("Age", container.Age, ImGuiInputTextFlags.CharsNoBlank);

        ImGui.newLine();
        ImGui.newLine();

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputText("Company name", container.CompanyName);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputFloat("Start budget", container.StartBudget);


        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            companyCreateView.onEndCreateButtonClick(container, this);

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
            companyCreateView.onReturnButtonClick();
        ImGui.end();
    }


    public static class Container
    {
        public ImString Login = new ImString(20);
        public ImString Password = new ImString(20);

        public ImString FirstName = new ImString(20);
        public ImString MiddleName = new ImString(20);
        public ImString LastName = new ImString(20);
        public Server.Entities.StaticEnumerable.Gender Gender = Server.Entities.StaticEnumerable.Gender.Man;
        public ImInt Age = new ImInt();

        public ImString CompanyName = new ImString(20);
        public ImFloat StartBudget = new ImFloat(0);

        public ImInt selectedGender = new ImInt(0);
    }
}
