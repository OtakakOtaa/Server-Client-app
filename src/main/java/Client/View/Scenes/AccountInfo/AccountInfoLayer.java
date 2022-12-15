package Client.View.Scenes.AccountInfo;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Employee;
import imgui.ImGui;

public class AccountInfoLayer implements Layer, ReturnableLayer
{
    private static final int ITEM_WIDTH = 200;

    private final ReturnView returnView;
    private final Employee employee;


    public AccountInfoLayer(ReturnView returnView, Employee employee)
    {
        this.returnView = returnView;
        this.employee = employee;
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
        ImGui.begin("Info", 1 );
        ImGui.setWindowSize(500, 500);

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Login - " + employee.getUser().getLogin());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Password - " + employee.getUser().getPassword());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Registration Date - " + employee.getUser().getRegistrationDate().toString());

        ImGui.newLine();
        ImGui.newLine();

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("First name - " + employee.getPerson().getFistName());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Middle name - " + employee.getPerson().getMiddleName());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Last name - " + employee.getPerson().getLastName());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Age - " + employee.getPerson().getAge());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Gender - " + employee.getPerson().getGender());


        ImGui.newLine();
        ImGui.newLine();

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Company name - " + employee.getCompany().getName());

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("Position - " + employee.getPosition());

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
