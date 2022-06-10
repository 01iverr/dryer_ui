package activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PreviewNotificationService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent()
                              .setAction("notificationPreview")
                              .putExtra("actionName", intent.getAction()));
    }
}
