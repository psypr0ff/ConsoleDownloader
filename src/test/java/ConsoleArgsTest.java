/**
 * Created by Александр on 07.05.2017.
 */
import org.junit.Before;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ConsoleArgsTest {
    String[] positive = {"-n","4","-l","30","-o","C:\\files","-f", "C:\\files\\file.txt"};
    String[] negative = {"100000","-l","-n","2","-n", "*km0","-o","C:\\files","-f", "C:\\files\\file.txt"};
    DownloaderHelper.ConsoleArgs ConsArgs=new DownloaderHelper.ConsoleArgs(positive);
    DownloaderHelper.ConsoleArgs ConsArgsNeg = new DownloaderHelper.ConsoleArgs(negative);

    @Test
    public void downloadSpeedTestPositive(){
        assertEquals(ConsArgs.downloadSpeed,30);
        System.out.println("Download speed positive = "+ ConsArgs.downloadSpeed);
    }
    @Test(enabled = true)
    public void threadsCountTest(){
        assertEquals(ConsArgs.threadsCount,4);
        System.out.println("Threads count positive = "+ ConsArgs.threadsCount);
    }
    @Test
    public void filePathTest(){
        assertEquals(ConsArgs.filePath,"C:\\files\\file.txt");
        System.out.println("Path to file with links positive= "+ConsArgs.filePath);
    }
    @Test
    public void folderNameTest(){
       assertEquals(ConsArgs.folderName,"C:\\files");
        System.out.println("Path to downloads folder positive = "+ConsArgs.folderName);
    }
    @Test
    public void threadsCountTestNegative(){
        System.out.println("Threads count negative= "+ ConsArgsNeg.threadsCount);
        assertEquals(ConsArgsNeg.threadsCount,1);
    }
    @Test
    public void downloadSpeedTestNegative(){
        System.out.println("DownloadSpeed negative="+ConsArgsNeg.downloadSpeed);
        assertEquals(ConsArgsNeg.downloadSpeed, 262144);
    }
}
