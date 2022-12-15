package Client.View.Scenes.Admin;

import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;

public interface AdminSceneView extends ReturnView
{
    void onRequestsToJoinCompanyButtonClick(NotificationLayer notificationLayer);
    void onAccountSettingsButtonClick(NotificationLayer notificationLayer);
    void onAccountInfoButtonClick();
    void onGetAllEmployeesInfoButtonClick(NotificationLayer notificationLayer);
    void onBanAccountButtonClick(NotificationLayer notificationLayer);
    void onSetPosition(NotificationLayer notificationLayer);

    void onAddTransactionButtonClick();
    void onBudgetAmountButtonClick();
    void onTransactionListAmountButtonClick();
    void onFileWriteButtonClick(NotificationLayer notificationLayer);
}
