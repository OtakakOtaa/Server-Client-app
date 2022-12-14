package Client.View.Scenes.TransactionList;

import Client.View.Config.ViewConfig;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnableLayer;
import Client.View.Scenes.Layer;
import Server.Entities.StaticEnumerable.Condition;
import Server.Entities.Transaction;
import imgui.ImGui;
import imgui.flag.ImGuiTabBarFlags;
import imgui.type.ImInt;

import java.util.List;

public class TransactionListLayer extends NotificationLayer implements Layer, ReturnableLayer
{
    private final TransactionListView transactionListView;
    private final Container container;

    public TransactionListLayer(TransactionListView transactionListView, List<Transaction> transactionList)
    {
        this.transactionListView = transactionListView;
        container = new Container();
        container.TransactionList = transactionList;
        container.AllTransactionList = transactionList;
    }

    @Override
    public void layer()
    {
        mainLayer();
        returnButton();
        selectButtonLayer();
        notificationLayer();
    }

    public void mainLayer()
    {
        ImGui.styleColorsLight();
        ImGui.setNextWindowPos(ViewConfig.MainWindow.Position.x(), ViewConfig.MainWindow.Position.y());
        ImGui.begin(" ",1);
        ImGui.beginTable("users in to your company", 6, ImGuiTabBarFlags.FittingPolicyMask_);
        ImGui.setWindowSize(500, 500);

        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Date");
        ImGui.tableSetupColumn("Description");
        ImGui.tableSetupColumn("condition");
        ImGui.tableSetupColumn("value");
        ImGui.tableSetupColumn("operation");
        ImGui.tableHeadersRow();

        int i = 0;
        for (var record : container.TransactionList)
        {
            ImGui.tableNextColumn();
            ImGui.text(i++ + "");
            ImGui.tableNextColumn();

            ImGui.text(record.getDate().toString());
            ImGui.tableNextColumn();

            ImGui.text(record.getDescription());
            ImGui.tableNextColumn();

            ImGui.text(record.getCondition().toString());
            ImGui.tableNextColumn();

            ImGui.text(record.getValue() + "");
            ImGui.tableNextColumn();

            ImGui.text(record.getOperation().toString());
            ImGui.tableNextColumn();

            ImGui.tableNextRow();
        }

        ImGui.endTable();
        ImGui.end();
    }

    public void selectButtonLayer()
    {
        ImGui.setNextWindowPos(ViewConfig.NotificationWindow.Position.x() + 700, ViewConfig.NotificationWindow.Position.y() - 200);
        ImGui.begin("View type", ViewConfig.NotificationWindow.WindowFlag);
        ImGui.setWindowSize(200, 100);

        ImGui.newLine();
        ImGui.setNextItemWidth(90);
        if(ImGui.combo(
                "View Type",
                container.SelectedOperationType,
                new String[] {
                        Container.OperationType.ALL,
                        Container.OperationType.BY_DATE,
                        Container.OperationType.BY_CONDITION,
                }
        ))
        {
            if (container.SelectedOperationType.get() == 0) container.Operation = Container.OperationType.ALL;
            if (container.SelectedOperationType.get() == 1) container.Operation = Container.OperationType.BY_DATE;
            if (container.SelectedOperationType.get() == 2) container.Operation = Container.OperationType.BY_CONDITION;
        }

        if (container.SelectedOperationType.get() == 0)
        {
            container.Operation = Container.OperationType.ALL;
        }
        if (container.SelectedOperationType.get() == 1)
        {
            container.Operation = Container.OperationType.BY_DATE;
            ImGui.newLine();
            ImGui.inputInt("Year", container.Year);
            ImGui.newLine();
            ImGui.inputInt("Month", container.Month);
            ImGui.newLine();
            ImGui.inputInt("Day", container.Day);
        }
        if (container.SelectedOperationType.get() == 2)
        {
            container.Operation = Container.OperationType.BY_CONDITION;
            ImGui.newLine();
            ImGui.setNextItemWidth(90);

            if(ImGui.combo(
                    "Condition",
                    container.SelectedCondition,
                    new String[]
                            {
                                    Condition.Deal.toString(),
                                    Condition.Taxes.toString(),
                                    Condition.Investment.toString(),
                                    Condition.Salary.toString(),
                            }))
            {
                if (container.SelectedCondition.get() == 0) container.Condition = Condition.Deal;
                if (container.SelectedCondition.get() == 1) container.Condition = Condition.Taxes;
                if (container.SelectedCondition.get() == 2) container.Condition = Condition.Investment;
                if (container.SelectedCondition.get() == 3) container.Condition = Condition.Salary;
            }
        }

        ImGui.newLine();
        if (ImGui.button("update", 200, 40))
            transactionListView.onUpdateButtonClick(container, this);
        ImGui.end();
    }

    @Override
    public void returnButton()
    {
        ImGui.setNextWindowPos(ViewConfig.ReturnButton.Position.x(), ViewConfig.ReturnButton.Position.y());
        ImGui.begin("Return",ViewConfig.ReturnButton.WindowFlag);
        if (ImGui.button("Return", ViewConfig.ReturnButton.Size.x(), ViewConfig.ReturnButton.Size.y()))
            transactionListView.onReturnButtonClick();
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


    public static class Container
    {
        public OperationType operationType;

        public String Operation = OperationType.ALL;

        public List<Transaction> TransactionList;
        public List<Transaction> AllTransactionList;

        public Server.Entities.StaticEnumerable.Condition Condition = Server.Entities.StaticEnumerable.Condition.Deal;


        public ImInt SelectedOperationType = new ImInt(0);
        public ImInt SelectedCondition = new ImInt(0);


        public ImInt Day = new ImInt(0);
        public ImInt Year = new ImInt(2022);
        public ImInt Month = new ImInt(12);


        public static class OperationType
        {
            public static final String ALL = "All";
            public static final String BY_DATE = "By Date";
            public static final String BY_CONDITION = "By Condition";
        }
    }

}
