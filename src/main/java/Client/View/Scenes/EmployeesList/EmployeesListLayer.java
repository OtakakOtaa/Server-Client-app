package Client.View.Scenes.EmployeesList;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Employee;
import imgui.ImGui;
import imgui.flag.ImGuiTabBarFlags;

import java.util.List;

public class EmployeesListLayer implements Layer, ReturnableLayer
{
    private final ReturnView returnView;
    private final List<Employee> employeeList;

    public EmployeesListLayer(ReturnView returnView, List<Employee> employeeList)
    {
        this.returnView = returnView;
        this.employeeList = employeeList;
    }

    @Override
    public void layer()
    {
        mainLayer();
        returnButton();
    }

    public void mainLayer()
    {
        ViewConfig.setColorSchema();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin(" ",1);
        ImGui.beginTable("all users in your company", 6, ImGuiTabBarFlags.FittingPolicyMask_);
        ImGui.setWindowSize(500, 500);

        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Login");
        ImGui.tableSetupColumn("Name");
        ImGui.tableSetupColumn("Gender");
        ImGui.tableSetupColumn("Registration date");
        ImGui.tableSetupColumn("Position");
        ImGui.tableHeadersRow();

        int i = 0;
        for (var record : employeeList)
        {
            ImGui.tableNextColumn();
            ImGui.text(i++ + "");

            ImGui.tableNextColumn();
            ImGui.text(record.getUser().getLogin());

            ImGui.tableNextColumn();
            String name = record.getPerson().getFistName() + " " + record.getPerson().getLastName();
            ImGui.text(name);

            ImGui.tableNextColumn();
            ImGui.text(record.getPerson().getGender().toString());

            ImGui.tableNextColumn();
            ImGui.text(record.getUser().getRegistrationDate().toString());

            ImGui.tableNextColumn();
            ImGui.text(record.getPosition().toString());

            ImGui.tableNextRow();
        }

        ImGui.endTable();
        ImGui.end();
    }

    @Override
    public void returnButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ReturnButton.Position.x(), ViewConfig.ReturnButton.Position.y());
        ImGui.begin("Return",ViewConfig.ReturnButton.WindowFlag);
        if (ImGui.button("Return", ViewConfig.ReturnButton.Size.x(), ViewConfig.ReturnButton.Size.y()))
            returnView.onReturnButtonClick();
        ImGui.end();
    }
}
