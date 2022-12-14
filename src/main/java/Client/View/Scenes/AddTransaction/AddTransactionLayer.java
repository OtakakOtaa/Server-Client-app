package Client.View.Scenes.AddTransaction;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.StaticEnumerable.Condition;
import Server.Entities.StaticEnumerable.Gender;
import Server.Entities.StaticEnumerable.Position;
import Server.Services.Transaction.TransactionCreator;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;

public class AddTransactionLayer extends NotificationLayer implements Layer, ReturnableLayer
{
    private static final int ITEM_WIDTH = 200;

    private final AddTransactionView addTransactionView;
    private final float budget;
    private final Container container;

    public AddTransactionLayer(AddTransactionView addTransactionView, float budget)
    {
        this.addTransactionView = addTransactionView;
        this.budget = budget;
        container = new Container();
    }

    @Override
    public void layer()
    {
        mainLayer();
        returnButton();
        notificationLayer();
    }

    public void mainLayer()
    {
        ImGui.styleColorsLight();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin("Add Transaction", 1 );
        ImGui.setWindowSize(500, 500);

        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.text("current budget amount - " + budget);

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        if (ImGui.combo("Condition", container.SelectedCondition,
                new String[]
                        {
                                Condition.Deal.toString(),
                                Condition.Taxes.toString(),
                                Condition.Investment.toString(),
                                Condition.Salary.toString(),
                        }))
        {
            if (container.SelectedOperationType.get() == 0)
                container.Condition = Condition.Deal;
            if (container.SelectedOperationType.get() == 1)
                container.Condition = Condition.Taxes;
            if (container.SelectedOperationType.get() == 2)
                container.Condition = Condition.Investment;
            if (container.SelectedOperationType.get() == 3)
                container.Condition = Condition.Salary;
        }


        ImGui.newLine();
        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        if (ImGui.combo("Operation", container.SelectedOperationType,
                new String[]
                        {
                                TransactionCreator.OperationType.Add.toString(),
                                TransactionCreator.OperationType.Assign.toString(),
                                TransactionCreator.OperationType.Scale.toString(),
                                TransactionCreator.OperationType.Subtract.toString()
                        }))
        {
            if (container.SelectedOperationType.get() == 0)
                container.OperationType = TransactionCreator.OperationType.Add;
            if (container.SelectedOperationType.get() == 1)
                container.OperationType = TransactionCreator.OperationType.Assign;
            if (container.SelectedOperationType.get() == 2)
                container.OperationType = TransactionCreator.OperationType.Scale;
            if (container.SelectedOperationType.get() == 3)
                container.OperationType = TransactionCreator.OperationType.Subtract;
        }

        ImGui.newLine();
        ImGui.setNextItemWidth(ITEM_WIDTH);
        ImGui.inputFloat("Value", container.Value, ImGuiInputTextFlags.CharsNoBlank);


        ImGui.newLine();
        ImGui.newLine();
        ImGui.inputTextMultiline("Description", container.Description);


        ImGui.newLine();
        if (ImGui.button("enter", 200, 70))
            addTransactionView.onAddTransactionButton(container, this);

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
            addTransactionView.onReturnButtonClick();
        ImGui.end();
    }

    public static class Container
    {
        public ImFloat Value = new ImFloat(1);
        public ImString Description = new ImString(100);

        public TransactionCreator.OperationType OperationType = TransactionCreator.OperationType.Assign;
        public Condition Condition = Server.Entities.StaticEnumerable.Condition.Deal;


        public ImInt SelectedOperationType = new ImInt(0);
        public ImInt SelectedCondition = new ImInt(0);
    }
}
