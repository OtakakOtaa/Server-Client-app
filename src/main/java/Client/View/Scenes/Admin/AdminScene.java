package Client.View.Scenes.Admin;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.StaticEnumerable.Position;
import imgui.ImGui;

public class AdminScene extends NotificationLayer implements Layer, ReturnableLayer
{
    private final AdminSceneView adminSceneView;
    private final boolean isMiddle;
    private final boolean isAdmin;

    public AdminScene(AdminSceneView adminSceneView, Position position)
    {
        this.adminSceneView = adminSceneView;
        isAdmin = position.equals(Position.High);
        isMiddle =  (position.equals(Position.High) || position.equals(Position.Middle));
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
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y() - 100);
        ImGui.begin("Welcome admin",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(600, 600);

        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("Account Settings", 200, 100))
            adminSceneView.onAccountSettingsButtonClick(this);


        ImGui.sameLine(300);
        if (ImGui.button("Account info", 200, 100))
            adminSceneView.onAccountInfoButtonClick();

        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("view budget", 200, 100))
            adminSceneView.onBudgetAmountButtonClick();

        ImGui.sameLine(300);
        if (ImGui.button("view all transaction", 200, 100))
            adminSceneView.onTransactionListAmountButtonClick();

        if(isAdmin)
        {
            ImGui.newLine();
            ImGui.sameLine(50);
            if (ImGui.button("Requests to join company", 200, 100))
                adminSceneView.onRequestsToJoinCompanyButtonClick(this);


            ImGui.sameLine(300);
            if (ImGui.button("All Employees info", 200, 100))
                adminSceneView.onGetAllEmployeesInfoButtonClick(this);

            ImGui.newLine();
            ImGui.sameLine(50);
            if (ImGui.button("Ban account", 200, 100) )
                adminSceneView.onBanAccountButtonClick(this);

            ImGui.sameLine(300);
            if (ImGui.button("Set position", 200, 100))
                adminSceneView.onSetPosition(this);

        }
        if(isMiddle)
        {
            ImGui.newLine();
            ImGui.sameLine(50);
            if (ImGui.button("add transaction", 200, 100))
                adminSceneView.onAddTransactionButtonClick();
        }

        ImGui.newLine();
        ImGui.sameLine(50);
        if (ImGui.button("write budget data in file", 200, 100))
            adminSceneView.onFileWriteButtonClick(this);

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
            adminSceneView.onReturnButtonClick();
        ImGui.end();
    }
}
