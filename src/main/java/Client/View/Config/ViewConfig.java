package Client.View.Config;

import imgui.flag.ImGuiWindowFlags;

public class ViewConfig
{
    public static final WindowSettings MainWindow = new WindowSettings(
            new WindowSettings.ReactVector(800, 300),
            new WindowSettings.ReactVector(500, 500),
            10)
            ;
    public static final WindowSettings ExitButton = new WindowSettings(
            new WindowSettings.ReactVector(40, 40),
            new WindowSettings.ReactVector(80, 80),
            10);

    public static final WindowSettings ReturnButton = new WindowSettings(
            new WindowSettings.ReactVector(40, 40),
            new WindowSettings.ReactVector(80, 80),
            10);

    public static final WindowSettings NotificationWindow = new WindowSettings(
            new WindowSettings.ReactVector(800, 300),
            new WindowSettings.ReactVector(400, 600),
            10);



    public static class WindowSettings
    {
        public final ReactVector Position;
        public final ReactVector Size;
        public final int WindowFlag;

        public WindowSettings(ReactVector position, ReactVector size, int windowFlag)
        {
            Position = position;
            Size = size;
            WindowFlag = windowFlag;
        }

        public record ReactVector(int x, int y)
        {
        }
    }
}
