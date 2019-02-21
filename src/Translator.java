import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//value="zh-CHS"	中文(简体)
//value="zh-CHT"
//value="sr-Latn    塞尔维亚语(拉丁语)
//value="en"		英语
//value="cs"        捷克语
//http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=AFC76A66CF4F434ED080D245C30CF1E71C22959C&from=zh-CHS&to=sr-Latn&text=%E4%BD%A0%E5%A5%BD
public class Translator {

    private static final String REQUEST_PATH = "http://localhost/server_url.php";
    private static final String BOUNDARY = "20140501";

    public  static String str;
    public static String translateWithBING(String from, String to,String text) {
        System.out.println( " 使用BING 翻译:"+from +" : "+ to+" : "+ text );
        String  translateResult=null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
//                http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=AFC76A66CF4F434ED080D245C30CF1E71C22959C&from=%s&to=%s&text=%s
                str = Utils.BING_TRAMSLATOR_BEGIN +from+"&to="+to+"&text="+ text;
                System.out.println( str );
                HttpGet httpGet = new HttpGet(str );
//                HttpPost httpPost = new HttpPost(str );

                client = HttpClients.createDefault();
//                response = client.execute(httpPost);
                response = client.execute(httpGet);
                System.out.println( " status code: "+ response.getStatusLine().getStatusCode()  );
                if(response != null && response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    System.out.println("Result:");
                    System.out.println(result);
                    System.out.println("End!");
                    InputStream in = new ByteArrayInputStream(result.getBytes());
                    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                    SAXBuilder saxBuilder = new SAXBuilder();

                    Document document = saxBuilder.build(isr);
                    // 4.通过document对象获取xml文件的根节点
                    Element rootElement = document.getRootElement();
                    // 5.获取根节点下的子节点的List集合
                    List<Element> rowList = rootElement.getChildren();
                    // 继续进行解析

                    if( rowList.size() == 0 ){
                        translateResult = rootElement.getText();

                        System.out.println( translateResult );
                    }else{
                        for (Element row : rowList) {
                            System.out.println(row.toString());
                        }
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translateResult;
    }

    public static boolean translateWithDictionary(String text, SDictionary dictionary ){

//        dictionary.selectText( );
        return false;
    }

}
