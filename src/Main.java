//import org.apache.poi.hslf.record.Document;
import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.usermodel.Cell;
//import org.apache.poi.hssf.usermodel.Row;
//import org.apache.poi.hssf.usermodel.Sheet;
//import org.apache.poi.hssf.usermodel.Workbook ;
//import org.jdom.Document;
import java.io.*;
//import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.jdom2.output.Format;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import javax.rmi.CORBA.Util;

public class Main {


    public static void main(String[] args) {
        System.out.println("Welcome!");

        translateWithDicFromExcel( );

        System.out.println("Bye!");
//        scanner.traverseFolder(Utils.ANDROID_PATH_TEST );
    }

    public static void  translateWithDicFromExcel( ){
        //        1.生成字典
        String  customerFilePath  = "D:\\xml2xml";
        Dictionary dictionary = new Dictionary();
        dictionary.generateDictionary( customerFilePath );

//        2.在 android 工程目录下转换出行的 语言文件
        String strFilePath  = "D:\\xml2xml\\strings.xml";
        File strFile = new File( strFilePath );
        Scanner   scanner = new Scanner();
        try{
//            scanner.convertStringWithDictonary(strFile , dictionary.getMap() );
            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SETTINGS, dictionary.getMap() );
            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_CONTACTS, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_DIALER, dictionary.getMap() );
            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SYSTEMUI, dictionary.getMap() );
            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_SYSTEMUI_FACEUNLOCK, dictionary.getMap() );
//            scanner.traverseStringsAndConvert(Utils.ANDROID_PATH_PACKAGE_DATAPROCTECTION, dictionary.getMap() );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void translateWithDicFromNetwork( ){

    }
}
