package realtech;

import org.junit.jupiter.api.Test;

public class UnitTest {
    
    @Test
    public void test() throws Exception {
        String remoteAddr = "::ffff:127.0.0.1";
        if (remoteAddr != null && remoteAddr.startsWith("::ffff:")) {
            System.out.println(remoteAddr.substring(7));
        }
    }
    
    
}
