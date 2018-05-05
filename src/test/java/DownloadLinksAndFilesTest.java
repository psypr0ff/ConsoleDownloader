import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Александр on 07.05.2017.
 */
public class DownloadLinksAndFilesTest {
    private static DownloaderHelper.getDownloadFileNames DFN;

    @BeforeTest
    //создадим файл со ссылками на скачивание
    public void createNewFileWithLinks(){
        File file=new File("src/test/links.txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file, true);
            try{
                out.write("http://localhost/denwer/1.jpg downloaded_1.jpg\r\n"+
                        "http://localhost/denwer/2.jpg downloaded_2.jpg\r\n"+
                        "http://localhost/denwer/3.jpg downloaded_3.jpg\r\n");
            } finally {
                out.close();
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Test
    public void checkLinksTest(){
        DFN = new DownloaderHelper.getDownloadFileNames("src/test/links.txt");
        System.out.println(DFN.downloadURL.get(0));
        assertEquals(DFN.downloadURL.get(0),"http://localhost/denwer/1.jpg");
    }
    @Test
    public void checkFileNamesTest(){
        DFN = new DownloaderHelper.getDownloadFileNames("src/test/links.txt");
        System.out.println(DFN.downloadFileName.get(1));
        assertEquals(DFN.downloadFileName.get(1),"downloaded_2.jpg");
    }

    @AfterTest(enabled = true)
    public void delFile(){
        File file=new File("src/test/links.txt");
        if (file.exists()){
            file.delete();
        }
    }

}
