package Server.Bootstrap.Token;

public class CancellationSessionToken implements ReadOnlyCancellationSessionToken
{
    private boolean isCancel = false;

    public void cancel() {
        isCancel = true;
    }

    @Override
    public boolean isCancel() {
        return isCancel;
    }
}
