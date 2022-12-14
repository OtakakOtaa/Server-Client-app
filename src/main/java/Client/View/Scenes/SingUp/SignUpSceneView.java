package Client.View.Scenes.SingUp;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface SignUpSceneView extends ReturnView
{
    void onEndRegistrationButtonClick(SignUpScene.Container container, NotificationLayer notificationLayer);
}
