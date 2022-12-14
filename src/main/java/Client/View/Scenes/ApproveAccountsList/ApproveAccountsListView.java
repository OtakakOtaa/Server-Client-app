package Client.View.Scenes.ApproveAccountsList;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface ApproveAccountsListView extends ReturnView
{
    void onApproveButtonClick(ApproveAccountsListLayer.Container container, NotificationLayer notificationLayer);
}
