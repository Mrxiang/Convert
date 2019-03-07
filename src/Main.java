//import org.apache.poi.hslf.record.Document;
//import org.apache.poi.hssf.usermodel.Cell;
//import org.apache.poi.hssf.usermodel.Row;
//import org.apache.poi.hssf.usermodel.Sheet;
//import org.apache.poi.hssf.usermodel.Workbook ;
//import org.jdom.Document;
//import java.text.Format;


public class Main {


    public static void main(String[] args) {
        System.out.println("Welcome!");
        translateWithSDictionary();
        System.out.println("Bye!");
    }

    public static void translateWithSDictionary() {
//        Translator.translateWithBING(Utils.EN,Utils.SR_LATN, "hello");
//        1.读取 String.xml ，遍历 string
//        2.拿string 的value 到 数据库中比较，如果匹配上，则 输出到新的 string.xml， 否则到 bing 上去翻译.翻译结果加入到数据库中，同时结果输出到新的string.xml.
        String customerFilePath = "D:\\xml2xml";
        Worker worker = new Worker();
        worker.getDictionary( Utils.VALUES_B_SR_LATN ).buildDictonaryFromXLS( customerFilePath );
//        worker.loadXLStoDictionary( PATH  );
//        boolean result = worker.translateStringXml(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION, worker.getDictionary());
        try {
            worker.translateStringsWithDictonary(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION_RES, worker.getDictionary( Utils.VALUES_B_SR_LATN ));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("bye!");
    }
}