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

//        translateWithDicFromExcel( );

        translateWithSDictionary();
        System.out.println("Bye!");
    }

    public static void translateWithDicFromExcel() {
        //        1.生成字典
        String customerFilePath = "D:\\xml2xml";
        Dictionary dictionary = new Dictionary();
        dictionary.generateDictionary(customerFilePath);

//        2.在 android 工程目录下转换出行的 语言文件
//        String strFilePath  = "D:\\xml2xml\\strings.xml";
//        File strFile = new File( strFilePath );
        Scanner scanner = new Scanner();
        try {
//            scanner.convertStringWithDictonary(strFile , dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SETTINGS, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_CONTACTS, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_DIALER, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SYSTEMUI, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SYSTEMUI_FACEUNLOCK, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_GALLERY2, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_FRAMEWORK_RES, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_FMRADIO, dictionary.getMap() );
            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_MESSAGE, dictionary.getMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void translateWithSDictionary() {
//        Translator.translateWithBING(Utils.EN,Utils.SR_LATN, "hello");
//        1.读取 String.xml ，遍历 string
//        2.拿string 的value 到 数据库中比较，如果匹配上，则 输出到新的 string.xml， 否则到 bing 上去翻译.翻译结果加入到数据库中，同时结果输出到新的string.xml.
        Worker worker = new Worker();
        worker.initDictionay( Utils.VALUES_B_SR_LATN );
//        worker.loadXLStoDictionary( PATH  );
//        boolean result = worker.translateStringXml(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION, worker.getDictionary());
        try {
            worker.translateStringsWithDictonary(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION_RES, worker.getDictionary());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("bye!");
    }
}