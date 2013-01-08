package test.lenovo.framework.KnowledgeBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-11 下午3:03:23
 * @version : V1.0
 */
public class TestPattern {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String strHTML = "<li><a \"href=http://www.abcxyz.com/something/article/143.htm\" title=\"FCKEditor高亮代码插件测试\"><span class=\"article-date\">[09/11]</span>FCKEditor高亮代码插件测试</a></li>";
        String pattern = "http://([^\\s]+)\".+?span.+?\\[(.+?)\\].+?>(.+?)<";

        Pattern hrefPattern = Pattern.compile(pattern, Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE);
        Matcher m = hrefPattern.matcher(strHTML);
        if (m.groupCount() >= 1) {
            while (m.find()) {
                String query0 = m.group(0);
                System.out.println("query0\t" + query0);
                String query1 = m.group(1);
                System.out.println("query1\t" + query1);
                String query2 = m.group(2);
                System.out.println("query2\t" + query2);
            }

        }

    }
}
