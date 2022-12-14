package Client.View.Scenes.ApproveAccountsList;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Employee;
import imgui.ImGui;
import imgui.flag.ImGuiTabBarFlags;
import imgui.type.ImInt;

import java.util.List;

public class ApproveAccountsListLayer extends NotificationLayer implements ReturnableLayer, Layer
{
    private final ApproveAccountsListView approveAccountsListView;
    private final Container container;

    public ApproveAccountsListLayer(ApproveAccountsListView approveAccountsListView, List<Employee> employeeList)
    {
        this.approveAccountsListView = approveAccountsListView;
        container = new Container();
        container.EmployeeList =employeeList;
    }

    @Override
    public void layer()
    {
        mainLayer();
        notificationLayer();
        returnButton();
        selectButtonLayer();
    }

    public void mainLayer()
    {
        ImGui.styleColorsLight();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin(" ",1);
        ImGui.beginTable("Not approved users in to your company", 4, ImGuiTabBarFlags.FittingPolicyMask_);
        ImGui.setWindowSize(500, 500);

        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Login");
        ImGui.tableSetupColumn("Name");
        ImGui.tableSetupColumn("Registration date");
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
            ImGui.tableNextRow();
        }

        ImGui.endTable();
        ImGui.end();
    }

    public void selectButtonLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.NotificationWindow.Position.x() + 700, ViewConfig.NotificationWindow.Position.y() - 200);
        ImGui.begin("Approve User", ViewConfig.NotificationWindow.WindowFlag);
        ImGui.setWindowSize(200, 100);
        ImGui.inputInt("user id", container.SelectedUser);
        ImGui.newLine();
        if (ImGui.button("Approve", 200, 40))
            approveAccountsListView.onApproveButtonClick(container,this);
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
            approveAccountsListView.onReturnButtonClick();
        ImGui.end();
    }


    public static class Container
    {
        public List<Employee> EmployeeList;

        public ImInt SelectedUser = new ImInt(0);
    }
}
