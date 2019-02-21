import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
        int resultCode=0;
        try {
            Class.forName(Drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/dictionary.db");//连接数据库zhou.db,不存在则创建
            Statement statement = connection.createStatement();   //创建连接对象，是Java的一个操作数据库的重要接口
//            String sql = "create table tables(name varchar(20),pwd varchar(20))";
            String sql = Utils.CREATE_DICTIONARY_SQL;
            resultCode = statement.executeUpdate(sql);            //创建数据库
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
            Statement statement = connection.createStatement();   //创建连接对象，是Java的一个操作数据库的重要接口
//            String sql = "create table tables(name varchar(20),pwd varchar(20))";
            String sql = String.format( Utils.INSERT_DICTIONARY_SQL, languageType, esValue, result );
            System.out.println( "insertText sql:"+sql );
            resultRow = statement.executeUpdate(sql );//搜索数据库，将搜索的放入数据集ResultSet中
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultRow;
    }
}
