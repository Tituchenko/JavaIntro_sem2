import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main  {
    public static void main(String[] args){
        String exampleStr="{{\"фамилия\":\"Иванов\",\"оценка\":\"5\",\"предмет\":\"Математика\"},{\"фамилия\":\"Петрова\",\"оценка\":\"4\",\"предмет\":\"Информатика\"},{\"фамилия\":\"Краснов\",\"оценка\":\"5\",\"предмет\":\"Физика\"}}";


        String menu="";
        Boolean runAgain=true;
        do {
            menu=showMenu();
            switch (menu){
                case "1":
                    System.out.println(ex1(exampleStr));
                    break;
                case "2":
                    ex2(exampleStr,"result.txt","log.txt");
                    break;
                case "3":
                    ex3("result.txt");
                    break;
                case "4":
                    ex4(getRandomArray(10,9),"buble.log");
                    break;


                case "0":
                    runAgain=false;
                    break;
                default:
                    System.out.println("Ошибка ввода пункта!");
                    break;
            }

        } while (runAgain);


    }
    static String showMenu(){
        System.out.println("Выберите задачу:");
        System.out.println("1.Распарсить json строку.");
        System.out.println("2.Создать метод, который запишет результат работы в файл Обработайте исключения и запишите ошибки в лог файл");
        System.out.println("3.*Получить исходную json строку из файла, используя FileReader или Scanner");
        System.out.println("4.*Реализуйте алгоритм сортировки пузырьком числового массива, результат после каждой итерации запишите в лог-файл.");
        System.out.println("0.Закончить");
        Scanner scanner = new Scanner(System.in);
        String p = scanner.nextLine();
        return p;
    }
    /*
    1.  Дана json строка
        {{"фамилия":"Иванов","оценка":"5","предмет":"Математика"},{"фамилия":"Петрова","оценка":"4","предмет":"Информатика"},{"фамилия":"Краснов","оценка":"5","предмет":"Физика"}}
        Задача написать метод(ы), который распарсить строку и выдаст ответ вида:
        Студент Иванов получил 5 по предмету Математика.
        Студент Петрова получил 4 по предмету Информатика.
        Студент Краснов получил 5 по предмету Физика.
        Используйте StringBuilder для подготовки ответа
     */
    static String ex1(String string){
        StringBuilder stringBuilder= new StringBuilder();

        String[] string_parts=clear(string).split(",");
        for (int i = 0; i < string_parts.length; i++) {
            String[] elements=clear(string_parts[i]).split(",");
            for (int j = 0; j < elements.length; j++) {
                String[] values=elements[j].replace("\"", "").split(":");
                switch (values[0]){
                    case "фамилия":
                        stringBuilder.append("Студент ");
                        stringBuilder.append(values[1]);
                        break;
                    case "оценка":
                        stringBuilder.append(" получил ");
                        stringBuilder.append(values[1]);
                        break;
                    case "предмет":
                        stringBuilder.append(" по предмету ");
                        stringBuilder.append(values[1]);
                        stringBuilder.append(".\n");
                        break;
                }
            }



        }
        return stringBuilder.toString();




    }
    //Если в начали { или } в конце то удалим это.
    static String clear(String s){
        int Start,Finish;
        if (s.charAt(0)=='{'){
            Start=1;
        } else {
            Start=0;
        }
        if (s.charAt(s.length()-1)=='}'){
            Finish=s.length()-1;
        } else {
            Finish=s.length();
        }
        return s.substring(Start,Finish);
    }
    //2.Создать метод, который запишет результат работы в файл Обработайте исключения и запишите ошибки в лог файл.
    static void ex2(String s, String resultFile, String logName){
        String result="";
        Logger logger = Logger.getAnonymousLogger();

        SimpleFormatter formatter = new SimpleFormatter();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(logName);
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        logger.log(Level.INFO, "Тестовое логирование");
        try {
            result=ex1(s);
            logger.info("Строка распарсена");
        } catch (Exception e) {
            logger.warning(e.getMessage());
        } finally {
            if (result.length()==0){
                logger.warning("Результат распарсивания пустой!");
            }
        }

        try {
             File file = new File(resultFile);
            Boolean appending;
            if (file.createNewFile()) {
                logger.info("Файл с выводом создан");
                appending=false;
            }
            else {
                logger.info("Файл с выводом существует");
                appending=true;
            }
            FileWriter fileWriter = new FileWriter(file, appending);
            fileWriter.append(result);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }



        }
        static String getJSON (String s){
        String[] arrStr;
        String result;
            arrStr=s.split(" ");
            result="{\"фамилия\":\""+arrStr[1]+"\"," +
                    "\"оценка\":\""+arrStr[3]+
                    "\",\"предмет\":\""+arrStr[6].substring(0,arrStr[6].length()-1)+"\"}";
            return result;

        }

    //Получить исходную json строку из файла, используя FileReader или Scanner
        static void ex3(String fileName){
            File file = new File(fileName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            try (Scanner scanner = new Scanner(file)){
                while (scanner.hasNext()){
                    stringBuilder.append(getJSON(scanner.nextLine()));
                    if (scanner.hasNext()){
                        stringBuilder.append(",");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            stringBuilder.append("{");stringBuilder.append("}");


            System.out.println(stringBuilder.toString());

        }

        //4.*Реализуйте алгоритм сортировки пузырьком числового массива,
        // результат после каждой итерации запишите в лог-файл.
        static void ex4(int[] arr,String nameLog){
            Logger logger = Logger.getAnonymousLogger();

            SimpleFormatter formatter = new SimpleFormatter();
            FileHandler fileHandler = null;
            try {
                fileHandler = new FileHandler(nameLog);
                fileHandler.setFormatter(formatter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, "Входной массив:"+Arrays.toString(arr));

            int temp;
            for (int i = arr.length-1; i >= 1; i--) {
                for (int j = 0; j < i; j++) {
                    if (arr[j]>arr[i]) {
                        temp=arr[i];
                        arr[i]=arr[j];
                        arr[j]=temp;
                    }
                }
                logger.log(Level.INFO, "Итерация" +(i-arr.length)+":"+Arrays.toString(arr));
                
            }
            logger.log(Level.INFO, "Отсортированный массив:"+Arrays.toString(arr));


        }
        //Создание случайного массива длиной n, максимальное число Max
        static int[] getRandomArray(int n, int max){
            int[] arr=new int[n];

            Random rand = new Random();
            for (int i = 0; i < n; i++) {
                arr[i]=rand.nextInt(max+1);
            }
            return arr;
        }
}