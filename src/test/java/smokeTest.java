import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * Created by Александр on 15.05.2017.
 */
public class smokeTest {
    DownloaderMain downloaderMain;
    String filePath="src/test/links1.txt";
    String downloadFolder="src/test/smoke";
    String[] positive = {"-n","4","-l","256k","-o",downloadFolder,"-f", filePath};
    String downloadedFile="src/test/smoke/downloaded_1.jpg";


    @BeforeTest
    //создадим файл со ссылками на скачивание
    public void createNewFileWithLinks(){
        File file=new File(filePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file, true);
            try{
                out.write("http://www.kstehnika.ru/base/data/544m.jpg downloaded_1.jpg");
            } finally {
                out.close();
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Smoke(){
        try {
            downloaderMain.main(positive);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(downloaderMain.totalTime>0);
        File chekedFile=new File(downloadedFile);
        assertTrue(chekedFile.exists());
    }

    @AfterTest
    public void cleanAll(){
        File links=new File(filePath);
        if(links.exists()) links.delete();
        File folder=new File(downloadFolder);
        if(folder.exists()) {
            File file = new File(downloadedFile);
            if (file.exists()) file.delete();
            folder.delete();
        }
    }
}
