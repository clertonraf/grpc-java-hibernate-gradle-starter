import io.clertonraf.grpc.WalletClient;
import io.clertonraf.grpc.WalletServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

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

        client.deposit("1",100.0,"USD");

        /*Assert.assertEquals(0.0,client.getBalance("1"), 0.00001);
        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));
        client.deposit("1",100.0,"USD");
        Assert.assertEquals(116.75, client.getBalance("1"), 0.00001);
        Assert.assertEquals("insufficient_funds",client.withdraw("1",200.0,"USD"));
        client.deposit("1",100.0,"EUR");*/

    }

    @After
    public void tearDown() throws InterruptedException{
        client.shutdown();
        server.stop();
    }
}
