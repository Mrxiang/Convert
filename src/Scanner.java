import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.util.*;

public class Scanner {



    public void convertStringsWithDictonary( File inFilePath,  Map<String,String> map)throws  IOException{
        System.out.println("转换 ："+ inFilePath.getPath() );

//        File targetFile = new File(  inFilePath + File.separator + Utils.TARGET );
//        if( !targetFile.exists() ){
//
//            targetFile.createNewFile();
//        }
        List<File>   fileList = new ArrayList<File>();
        fileList = Utils.getFileList( inFilePath.getPath(), ".xml");

        String outFilePath = inFilePath.getParentFile().getPath()+File.separator+ Utils.VALUE_PATH+File.separator+ Utils.MATCHED_FILE;
        File    outFile = new File( outFilePath );
        if(  !outFile.exists() ){
            if( !outFile.getParentFile().exists() ){
                outFile.getParentFile().mkdir();
            }
            outFile.createNewFile();
        }
        if( !outFile.exists()){
            System.out.println( "  无效路径 "+ outFilePath);
            return;
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
//设置根<tax_institutions></tax_institutions>元素
        Element newRoot = new Element(Utils.ELEMENT_RESOURCES );
        Document newDoc = new Document(newRoot);
        for( File inFile: fileList) {
            try {
                // 2.创建一个输入流，将xml文件加载到输入流中
                String  filePath = inFile.getPath();
                in = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                // 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
                Document document = saxBuilder.build(isr);
                // 4.通过document对象获取xml文件的根节点
                Element rootElement = document.getRootElement();
                // 5.获取根节点下的子节点的List集合
                List<Element> rowList = rootElement.getChildren();
                // 继续进行解析
                int index = 0;
                Iterator<Element> iter = rowList.iterator();
                while (iter.hasNext()) {
                    Element row = iter.next();
                    if (row.getName().equals("string")) {
//                    System.out.println("======开始转换第" + (index++) + "行======");
                        // 解析属性集合
                        List<Attribute> attrList = row.getAttributes();
                        // //知道节点下属性名称时，获取节点值
                        // 遍历attrList(针对不清楚节点下属性的名字及数量)
                        String resID = "";
                        for (Attribute attr : attrList) {
                            // 获取属性名
                            String attrName = attr.getName();
                            // 获取属性值
                            String attrValue = attr.getValue();
//                        System.out.println("属性名：" + attrName + "----属性值："+ attrValue);
                            if (attrName.equals("name")) {
                                resID = attrList.get(0).getValue();
                            }
                        }
                        List<Content> content = row.getContent();
                        if (content != null) {
                            String esValue = "";
                            for (int i = 0; i < content.size(); i++) {
                                String value = content.get(i).getValue();
                                esValue += value;

                            }
                            System.out.println("匹配:" + esValue);
                            if (!esValue.equals("")) {
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    if (entry.getKey().equals(esValue)) {
                                        String newName = resID;
                                        String newContent = entry.getValue();
                                        Element rowElement = new Element("string");

                                        rowElement.setAttribute("name", newName);
                                        rowElement.addContent(newContent);
                                        newRoot.addContent(rowElement);
                                        System.out.println("匹配到: " + newName + " : " + newContent);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
                FileOutputStream fo = new FileOutputStream(outFile);
                Format format = Format.getCompactFormat().setEncoding("UTF-8")
                        .setIndent("");
                XMLOutputter XMLOut = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
                XMLOut.output(newDoc, fo);
                fo.close();
                System.out.println("转换结束 !");
            } catch (Exception e) {
                System.out.println();
                e.printStackTrace();
            }
        }
        return ;
    }


    public  void traverseStringsAndConvert(String path, Map<String, String> dictionary)throws IOException {
//        System.out.println(" traverseFolderAndConvert: "+ path );
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!"+file.getPath());
//                return;
            } else {
//                System.out.println(" ** "+ file.getName());
                for (File file2 : files) {
//                    System.out.println(""+ file2.getName());
                    if (file2.isDirectory() ) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        if( file2.getName().equals( Utils.VALUE )){
                            System.out.println("文件夹:" + file2.getAbsolutePath());
                            convertStringsWithDictonary( file2 , dictionary );
                        }{
                            traverseStringsAndConvert(file2.getAbsolutePath(), dictionary);
                        }

                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
//            System.out.println("文件不存在!");
        }
//        System.out.println(" traverseFolderAndConvert: END!" );
    }
    public  void traverseFolder(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory() ) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        if( file2.getName().equals( Utils.VALUE)){
                            System.out.println("文件夹:" + file2.getAbsolutePath());
                        }{
                            traverseFolder(file2.getAbsolutePath());
                        }

                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    /* 扫描 strings.xml 文件 ， 用 strings.xml 的值匹配 map 的键， 取出 strings.xml 的键 和 map 的值， 组合到新的Document 中*/
    public Document   convertNewFile(  File xmlFile, Map<String,String> map){
        System.out.println("转换 ："+ xmlFile.getPath() );
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
//设置根<tax_institutions></tax_institutions>元素
        Element newRoot = new Element("Resource");
        Document newDoc = new Document(newRoot);
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
//            Element row = rowList.get(0)
//            for (Element row : rowList) {
            int index=0;
            Iterator<Element> iter = rowList.iterator();
            while(iter.hasNext()){
                Element row = iter.next();
                if( row.getName().equals( "string")){
//                    System.out.println("======开始转换第" + (index++) + "行======");
                    // 解析属性集合
                    List<Attribute> attrList = row.getAttributes();
                    // //知道节点下属性名称时，获取节点值
                    // 遍历attrList(针对不清楚节点下属性的名字及数量)
                    String   resID="";
                    for (Attribute attr : attrList) {
                        // 获取属性名
                        String attrName = attr.getName();
                        // 获取属性值
                        String attrValue = attr.getValue();
//                        System.out.println("属性名：" + attrName + "----属性值："+ attrValue);

                        if( attrName.equals( "name") ){
                            resID = attrList.get(0).getValue();
                        }
                    }
                    List<Content> content = row.getContent();
                    if( content != null ){

                        String esValue = "";
                        for( int i=0 ; i<content.size(); i++ ){
                            String  value = content.get(i).getValue();
                            esValue += value;
                        }
                        System.out.println( "匹配:"+ esValue );
                        if( !esValue.equals("")){
                            for(Map.Entry<String, String> entry :map.entrySet() ){
                                if( entry.getKey().equals( esValue )  ){
                                    String  newName = resID;
                                    String  newContent = entry.getValue();
                                    Element rowElement = new Element( "string");

                                    rowElement.setAttribute("name", newName);
                                    rowElement.addContent( newContent);
                                    newRoot.addContent( rowElement);
                                    System.out.println("匹配到: "+newName + " : "+ newContent);
                                    break;
                                }
                            }
                        }
                    }

                }
            }

        }  catch (Exception e) {
            System.out.println( );
            e.printStackTrace();
        }
        return newDoc;
    }

    public void convertStringWithDictonary( File inFile,  Map<String,String> map)throws  IOException{
        System.out.println("转换 ："+ inFile.getPath() );
        String inFileDir = inFile.getParentFile().getPath();
        File targetFile = new File(  inFileDir + File.separator + Utils.MATCHED_FILE );
        if( !targetFile.exists() ){

            targetFile.createNewFile();
        }
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
//设置根<tax_institutions></tax_institutions>元素
        Element newRoot = new Element("Resource");
        Document newDoc = new Document(newRoot);
        try {
            // 2.创建一个输入流，将xml文件加载到输入流中
            String inFilePath = inFile.getPath();
            in = new FileInputStream( inFilePath  );
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            // 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
            Document document = saxBuilder.build(isr);
            // 4.通过document对象获取xml文件的根节点
            Element rootElement = document.getRootElement();
            // 5.获取根节点下的子节点的List集合
            List<Element> rowList = rootElement.getChildren();
            // 继续进行解析
            int index=0;
            Iterator<Element> iter = rowList.iterator();
            while(iter.hasNext()){
                Element row = iter.next();
                if( row.getName().equals( "string")){
//                    System.out.println("======开始转换第" + (index++) + "行======");
                    // 解析属性集合
                    List<Attribute> attrList = row.getAttributes();
                    // //知道节点下属性名称时，获取节点值
                    // 遍历attrList(针对不清楚节点下属性的名字及数量)
                    String   resID="";
                    for (Attribute attr : attrList) {
                        // 获取属性名
                        String attrName = attr.getName();
                        // 获取属性值
                        String attrValue = attr.getValue();
//                        System.out.println("属性名：" + attrName + "----属性值："+ attrValue);
                        if( attrName.equals( "name") ){
                            resID = attrList.get(0).getValue();
                        }
                    }
                    List<Content> content = row.getContent();
                    if( content != null ){
                        String esValue = "";
                        for( int i=0 ; i<content.size(); i++ ){
                            String  value = content.get(i).getValue();
                            esValue += value;

                        }
                        System.out.println( "匹配:"+ esValue );
                        if( !esValue.equals("")){
                            for(Map.Entry<String, String> entry :map.entrySet() ){
                                if( entry.getKey().equals( esValue )  ){
                                    String  newName = resID;
                                    String  newContent = entry.getValue();
                                    Element rowElement = new Element( "string");

                                    rowElement.setAttribute("name", newName);
                                    rowElement.addContent( newContent);
                                    newRoot.addContent( rowElement);
                                    System.out.println("匹配到: "+newName + " : "+ newContent);
                                    break;
                                }
                            }
                        }
                    }

                }
            }
            FileOutputStream fo = new FileOutputStream( targetFile );
            Format format = Format.getCompactFormat().setEncoding("UTF-8")
                    .setIndent("");
            XMLOutputter XMLOut = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
            XMLOut.output(newDoc, fo);
            fo.close();
            System.out.println("转换结束 !");
        }  catch (Exception e) {
            System.out.println( );
            e.printStackTrace();
        }
        return ;
    }
}
