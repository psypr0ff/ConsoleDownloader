import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
//import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Александр on 08.05.2017.
 */
public class Downloader implements Runnable {
    private String url;
    private String file;
    private int downloadSpeed;
    private long startTimeStamp;
    private long endTimeStamp;
    private long contentSize;
    public Downloader(String url, String file, int downloadSpeed){
        this.url = url;
        this.file = file;
        this.downloadSpeed=downloadSpeed;
    }
    long getDownloadTime(){
        return endTimeStamp-startTimeStamp;
    }
    long getDownloadSize(){
        return contentSize;
    }
    public void run() {
        System.out.println("Download from "+this.url +" to "+this.file+" started ");
        try {
            URL url=new URL(this.url);
            contentSize=0;
            startTimeStamp=System.nanoTime();
            ReadableByteChannel rbc= Channels.newChannel(url.openStream());//создаем канал для входящего потока
            FileOutputStream fos = new FileOutputStream(file); //создаем исходящий поток для зайписи байтов в файл
            File f=new File(file);//
            long counter=0;//счетчик скачанных байтов
            while (counter<=f.length()){
                fos.getChannel().transferFrom(rbc,counter,downloadSpeed/1000); //скачиваем периодами в 1 милисекунду
                counter=counter+downloadSpeed/1000;
                Thread.sleep(1);
            }
            fos.close(); //закрываем поток
            rbc.close(); //закрываем канал
            contentSize=f.length(); //записываем размер скачанного файла
            endTimeStamp=System.nanoTime();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e){
            System.out.println("файл указанный в ссылке не найден");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.printf("Finished download from "+url+" . %d kilobytes in %.4f seconds.\n", getDownloadSize()/1024, getDownloadTime() / 1000000000d);
    }
}
