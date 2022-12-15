package Client.View.Scenes.BudgetAmount;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.Company;
import Server.Entities.Transaction;
import imgui.ImGui;
import imgui.ImVec4;

import java.util.List;

public class BudgetAmountLayer implements Layer, ReturnableLayer
{
    private final ReturnView returnView;

    private final float[] commits;
    private final Company company;

    public BudgetAmountLayer(ReturnView returnView, Company company, float[] commits)
    {
        this.returnView = returnView;
        this.company = company;
        this.commits = commits;
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
        ImGui.begin("Company budget amount",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(700, 700);

        ImGui.text("Company: " + company.getName() + " - " + company.getBudget());

        ImGui.newLine();

        ImGui.plotLines("Budget Commit Dot", commits, commits.length + 1);
        ImGui.newLine();
        ImGui.beginChild("Commits");
        for (int n = 0; n < commits.length; n++)
            ImGui.text(n+ " commit amount : " + commits[n]);
        ImGui.endChild();
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
