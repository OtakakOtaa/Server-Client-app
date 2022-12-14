package Client.View.Scenes.BudgetAmount;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Company;
import imgui.ImGui;

public class BudgetAmountLayer implements Layer, ReturnableLayer
{
    private final ReturnView returnView;
    private final Company company;

    public BudgetAmountLayer(ReturnView returnView, Company company)
    {
        this.returnView = returnView;
        this.company = company;
    }

    @Override
    public void layer()
    {
        mainLayer();
        returnButton();
    }

    public void mainLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Company budget amount",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(300, 300);

        ImGui.text("Company: " + company.getName() + " - " + company.getBudget());
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
