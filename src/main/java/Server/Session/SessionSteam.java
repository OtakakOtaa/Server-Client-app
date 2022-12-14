package Server.Session;

import Infrastructure.Disposable;
import Server.TransferMessageContainer;

import java.io.*;
import java.net.Socket;

public class SessionSteam implements Disposable
{
    private final Socket forwardSocket;
    public final ObjectInputStream in;
    public final ObjectOutputStream out;

    public SessionSteam(Socket forwardSocket) throws Exception
    {
        this.forwardSocket = forwardSocket;

        out = new ObjectOutputStream(forwardSocket.getOutputStream());
        in = new ObjectInputStream(forwardSocket.getInputStream());
    }

    public void send(Serializable object) throws IOException
    {
        out.writeObject(object);
    }
    public Serializable accept() throws IOException, ClassNotFoundException
    {
        return (Serializable) in.readObject();
    }
    public void startAction(String actionKey) throws IOException
    {
        out.writeObject(new TransferMessageContainer(actionKey, ""));
    }

    public void startAction(TransferMessageContainer transferMessageContainer) throws IOException
    {
        out.writeObject(transferMessageContainer);
    }

    public String waitAction() throws IOException, ClassNotFoundException
    {
        return ((TransferMessageContainer) in.readObject()).Key();
    }

    public TransferMessageContainer waitMessage() throws IOException, ClassNotFoundException
    {
        return ((TransferMessageContainer) in.readObject());
    }

    public void dispose() throws Exception
    {
        in.close();
        out.close();
        forwardSocket.close();
    }
}
