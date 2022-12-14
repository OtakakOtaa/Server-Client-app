package Client.View.Scenes.SetPosition;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface SetPositionView extends ReturnView
{
    void onSetButtonClick(SetPositionLayer.Container container, NotificationLayer notificationLayer);
}
