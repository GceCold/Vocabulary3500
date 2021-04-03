package ltd.icecold.vocabulary;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author ice-cold
 */
public class Main {
    private static Map<String,String> vocabulary = new HashMap<>();
    public static void main(String[] args) throws IOException, BiffException {
        InputStream resource = getResource("3500.txt");
        InputStreamReader read = new InputStreamReader(resource);
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt;
        while((lineTxt = bufferedReader.readLine()) != null){
            String[] s = lineTxt.split(" ");

            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 1;i<s.length;i++){
                stringBuffer.append(s[i]).append(" ");
            }

            vocabulary.put(s[0],stringBuffer.toString().trim());
        }
        read.close();
        resource.close();
        bufferedReader.close();



        Scanner input=new Scanner(System.in);
        input.useDelimiter("\n");

        new Thread(()->{
            input(input);
        }).start();


    }

    public static void input(Scanner input){
        String command=input.next();
        try {
            if (vocabulary.containsKey(command)){
                writeVocabulary(command+" "+vocabulary.get(command));
                System.out.println(vocabulary.get(command));
            }else {
                System.out.println("未找到单词");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        input(input);
    }

    public static void writeVocabulary(String words) throws WriteException, IOException {
        File excel = new File("check.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(excel);
        WritableWorkbook workbook = Workbook.createWorkbook(fileOutputStream);

        List<String> sheets = new ArrayList<>(Arrays.asList(workbook.getSheetNames()));
        WritableSheet sheet ;
        if (!sheets.contains("Unknown Vocabulary")){
            workbook.createSheet("Unknown Vocabulary",0);
        }
        sheet = workbook.getSheet("Unknown Vocabulary");

        String[] s = words.split(" ");

        int rows = sheet.getRows();

        sheet.setColumnView(0,35);
        sheet.setColumnView(1,35);
        sheet.setColumnView(2,60);


        WritableFont wordFont = new WritableFont(WritableFont.createFont("微软雅黑"),14,WritableFont.BOLD);
        WritableCellFormat wordFormate = new WritableCellFormat(wordFont);
        wordFormate.setAlignment(jxl.format.Alignment.CENTRE);
        wordFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Label word = new Label(0,rows,s[0],wordFormate);
        sheet.addCell(word);

        WritableFont phoneticFont = new WritableFont(WritableFont.createFont("Lucida Sans Unicode"),14);
        WritableCellFormat phoneticFormate = new WritableCellFormat(phoneticFont);
        Label phonetic = new Label(1,rows,s[1],phoneticFormate);
        sheet.addCell(phonetic);

        WritableFont chineseFont = new WritableFont(WritableFont.createFont("微软雅黑"),12);
        WritableCellFormat chineseFormate = new WritableCellFormat(chineseFont);

        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 2;i<s.length;i++){
            stringBuffer.append(s[i]).append(" ");
        }

        Label chinese = new Label(2,rows,stringBuffer.toString().trim().replace("a.","adj.").replace("ad.","adv."),chineseFormate);;

        sheet.addCell(chinese);

        workbook.write();
        workbook.close();
        fileOutputStream.close();
    }

    public static InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = Main.class.getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
}
