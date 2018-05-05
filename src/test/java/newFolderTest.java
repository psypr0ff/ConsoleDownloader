/**
 * Created by Александр on 07.05.2017.
 */

import org.testng.annotations.AfterTest;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import java.io.File;

public class newFolderTest {

    public boolean checkFolder(String path){ // проверяем наличие указанной папки
        File file=new File(path);
        return file.exists();
    }
    @Test
    public void checkFolderCreation(){ //проверяем создается ли папка
        String path = "src/test/newFolder";
        DownloaderHelper d = new DownloaderHelper();
        d.newFolder(path);
        assertTrue(checkFolder(path));
    }
    @AfterTest(enabled = true)
    public void delFolder(){ //удаляем созданную тестовую папку
        String path = "src/test/newFolder";
        File file = new File(path);
        if (checkFolder(path)) {
            file.delete();
        }
    }



}
