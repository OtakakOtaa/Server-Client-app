package Client.View.Scenes.TransactionList;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface TransactionListView extends ReturnView
{
    void onUpdateButtonClick(TransactionListLayer.Container container, NotificationLayer notificationLayer);
}
