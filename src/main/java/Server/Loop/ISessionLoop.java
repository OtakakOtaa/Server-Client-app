package Server.Loop;

import Infrastructure.Disposable;
import Server.Bootstrap.Token.CancellationSessionToken;
import Server.Bootstrap.Token.ReadOnlyCancellationSessionToken;

public interface ISessionLoop extends Disposable
{
    void startSession(CancellationSessionToken token);

}
