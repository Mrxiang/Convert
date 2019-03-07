import org.apache.poi.ss.usermodel.*;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.awt.print.Book;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SDictionary {

    private static String Drivde="org.sqlite.JDBC";

    private  static  SDictionary dictionary;

    private  String languageType= null;
    private SDictionary(){

        createDatabase();
    }

    public static SDictionary  getInstance( ){
        if( dictionary == null ){
            dictionary = new SDictionary();
        }
        return dictionary;
    }

    public int createDatabase( ){
        System.out.println("createDatabase");//

        int resultCode=0;
        try {
            Class.forName(Drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/dictionary.db");//连接数据库zhou.db,不存在则创建
            String sql = Utils.CREATE_DICTIONARY_SQL;
            System.out.println("createDatabase:"+sql);//
            PreparedStatement statement = connection.prepareStatement(sql);   //创建连接对象，是Java的一个操作数据库的重要接口
            resultCode = statement.executeUpdate( );            //创建数据库
            statement.close();
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultCode;
    }

    public String selectText(String language_type,String  text) {
        String result= null;
        try {
            Class.forName(Drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/dictionary.db");//连接数据库zhou.db,不存在则创建
            Statement statement = connection.createStatement();   //创建连接对象，是Java的一个操作数据库的重要接口
//            String sql = "create table tables(name varchar(20),pwd varchar(20))";
            String sql = String.format( Utils.QUERY_DICTIONARY_SQL, language_type, text);
            System.out.println( "selectText sql:"+sql );
            ResultSet rSet = statement.executeQuery(sql );//搜索数据库，将搜索的放入数据集ResultSet中
            if( rSet.getRow() == 0 ){
                return result;
            }
            while (rSet.next()) {            //遍历这个数据集
                System.out.println("语言：" + rSet.getString("language_type"));//依次输出 也可以这样写 rSet.getString(“name”)
                System.out.println("源文：" + rSet.getString("source_text"));//依次输出 也可以这样写 rSet.getString(“name”)
                System.out.println("译文：" + rSet.getString("translated_text"));
                result = rSet.getString( "translated_text");
            }
            rSet.close();//关闭数据集
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public int getRowCount() {
        int count = 0;
        try {
            Class.forName(Drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/dictionary.db");//连接数据库zhou.db,不存在则创建
            Statement statement = connection.createStatement();   //创建连接对象，是Java的一个操作数据库的重要接口
//            String sql = "create table tables(name varchar(20),pwd varchar(20))";
            String sql = String.format( Utils.QUERY_DICTIONARY_COUNT_SQL );
            System.out.println( "getRowCount sql:"+sql );
            ResultSet rSet = statement.executeQuery(sql );//搜索数据库，将搜索的放入数据集ResultSet中
            count = rSet.getRow();
            rSet.close();//关闭数据集
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return count;
    }

    public String getLanguageType( ) {
        return languageType;
    }

    public  void setLanguageType(String languageType ){
        this.languageType = languageType;
    }
    public String getBingLanguageType( String languageType){

        if( languageType == Utils.VALUES_B_SR_LATN ){
            return Utils.BING_SR;
        }
        return null;
    }
    public int insertText(String languageType, String esValue, String result) {

        int resultRow=0;
        try {
            Class.forName(Drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/dictionary.db");//连接数据库 dictionary.db,不存在则创建
            String sql =  Utils.INSERT_DICTIONARY_SQL ;
            System.out.println( "insertText sql:"+sql+ " "+languageType+" "+esValue+" "+result );
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.setString(1, languageType );
            preparedStatement.setString( 2, esValue);
            preparedStatement.setString( 3,result);
            resultRow = preparedStatement.executeUpdate(  );
            System.out.println( "insertText result:"+resultRow );
            preparedStatement.close();
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultRow;
    }
    public boolean  buildDictonaryFromXLS(String xlsFilePath ) {

        System.out.println( "generateDictionary "+ xlsFilePath);

        File newDictionaryFile = new File(  xlsFilePath+File.separator +Utils.DICTIONARY);
        try {
            if (!newDictionaryFile.exists()) {
                newDictionaryFile.createNewFile();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        List<File> fileList = new ArrayList<File>();
        fileList = Utils.getFileList( xlsFilePath, "xls");
//        1.将 xls 文件夹转成 dictionary.xml 文件
        try {
            FileOutputStream fo = new FileOutputStream( newDictionaryFile );// 得到输入流
            Element root = new Element("Resource");
            Document doc =  new Document( root);// 读取EXCEL函数
            Format format = Format.getCompactFormat().setEncoding("UTF-8")
                    .setIndent("");
            for (File file: fileList) {

                convertXLS2XML(file, root);
            }
            XMLOutputter XMLOut = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
            XMLOut.output(doc, fo);
            fo.close();
            System.out.println("转换结束 !");
        }catch ( Exception e){
            e.printStackTrace();
            return false;
        }
//        2.将 dictionary.xml  文件转成  Hashmap
        convertXml2DB( newDictionaryFile, dictionary );
        System.out.println( "generateDictionary  success!"+ xlsFilePath);
        return true;
    }
    private  void convertXLS2XML( File xlsFile , Element root)
            throws IOException {
        if( xlsFile == null  ){
            return;
        }
        System.out.println( "convertXLS2XML  "+ xlsFile.getPath());
        readExcell( xlsFile.getPath() ,  root);// 读取EXCEL函数
        System.out.println("转换结束 !");
    }
    private  boolean readExcell(String excelPath, Element root) {
        System.out.println("readExcell  "+ excelPath );
//设置根<tax_institutions></tax_institutions>元素
//        Element root = new Element("Resource");
//        Document doc = new Document(root);
        try {
            Workbook wb = WorkbookFactory.create(new File(excelPath));
//获取工作薄的个数，即一个excel文件中包含了多少个Sheet工作簿
            int WbLength = wb.getNumberOfSheets();
            System.out.println("总页数:" + WbLength);
//            对每一个工作簿进行操作
            for (int i = 0; i < WbLength; i++) {
                Sheet shee = wb.getSheetAt(i);
                int sheetRows = shee.getLastRowNum();
                System.out.println("总行数：" + sheetRows);
//               对每一行进行操作
                for (int j = 1; j <= sheetRows; j++) {
                    Element rowElement = new Element( "row");
                    Row row = shee.getRow(j);
                    if (row == null) {
                        continue;
                    }
                    int cellNum = row.getPhysicalNumberOfCells();// 获取一行中最后一个单元格的位置
//                    System.out.println( " 第 "+ j +" 行 ,有"+cellNum+" 列 ");
//设置根元素下的并列元素<tax_institution></tax_institution>
                    Element  appPath            = new Element("app-path");
                    Element  resId              = new Element("res-id");
                    Element enLaunguage         = new Element("values");
                    Element  cnLanguage         = new Element("values-ch");
                    Element hrLaunguage         = new Element( "values-hr");
// 对每一列操作
                    for (int cellCol = 0; cellCol < cellNum; cellCol++) {

                        Cell cell = row.getCell((short) cellCol);

                        if (cell == null) {
                            cellNum++;//如果存在空列，那么cellNum增加1，这一步很重要。
                            continue;
                        } else {

                            String value= "";
                            if(cell.getCellType() == CellType.STRING) {
                                value = cell.getStringCellValue();
                            }else if(cell.getCellType() == CellType.NUMERIC){
                                value = ""+cell.getNumericCellValue();

                            }else{
                                System.out.println("错误类型 "+ cell.getCellType()+"");
                            }
                            if( cellCol == 0){
                                appPath.setText( value );
                            }else if( cellCol == 1){
                                resId.setText( value );
                            }else if( cellCol == 2){
                                enLaunguage.setText( value );
                            }else if( cellCol == 3){
                                cnLanguage.setText( value );
                            }else if( cellCol == 4){
                                hrLaunguage.setText( value );
                            }
//                            e.setText( value );
//                            System.out.print(cellCol + " " + value + ";");
                        }
                    }
                    rowElement.addContent( appPath );
                    rowElement.addContent( resId );
                    rowElement.addContent( enLaunguage );
                    rowElement.addContent( cnLanguage );
                    rowElement.addContent( hrLaunguage );
                    root.addContent( rowElement );
//                    System.out.println( );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
//        return doc;
        return true;
    }
    public boolean convertXml2DB(File xmlFile, SDictionary dictionary) {

        System.out.println( "convertXml2DB: "+ xmlFile.getPath());
        if( dictionary == null ){
            return false;
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        try {
            // 2.创建一个输入流，将xml文件加载到输入流中
            String filePath = xmlFile.getPath();
            in = new FileInputStream( filePath  );
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            // 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
            Document document = saxBuilder.build(isr);
            // 4.通过document对象获取xml文件的根节点
            Element rootElement = document.getRootElement();
            // 5.获取根节点下的子节点的List集合
            List<Element> rowList = rootElement.getChildren();
            // 继续进行解析
            for (Element row : rowList) {
                Book bookEntity = new Book();
                System.out.println("======开始解析第" + (rowList.indexOf(row) + 1)
                        + "行======");
                // 解析属性集合
                List<Attribute> attrList = row.getAttributes();
                // //知道节点下属性名称时，获取节点值
                // 遍历attrList(针对不清楚节点下属性的名字及数量)
                for (Attribute attr : attrList) {
                    // 获取属性名
                    String attrName = attr.getName();
                    // 获取属性值
                    String attrValue = attr.getValue();
//                    System.out.println("属性名：" + attrName + "----属性值："
//                            + attrValue);
                }
                List<Element> rowChilds = row.getChildren();
                String key      = null;
                String value    = null;
                for (Element child : rowChilds) {
                    if (child.getName().equals( Utils.VALUE)) {
                        key =  child.getValue().trim();
                    }
                    else if (child.getName().equals(Utils.VALUE_HR)) {
                        value = child.getValue().trim();
                    }
                }
                if( key != null && value !=null ) {
                    System.out.println("key:"+key+" value :"+value);
//                    map.put(key, value);
                    dictionary.insertText( getLanguageType(), key, value );
                }
            }
            System.out.println("======生成字典结束====");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
