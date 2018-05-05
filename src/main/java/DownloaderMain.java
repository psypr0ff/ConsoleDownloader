import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Александр on 07.05.2017.
 */
public class DownloaderMain {

    private static DownloaderHelper.ConsoleArgs ConsArgs;
    private static DownloaderHelper.getDownloadFileNames DownloadFileName;
    static DownloaderHelper d = new DownloaderHelper();
    static long totalTime;

    public static void main(String[] args) throws InterruptedException, IOException {
        long startTime;
        long endTime;
        int failedDownloads=0;
        int successDownloads=0;
        //получаем аргументы из консоли
        ConsArgs = new DownloaderHelper.ConsoleArgs(args);
        System.out.println("Start downloading in "+ConsArgs.threadsCount+" threads with "+ConsArgs.downloadSpeed+" b/s download speed ");
        System.out.println("Path to with links is "+ConsArgs.filePath);
        System.out.println("Path to downloads folder is "+ConsArgs.folderName);
        System.out.println("------------------------------------------------------------------------");

        //получаем значения из файла со ссылками
        DownloadFileName = new DownloaderHelper.getDownloadFileNames(ConsArgs.filePath);

        //создаем папку куда будут сохраняться файлы
        d.newFolder(ConsArgs.folderName);

        List<Downloader> downloaders = new ArrayList<Downloader>(); //список загрузчиков
        //заполняем по циклу списки загрузчиков и потоков
        ExecutorService pool= Executors.newFixedThreadPool(ConsArgs.threadsCount);

        startTime=System.nanoTime();
        for (int x = 0; x < DownloadFileName.downloadURL.size(); x++) {
            //проверяем - существует ли файл который мы собираемся скачать
            if (d.checkDownloadFile(DownloadFileName.downloadURL.get(x))) {
                Downloader downloader = new Downloader(DownloadFileName.downloadURL.get(x), ConsArgs.folderName + "\\" + DownloadFileName.downloadFileName.get(x), ConsArgs.downloadSpeed);
                pool.submit(downloader);
                downloaders.add(downloader);
                successDownloads=successDownloads+1;
            } else {
                System.out.println("File " + DownloadFileName.downloadURL.get(x) + " not found"); //если файл не найден, сообщаем об этом
                failedDownloads=failedDownloads+1;
            }
        }
        pool.shutdown();
        //проверяем завершение всех потоков
        if (pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
            System.out.println("All downloads are finished. ");
            System.out.println("Success downloads: "+successDownloads);
            System.out.println("Failed downloads: "+failedDownloads);
            endTime=System.nanoTime();
            totalTime=endTime-startTime;
            d.getTotalDownloadStatistic(downloaders, totalTime); // получаем и выводим в консоль итоговую статистику
        }

    }
}
