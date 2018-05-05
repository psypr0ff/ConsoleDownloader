import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertTrue;

/**
 * Created by Александр on 08.05.2017.
 */
public class downloaderTest {
    String outputFile="src/test/downloaded_3.jpg";
    @Test
    public void downloadFile(){
        String url="http://www.kstehnika.ru/base/data/4271big.jpg";
        Downloader d=new Downloader(url,outputFile,1048576);
        d.run();
        File file=new File(outputFile);
        assertTrue(file.exists());
    }
    @AfterTest(enabled = true)
    public void delFile(){
        File file=new File(outputFile);
        if(file.exists()) file.delete();
    }
}
