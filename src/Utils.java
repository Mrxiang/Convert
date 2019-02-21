import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String  VALUE_HR ="values-hr";
    public static final String  VALUE ="values";
    public static final String  DICTIONARY ="Dictonary.xml";

    public static final String  MATCHED_FILE = "matched_string.xml";
    public static final String  UN_MATCHED_FILE = "unmatch_string.xml";
    public static final String  VALUE_PATH ="values-b+sr+Latn";

    public static final String  ANDROID_PATH_TEST ="D:\\DataProtection";
    public static final String  ANDROID_PATH ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps";
    public static final String  ANDROID_PATH_PACKAGE_SETTINGS ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\MtkSettings";
    public static final String  ANDROID_PATH_PACKAGE_CONTACTS ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Contacts";
    public static final String  ANDROID_PATH_PACKAGE_DIALER ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Dialer";
    public static final String  ANDROID_PATH_PACKAGE_SYSTEMUI ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\SystemUI";
    public static final String  ANDROID_PATH_PACKAGE_SYSTEMUI_FACEUNLOCK ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\SystemUI_faceUnlock";
    public static final String  ANDROID_PATH_PACKAGE_DATAPROCTECTION ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\DataProtection";
    public static final String  ANDROID_PATH_PACKAGE_DATAPROCTECTION_RES ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\DataProtection\\res\\values\\strings.xml";
    public static final String  ANDROID_PATH_PACKAGE_GALLERY2 ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Gallery2";
    public static final String  ANDROID_PATH_PACKAGE_FRAMEWORK_RES ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\frameworks\\base\\core\\res";
    public static final String  ANDROID_PATH_PACKAGE_FMRADIO ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\FMRadio";
    public static final String  ANDROID_PATH_PACKAGE_MESSAGE ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Mms";

    public static final String  ELEMENT_RESOURCES ="resources";
    public static final String  BING_TRAMSLATOR ="http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=AFC76A66CF4F434ED080D245C30CF1E71C22959C&from=%s&to=%s&text=%s";
    public static final String  BING_TRAMSLATOR_BEGIN ="http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=AFC76A66CF4F434ED080D245C30CF1E71C22959C&from=";
//value="zh-CHS"	中文(简体)
//value="zh-CHT"
//value="sr-Latn    塞尔维亚语(拉丁语)
//value="en"		英语
//value="cs"        捷克语
    public static final String  SR_LATN="sr-Latn";
    public static final String  EN="en";
    public static final String  ZH_CHS="zh-CHS";

    public static List<File> getFileList(String dirPath, String  suffix ){
        File dir = new File( dirPath );
        if( !dir.exists() ){
            System.out.println("目录不存在");
            return null;
        }
        List<File>  fileList = new ArrayList<File>( );
        File[] files = dir.listFiles();
        if( files != null ){
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
//                if (files[i].isDirectory()) { // 判断是文件还是文件夹
//                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
//                }
                if ( files[i].isFile() && fileName.endsWith(suffix)) { // 判断文件名是否以.xls结尾
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println("找到文件:"+strFileName);
                    fileList.add(files[i]);
                } else {
                    continue;
                }
            }
        }
        return fileList;
    }

    public static void traverseFolder(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }


    public static final String CREATE_DICTIONARY_SQL = "create table dictionary(language_type varchar(255) not null,source_text text not null,translated_text text not null)";
    public static final String QUERY_DICTIONARY_SQL = "select * from dictionary where language_type='%s' and source_text='%s'";
    public static final String QUERY_DICTIONARY_COUNT_SQL = "select * from dictionary";
    public static final String INSERT_DICTIONARY_SQL = "insert into dictionary(language_type, source_text, translated_text) values('%s', '%s', '%s')";






//    中文（中国）：values-zh-rCN
    public static final String VALUES_ZH_RCN = "values-zh-rCN";
//    中文（台湾）：values-zh-rTW
//    中文（香港）：values-zh-rHK
//    英语（美国）：values-en-rUS
//    英语（英国）：values-en-rGB
//    英文（澳大利亚）：values-en-rAU
//    英文（加拿大）：values-en-rCA
//    英文（爱尔兰）：values-en-rIE
//    英文（印度）：values-en-rIN
//    英文（新西兰）：values-en-rNZ
//    英文（新加坡）：values-en-rSG
//    英文（南非）：values-en-rZA
//    阿拉伯文（埃及）：values-ar-rEG
//    阿拉伯文（以色列）：values-ar-rIL
//    保加利亚文:  values-bg-rBG
//    加泰罗尼亚文：values-ca-rES
//    捷克文：values-cs-rCZ
//    丹麦文：values-da-rDK
//    德文（奥地利）：values-de-rAT
//    德文（瑞士）：values-de-rCH
//    德文（德国）：values-de-rDE
//    德文（列支敦士登）：values-de-rLI
//    希腊文：values-el-rGR
//    西班牙文（西班牙）：values-es-rES
//    西班牙文（美国）：values-es-rUS
//    芬兰文（芬兰）：values-fi-rFI
//    法文（比利时）：values-fr-rBE
//    法文（加拿大）：values-fr-rCA
//    法文（瑞士）：values-fr-rCH
//    法文（法国）：values-fr-rFR
//    希伯来文：values-iw-rIL
//    印地文：values-hi-rIN
//    克罗里亚文：values-hr-rHR
//    匈牙利文：values-hu-rHU
//    印度尼西亚文：values-in-rID
//    意大利文（瑞士）：values-it-rCH
//    意大利文（意大利）：values-it-rIT
//    日文：values-ja-rJP
//    韩文：values-ko-rKR
//    立陶宛文：valueslt-rLT
//    拉脱维亚文：values-lv-rLV
//    挪威博克马尔文：values-nb-rNO
//    荷兰文(比利时)：values-nl-BE
//    荷兰文（荷兰）：values-nl-rNL
//    波兰文：values-pl-rPL
//    葡萄牙文（巴西）：values-pt-rBR
//    葡萄牙文（葡萄牙）：values-pt-rPT
//    罗马尼亚文：values-ro-rRO
//    俄文：values-ru-rRU
//    斯洛伐克文：values-sk-rSK
//    斯洛文尼亚文：values-sl-rSI
//    塞尔维亚文：values-sr-rRS
//    塞尔维亚拉丁文：values-b+sr+Latn
    public static final  String  VALUES_B_SR_LATN = "values-b+sr+Latn";
    public static final String BING_SR = "sr";

//    瑞典文：values-sv-rSE
//    泰文：values-th-rTH
//    塔加洛语：values-tl-rPH
//    土耳其文：values--r-rTR
//    乌克兰文：values-uk-rUA
//    越南文：values-vi-rVN
}
