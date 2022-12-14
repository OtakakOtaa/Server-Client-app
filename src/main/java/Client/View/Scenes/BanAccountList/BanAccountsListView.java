package Client.View.Scenes.BanAccountList;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface BanAccountsListView extends ReturnView
{
    void onBanButtonClick(BanAccountsListLayer.Container container, NotificationLayer notificationLayer);
}
