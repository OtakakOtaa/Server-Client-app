package Client.View.Scenes.CompanyCreate;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface CompanyCreateView extends ReturnView
{
    public void onEndCreateButtonClick(CompanyCreateScene.Container container, NotificationLayer notificationLayer);
    public void onReturnButtonClick();
}
