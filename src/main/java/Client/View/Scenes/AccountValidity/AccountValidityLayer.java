package Client.View.Scenes.AccountValidity;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import imgui.ImGui;

public class AccountValidityLayer implements Layer, ReturnableLayer
{
    private final AccountValidityView accountValidityView;

    public AccountValidityLayer(AccountValidityView accountValidityView)
    {
        this.accountValidityView = accountValidityView;
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
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x() - 60, ViewConfig.MainWindow.Position.y());
        ImGui.begin(" ",  ViewConfig.MainWindow.WindowFlag);
        ImGui.setWindowSize(600, 200);

        ImGui.sameLine(50);
        ImGui.text("wait for you accepted into the company by the admin");
        accountValidityView.observeState();
        ImGui.end();
    }

    @Override
    public void returnButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ReturnButton.Position.x(), ViewConfig.ReturnButton.Position.y());
        ImGui.begin("Return",ViewConfig.ReturnButton.WindowFlag);
        if (ImGui.button("Return", ViewConfig.ReturnButton.Size.x(), ViewConfig.ReturnButton.Size.y()))
            accountValidityView.onReturnButtonClick();
        ImGui.end();
    }
}
