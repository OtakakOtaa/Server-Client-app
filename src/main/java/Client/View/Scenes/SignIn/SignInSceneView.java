package Client.View.Scenes.SignIn;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface SignInSceneView extends ReturnView
{
    void onEndButtonClick(SignInScene.Container container, NotificationLayer notificationLayer);
}
