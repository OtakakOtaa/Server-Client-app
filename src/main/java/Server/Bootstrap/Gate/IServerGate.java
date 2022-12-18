package Server.Bootstrap.Gate;

import Infrastructure.Disposable;

public interface IServerGate extends Disposable
{
    void listenGate() throws Exception;
}
