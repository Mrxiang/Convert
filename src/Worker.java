import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Worker {

    private  SDictionary dictionary;

    public Worker( ){

    }


    public  SDictionary getDictionary(String languageType ){
        if( dictionary == null ){
            System.out.println( "初始化字典");
            dictionary = SDictionary.getInstance();
            dictionary.setLanguageType( languageType );
            int count = dictionary.getRowCount();
            System.out.println( "字典有 " +count+" 项 ");
        }
        return dictionary;
    }

    public boolean translateStringXml(String appPath, SDictionary dictionary ) {
        File file = new File(appPath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!"+file.getPath());
            } else {
//                System.out.println(" ** "+ file.getName());
                for (File file2 : files) {
//                    System.out.println(""+ file2.getName());
                    if (file2.isDirectory() ) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        if( file2.getName().equals( Utils.VALUE )){
                            System.out.println("文件夹:" + file2.getAbsolutePath());
                            try {
                                translateStringsWithDictonary(file2, dictionary);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }{
                            translateStringXml(file2.getAbsolutePath(), dictionary);
                        }

                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
//            System.out.println("文件不存在!");
        }
        return false;
    }

    public void translateStringsWithDictonary( File inFilePath,  SDictionary dictionary)throws IOException {
        System.out.println("转换 ："+ inFilePath.getPath() );

        List<File> fileList = new ArrayList<File>();
        fileList = Utils.getFileList( inFilePath.getPath(), ".xml");

        String outMatchedFilePath = inFilePath.getParentFile().getPath()+File.separator+ dictionary.getLanguageType()+File.separator+ Utils.MATCHED_FILE;
        String outUnMatchedFilePath = inFilePath.getParentFile().getPath()+File.separator+ dictionary.getLanguageType()+File.separator+ Utils.UN_MATCHED_FILE;
        File    outMatchedFile = new File( outMatchedFilePath );
        File    outUnMatchedFile = new File( outUnMatchedFilePath );
        if(  !outMatchedFile.exists() ){
            if( !outMatchedFile.getParentFile().exists() ){
                outMatchedFile.getParentFile().mkdir();
            }
            outMatchedFile.createNewFile();
        }
        if(  !outUnMatchedFile.exists() ){
            if( !outUnMatchedFile.getParentFile().exists() ){
                outUnMatchedFile.getParentFile().mkdir();
            }
            outUnMatchedFile.createNewFile();
        }
        if( !outMatchedFile.exists( ) ){
            System.out.println( " 输出文件路径无效 "+ outMatchedFilePath );
            return;
        }
        if( !outUnMatchedFile.exists( ) ){
            System.out.println( " 输出文件路径无效 "+ outUnMatchedFilePath );
            return;
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        Element newRoot = new Element(Utils.ELEMENT_RESOURCES );
        Document newDoc = new Document(newRoot);

        Element newUnRoot = new Element(Utils.ELEMENT_RESOURCES );
        Document newUnDoc = new Document(newUnRoot);

        int rowSum=0;
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
                        rowSum++;
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
                            if( content.size() == 1 ){
                                esValue = content.get(0).getValue();
                            }else{
                                for (int i = 0; i < content.size(); i++) {
                                    String value = content.get(i).getValue();
                                    esValue += value;

                                }
                            }
                            System.out.println("请求匹配:" + esValue);
                            if (!esValue.equals("")) {
                                String result = dictionary.selectText( dictionary.getLanguageType(), esValue);
                                if( result != null ){
                                    Element rowElement = new Element("string");
                                    rowElement.setAttribute( "name", resID);
                                    rowElement.addContent( result );
                                    newRoot.addContent( rowElement );
                                    System.out.println("字典中匹配到: " + esValue + " : " + result);
                                    break;
                                }else{
                                    System.out.println("字典中未匹配到: " + esValue + " : " + result);
                                    result = Translator.translateWithBING(Utils.EN, dictionary.getBingLanguageType(dictionary.getLanguageType() ), esValue  );
                                    if( result != null ){
                                        System.out.println("BING中翻译到: " + esValue + " : " + result);
                                        dictionary.insertText(dictionary.getLanguageType(), esValue, result);
                                        System.out.println("插入数据库: " + esValue + " : " + result);
                                        Element rowElement = new Element("string");
                                        rowElement.setAttribute( "name", resID);
                                        rowElement.addContent( result );
                                        newRoot.addContent( rowElement );
                                        System.out.println("插入输出文件: " + esValue + " : " + result);

                                    }else{
                                        System.out.println("字典和BING都未匹配到: " + esValue + " : " + result);
                                        Element rowUnElement = new Element("string");
                                        rowUnElement.setAttribute( "name", resID);
                                        rowUnElement.addContent( esValue );
                                        newUnRoot.addContent( rowUnElement );
                                    }

                                }
                            }

                        }

                    }
                }
                System.out.println(" 待转换的有"+ rowSum+"  个 " );
                System.out.println(" 转换的有"+ newRoot.getChildren().size()+"  个 " );
                FileOutputStream fos = new FileOutputStream(outMatchedFile);
                Format format = Format.getCompactFormat().setEncoding("UTF-8")
                        .setIndent("");
                XMLOutputter XMLOut = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
                XMLOut.output(newDoc, fos);
                fos.close();

                System.out.println(" 未转换的有 "+ newUnRoot.getChildren().size()+" 个 " );
                FileOutputStream unfos = new FileOutputStream(outUnMatchedFile);
                XMLOutputter XMLOut2 = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
                XMLOut2.output(newUnDoc, unfos);
                unfos.close();
                System.out.println("转换结束 !");
            } catch (Exception e) {
                System.out.println();
                e.printStackTrace();
            }
        }
        return ;
    }

    public void translateStringsWithDictonary( String inFile,  SDictionary dictionary)throws IOException {
        System.out.println("转换 ："+ inFile  );

//        List<File> fileList = new ArrayList<File>();
//        fileList = Utils.getFileList( inFilePath.getPath(), ".xml");
        File  inFilePath = new File( inFile );
        String outMatchedFilePath = inFilePath.getParentFile().getPath()+File.separator+ dictionary.getLanguageType()+File.separator+ Utils.MATCHED_FILE;
        String outUnMatchedFilePath = inFilePath.getParentFile().getPath()+File.separator+ dictionary.getLanguageType()+File.separator+ Utils.UN_MATCHED_FILE;
        File    outMatchedFile = new File( outMatchedFilePath );
        File    outUnMatchedFile = new File( outUnMatchedFilePath );
        if(  !outMatchedFile.exists() ){
            if( !outMatchedFile.getParentFile().exists() ){
                outMatchedFile.getParentFile().mkdir();
            }
            outMatchedFile.createNewFile();
        }
        if(  !outUnMatchedFile.exists() ){
            if( !outUnMatchedFile.getParentFile().exists() ){
                outUnMatchedFile.getParentFile().mkdir();
            }
            outUnMatchedFile.createNewFile();
        }
        if( !outMatchedFile.exists( ) ){
            System.out.println( " 输出文件路径无效 "+ outMatchedFilePath );
            return;
        }
        if( !outUnMatchedFile.exists( ) ){
            System.out.println( " 输出文件路径无效 "+ outUnMatchedFilePath );
            return;
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        Element newRoot = new Element(Utils.ELEMENT_RESOURCES );
        Document newDoc = new Document(newRoot);

        Element newUnRoot = new Element(Utils.ELEMENT_RESOURCES );
        Document newUnDoc = new Document(newUnRoot);

        int rowSum=0;
//        for( File inFile: fileList) {
            try {
                // 2.创建一个输入流，将xml文件加载到输入流中
                String  filePath = inFile;
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
                        rowSum++;
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
                            if( content.size() == 1 ){
                                esValue = content.get(0).getValue();
                            }else{
                                for (int i = 0; i < content.size(); i++) {
                                    String value = content.get(i).getValue();
                                    esValue += value;

                                }
                            }
                            System.out.println("请求匹配:" + esValue);
                            if (!esValue.equals("")) {
                                String result = dictionary.selectText( dictionary.getLanguageType(), esValue);
                                if( result != null ){
                                    Element rowElement = new Element("string");
                                    rowElement.setAttribute( "name", resID);
                                    rowElement.addContent( result );
                                    newRoot.addContent( rowElement );
                                    System.out.println("字典中匹配到: " + esValue + " : " + result);
                                    break;
                                }else{
                                    System.out.println("字典中未匹配到: " + esValue + " : " + result);
                                    result = Translator.translateWithBING(Utils.EN, dictionary.getBingLanguageType(dictionary.getLanguageType() ), esValue  );
                                    if( result != null ){
                                        System.out.println("BING中翻译到: " + esValue + " : " + result);
                                        dictionary.insertText(dictionary.getLanguageType(), esValue, result);
                                        System.out.println("插入数据库: " + esValue + " : " + result);
                                        Element rowElement = new Element("string");
                                        rowElement.setAttribute( "name", resID);
                                        rowElement.addContent( result );
                                        newRoot.addContent( rowElement );
                                        System.out.println("插入输出文件: " + esValue + " : " + result);

                                    }else{
                                        System.out.println("字典和BING都未匹配到: " + esValue + " : " + result);
                                        Element rowUnElement = new Element("string");
                                        rowUnElement.setAttribute( "name", resID);
                                        rowUnElement.addContent( esValue );
                                        newUnRoot.addContent( rowUnElement );
                                    }

                                }
                            }

                        }

                    }
                }
                System.out.println(" 待转换的有"+ rowSum+"  个 " );
                System.out.println(" 转换的有"+ newRoot.getChildren().size()+"  个 " );
                FileOutputStream fos = new FileOutputStream(outMatchedFile);
                Format format = Format.getCompactFormat().setEncoding("UTF-8")
                        .setIndent("");
                XMLOutputter XMLOut = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
                XMLOut.output(newDoc, fos);
                fos.close();

                System.out.println(" 未转换的有 "+ newUnRoot.getChildren().size()+" 个 " );
                FileOutputStream unfos = new FileOutputStream(outUnMatchedFile);
                XMLOutputter XMLOut2 = new XMLOutputter(format);// 在元素后换行，每一层元素缩排四格
                XMLOut2.output(newUnDoc, unfos);
                unfos.close();
                System.out.println("转换结束 !");
            } catch (Exception e) {
                System.out.println();
                e.printStackTrace();
            }
        return ;
    }

}
