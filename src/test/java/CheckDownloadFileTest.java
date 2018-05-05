import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * Created by Александр on 08.05.2017.
 */
public class CheckDownloadFileTest {
    DownloaderHelper d=new DownloaderHelper();
    @Test
    public void DownloadFileTest() throws IOException{
        assertTrue(d.checkDownloadFile("http://www.kstehnika.ru/base/data/4271big.jpg"));
    }
}
