package Client.View.Scenes.AccountSettings;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface AccountSettingsView extends ReturnView
{
    void onEditNameButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer);
    void onEditPasswordButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer);
    void onEditLoginButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer);
    void onEditAgeNButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer);
}
