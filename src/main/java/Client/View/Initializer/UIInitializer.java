package Client.View.Initializer;

import Client.View.Scenes.Layer;
import Client.View.Window.Window;

public class UIInitializer
{
    public static void initialize(Window window, Layer firstLayer)
    {
        new Thread(() ->
        {
            try
            {
                window.initialize(firstLayer);
                window.run();
                window.destroy();
            }
            catch (Exception ignored) {}
        }).start();
    }
}
