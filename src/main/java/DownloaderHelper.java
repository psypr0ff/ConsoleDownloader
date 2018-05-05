import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 07.05.2017.
 */
public class DownloaderHelper {

//получение значения аргументов из значений введенных с консоли
    public static class ConsoleArgs{
        public int downloadSpeed; //скорость скачивания
        public int threadsCount; //количество потоков
        public String filePath; //путь к файлу со ссылками
        public String folderName; //путь к папке кудс скачивать файлы

        ConsoleArgs(String[] args){
            for(int x=0; x<args.length; x++){
                if((args[x].equals("-n"))&&!(args[x+1].equals("-l"))&&!(args[x+1].equals("-f"))&&!(args[x+1].equals("-o"))){
                    threadsCount=Integer.parseInt("0".concat(args[x+1].replaceAll("[^0-9]", ""))); //узнаем количество потоков скачивания
                }
                if(args[x].equals("-l")){
                    if (args[x+1].endsWith("k")) {// проверяем, есть ли суффикс k в конце
                        downloadSpeed = Integer.parseInt("0".concat(args[x + 1].substring(0,args[x+1].length()-1).replaceAll("[^0-9]", "")))*1024;
                    }
                    if (args[x+1].endsWith("m")){
                        downloadSpeed = Integer.parseInt("0".concat(args[x + 1].substring(0,args[x+1].length()-1).replaceAll("[^0-9]", "")))*1024*1024;
                    }
                    if (!(args[x+1].endsWith("k"))&&!(args[x+1].endsWith("m"))){
                        downloadSpeed=Integer.parseInt("0".concat(args[x+1].replaceAll("[^0-9]", "")));
                    }
                }
                if(args[x].equals("-f")){
                    filePath=args[x+1]; // узнаем где лежит файл с ссылками
                }
                if(args[x].equals("-o")){
                    folderName=args[x+1]; //узнаем куда класть скаченные файлы
                }
            }
            if (downloadSpeed==0) downloadSpeed=262144;
            if (threadsCount==0) threadsCount=1;
        }

    }
    //создание папки, куда будут помещаться скаченные файлы
    public void newFolder(String folder) {
        File createFolder = new File(folder);
        createFolder.mkdirs();
    }
    //получаем из файла ссылки для скачивания и имена сохраняемых файлов
    public static class getDownloadFileNames{
        public static List<String> downloadFileName = new ArrayList<String>();
        public static List<String> downloadURL = new ArrayList<String>();

        getDownloadFileNames(String filepath) {
            File fileWithLinks = new File(filepath);
            if (!fileWithLinks.exists()) { //если файл с сылками не найден по указанному пути
                System.out.println("File with links not found!!! Check path to file."); //пишем, что файл не найден
            } else { //иначе
                List<String> downloadLinks = new ArrayList<String>(); //создаем список
                try {
                    downloadLinks = Files.readAllLines(Paths.get(filepath)); //заполняем список строками из файла с сылками
                }catch (IOException e){};
                String[][] temp = new String[downloadLinks.size()][1]; //служебный двумерный массив для хранения разделенных данных из списка
                for (int x=0; x<downloadLinks.size(); x++) { //пробегаемся по строкам списка downloadLinks
                    if (!downloadLinks.get(x).isEmpty()) { //если строка не пустая
                        temp[x]=downloadLinks.get(x).split(" "); //делим строку по пробелу и записываем значения в двумерный массив
                        if (temp[x][0].startsWith("http://")) { //если в строке первый эелемент начинается с http:// то
                            downloadURL.add(temp[x][0]); //добавляем значение в список со ссылками
                            if(temp[x].length==1) {  // если есть только ссылка и не указано название сохраняемого файла
                                //записываем в список с названиями файло исходное название файла на сервере
                                downloadFileName.add(temp[x][0].substring(temp[x][0].lastIndexOf("/")+1,temp[x][0].length()));
                            } else
                            downloadFileName.add(temp[x][1]); // добавляем значение в список с названиями файлов
                        } else System.out.println(temp[x][0]+" is invalid link!!!"); //если первый элемент не содержит http:// то сообщаем, что это не ссылка
                    }
                }
            }
        }
    }

    //проверяем существование файла по указанной ссылке
    public boolean checkDownloadFile(String urlLink) throws IOException{
        URL url=new URL(urlLink);
        Boolean result=false;
        try {
            ReadableByteChannel rbc= Channels.newChannel(url.openStream()); //создаем новый байтканал
            if (rbc.isOpen()) result=true; //если канал открыт - записываем значение true
            rbc.close(); // закрываем канал
        } catch (FileNotFoundException e){
            result = false; //если ловим исключение "файл не найден", то записываем значение false
        } catch (ConnectException e){
            result = false;
            System.out.println("Http server not found. Check download links.");
        }
        return result;
    }
    //получаем и выводим статистику (общее время скачивания и общее количество байт)
    public void getTotalDownloadStatistic(List<Downloader> downloaders, long totalDownloadTime){
        long totalDownloadSize=0;
        double dimension=1000000000d; // должно быть 1000000000d  т.к. считаем время в наносекундах и в секунды нужно переводить
        int minutes;
        int seconds;
        int hours;
        for (Downloader downloader : downloaders) { //для каждого элемента списка Downloaders
            totalDownloadSize += downloader.getDownloadSize(); //суммируем общий размер всех файлов
        }
        if (((totalDownloadTime/dimension)>=1)&&((totalDownloadTime/dimension)<60)){ //если общее время меньше 1мин, то показываем время в секундах
            seconds= (int) (totalDownloadTime/dimension); //считаем секунды
            System.out.println("Total time is:"+seconds+" sec"); //выводим время в секундах
        } if(((totalDownloadTime/dimension)>=60)&&((totalDownloadTime/dimension)<3600)){ //есди общее время больше 1 минуты и меньше 1 часа
            minutes= (int) (Math.floor((totalDownloadTime/dimension)/60)); //считаем минуты
            seconds= (int) (Math.round(((totalDownloadTime/dimension)/60-minutes)*100)); //считаем секунды
            System.out.println("Total time is:"+minutes+" min "+seconds+" sec"); //выводим время в минутах и секундах
        } if (((totalDownloadTime/dimension)>=3600)){ //если общее время больше 1 часа
            hours= (int) (Math.floor((totalDownloadTime/dimension)/3600)); //считаем часы
            minutes= (int) (Math.floor(((totalDownloadTime/dimension)/3600-hours)*60)); //считаем минуты
            seconds= (int) (Math.round(totalDownloadTime/dimension-hours*3600-minutes*60)); //считаем секунды
            System.out.println("Total time is:"+hours+"h "+minutes+"min "+seconds+"sec "); //выводим время в часах, минутах и секундах
        } if ((totalDownloadTime/dimension)<1)
            System.out.println("Total time is:"+totalDownloadTime/dimension+" sec");

        System.out.println("Total download size is:"+totalDownloadSize+" bytes"); //выводим общий размер загрузок
    }

}
