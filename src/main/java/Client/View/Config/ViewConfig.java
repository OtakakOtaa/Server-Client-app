package Client.View.Config;

import imgui.ImGui;

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
            new WindowSettings.ReactVector(1400, 300),
            new WindowSettings.ReactVector(400, 200),
            10);

    public static int COLOR_SCHEMA = 0;

    public static void setColorSchema()
    {
        if(COLOR_SCHEMA == 0)
            ImGui.styleColorsLight();
        if(COLOR_SCHEMA == 1)
            ImGui.styleColorsDark();
        if(COLOR_SCHEMA == 2)
            ImGui.styleColorsClassic();
    }

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

        public record ReactVector(int x, int y) { }
    }
}
