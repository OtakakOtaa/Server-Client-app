package Client.View.Scenes.SetPosition;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Employee;
import Server.Entities.StaticEnumerable.Position;
import imgui.ImGui;
import imgui.flag.ImGuiTabBarFlags;
import imgui.type.ImInt;

import java.util.List;

public class SetPositionLayer extends NotificationLayer implements Layer, ReturnableLayer
{
    private final SetPositionView setPositionView;
    private final Container container;

    public SetPositionLayer(SetPositionView setPositionView, List<Employee> employeeList)
    {
        this.setPositionView = setPositionView;
        this.container = new Container();
        container.EmployeeList = employeeList;
    }

    @Override
    public void layer()
    {
        selectButtonLayer();
        mainLayer();
        notificationLayer();
        returnButton();
    }

    public void mainLayer()
    {
        ImGui.styleColorsLight();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin(" ",1);
        ImGui.beginTable("users in to your company", 5, ImGuiTabBarFlags.FittingPolicyMask_);
        ImGui.setWindowSize(500, 500);

        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Login");
        ImGui.tableSetupColumn("Name");
        ImGui.tableSetupColumn("Registration date");
        ImGui.tableSetupColumn("Position");
        ImGui.tableHeadersRow();

        int i = 0;
        for (var record : container.EmployeeList)
        {
            ImGui.tableNextColumn();
            ImGui.text(i++ + "");

            ImGui.tableNextColumn();
            ImGui.text(record.getUser().getLogin());

            ImGui.tableNextColumn();
            String name = record.getPerson().getFistName() + " " + record.getPerson().getLastName();
            ImGui.text(name);

            ImGui.tableNextColumn();
            ImGui.text(record.getUser().getRegistrationDate().toString());

            ImGui.tableNextColumn();
            ImGui.text(record.getPosition().toString());

            ImGui.tableNextRow();
        }

        ImGui.endTable();
        ImGui.end();
    }

    public void selectButtonLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.NotificationWindow.Position.x() + 700, ViewConfig.NotificationWindow.Position.y() - 200);
        ImGui.begin("Set position", ViewConfig.NotificationWindow.WindowFlag);
        ImGui.setWindowSize(200, 100);
        ImGui.inputInt("user id", container.SelectedUser);
        ImGui.newLine();

        ImGui.newLine();
        if (ImGui.combo("Access level", container.selectedPosition,
                new String[]{Position.Low.toString(), Position.Middle.toString(), Position.High.toString()}))
        {
            if (container.selectedPosition.get() == 0) container.Position = Position.Low;
            if (container.selectedPosition.get() == 1) container.Position = Position.Middle;
            if (container.selectedPosition.get() == 2) container.Position = Position.High;
        }

        ImGui.newLine();
        if (ImGui.button("Set", 200, 40))
            setPositionView.onSetButtonClick(container,this);
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
            setPositionView.onReturnButtonClick();
        ImGui.end();
    }



    public static class Container
    {
        public List<Employee> EmployeeList;
        public ImInt SelectedUser = new ImInt(0);

        public Position Position = Server.Entities.StaticEnumerable.Position.Low;
        public ImInt selectedPosition = new ImInt(0);
    }
}
