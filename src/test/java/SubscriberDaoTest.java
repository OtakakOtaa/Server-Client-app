import dao.SubscriberDaoImpl;
import entity.Subscriber;
import org.junit.After;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

public final class SubscriberDaoTest {

    @Test
    public final void pushToDBTest() {

        //Attempt
        SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();

        Subscriber subscriber = new Subscriber(
                "Timofei",
                "Sergei",
                "Gonchar",
                new Date(300),
                "MTS",
                "MeGA"
        );

        List<Subscriber> receivedSubscriber = null;
        Boolean existCondition = false;

        //Action
        receivedSubscriber = subscriberDao.getAll();
        //Assert

    }


}
