import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String  VALUE_HR ="values-hr";
    public static final String  VALUE ="values";
    public static final String  DICTIONARY ="Dictonary.xml";

    public static final String  TARGET ="xshx_string.xml";
    public static final String  VALUE_PATH ="values-b+sr+Latn";

    public static final String  ANDROID_PATH_TEST ="D:\\DataProtection";
    public static final String  ANDROID_PATH ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps";
    public static final String  ANDROID_PATH_PACKAGE_SETTINGS ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\MtkSettings";
    public static final String  ANDROID_PATH_PACKAGE_CONTACTS ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Contacts";
    public static final String  ANDROID_PATH_PACKAGE_DIALER ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\Dialer";
    public static final String  ANDROID_PATH_PACKAGE_SYSTEMUI ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\SystemUI";
    public static final String  ANDROID_PATH_PACKAGE_SYSTEMUI_FACEUNLOCK ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\SystemUI_faceUnlock";
    public static final String  ANDROID_PATH_PACKAGE_DATAPROCTECTION ="\\\\192.168.12.224\\xshx\\Project\\mtk67xx_o1_mp1_dev\\alps\\vendor\\mediatek\\proprietary\\packages\\apps\\DataProtection";

    public static final String  ELEMENT_RESOURCES ="resources";

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
}
