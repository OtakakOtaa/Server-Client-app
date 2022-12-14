package Client.View.Scenes.AddTransaction;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface AddTransactionView extends ReturnView
{
    void onAddTransactionButton(AddTransactionLayer.Container container, NotificationLayer notificationLayer);
}
