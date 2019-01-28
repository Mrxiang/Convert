import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

//value="zh-CHS"	中文(简体)
//value="zh-CHT"
//value="sr-Latn    塞尔维亚语(拉丁语)
//value="en"		英语
//value="cs"        捷克语
//http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=AFC76A66CF4F434ED080D245C30CF1E71C22959C&from=zh-CHS&to=sr-Latn&text=%E4%BD%A0%E5%A5%BD
public class Translator {

    private static final String REQUEST_PATH = "http://localhost/server_url.php";
    private static final String BOUNDARY = "20140501";
    public void  translate( ){
        try {
            URL url = new URL(REQUEST_PATH);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(3000); // 设置发起连接的等待时间，3s
            httpConn.setReadTimeout(30000); // 设置数据读取超时的时间，30s
            httpConn.setUseCaches(false); // 设置不使用缓存
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream os = httpConn.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);

            String content = "--" + BOUNDARY + "\r\n";
            content += "Content-Disposition: form-data; name=\"title\"" + "\r\n\r\n";
            content += "我是post数据的值";
            content += "\r\n--" + BOUNDARY + "\r\n";
            content += "Content-Disposition: form-data; name=\"cover_img\"; filename=\"avatar.jpg\"\r\n";
            content += "Content-Type: image/jpeg\r\n\r\n";
            bos.write(content.getBytes());

            // 开始写出文件的二进制数据
            FileInputStream fin = new FileInputStream(new File("avatar.jpg"));
            BufferedInputStream bfi = new BufferedInputStream(fin);
            byte[] buffer = new byte[4096];
            int bytes = bfi.read(buffer, 0, buffer.length);
            while (bytes != -1) {
                bos.write(buffer, 0, bytes);
                bytes = bfi.read(buffer, 0, buffer.length);
            }
            bfi.close();
            fin.close();
            bos.write(("\r\n--" + BOUNDARY).getBytes());
            bos.flush();
            bos.close();
            os.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            String res = strBuf.toString();
            System.out.println(res);
            reader.close();
            httpConn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
