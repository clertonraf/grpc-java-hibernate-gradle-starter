import io.clertonraf.grpc.WalletClient;
import io.clertonraf.grpc.WalletServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Map;

@RunWith(JUnit4.class)
public class AccountServerTest {

    private WalletServer server;
    private WalletClient client;

    @Before
    public void setUp() throws IOException {
        server = new WalletServer();
        server.start(50053);
        client = new WalletClient("localhost", 50053);
    }

    @Test
    public void testWallet() {

        /*client.deposit("1",100.0,"USD");

        */
        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));

        client.deposit("1",100.0,"USD");

        Map<String, Double> balances =  client.getBalance("1");
        Assert.assertEquals(100.0, balances.get("USD"), 0.00001);
        Assert.assertEquals(0.0, balances.get("EUR"), 0.00001);
        Assert.assertEquals(0.0, balances.get("GBR"), 0.00001);

        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));

        client.deposit("1",100.0,"EUR");

        balances =  client.getBalance("1");
        Assert.assertEquals(100.0, balances.get("USD"), 0.00001);
        Assert.assertEquals(100.0, balances.get("EUR"), 0.00001);
        Assert.assertEquals(0.0, balances.get("GBR"), 0.00001);

        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));
        client.deposit("1",100.0,"USD");

        balances =  client.getBalance("1");
        Assert.assertEquals(200.0, balances.get("USD"), 0.00001);
        Assert.assertEquals(100.0, balances.get("EUR"), 0.00001);
        Assert.assertEquals(0.0, balances.get("GBR"), 0.00001);

        Assert.assertEquals("ok",client.withdraw("1",200.0,"USD"));

        balances =  client.getBalance("1");
        Assert.assertEquals(0.0, balances.get("USD"), 0.00001);
        Assert.assertEquals(100.0, balances.get("EUR"), 0.00001);
        Assert.assertEquals(0.0, balances.get("GBR"), 0.00001);

        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));
    }

    @After
    public void tearDown() throws InterruptedException{
        client.shutdown();
        server.stop();
    }
}
