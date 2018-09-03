import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by ciel on 2018/9/3
 */
public class Main {
    public static void main(String[] args) {
        /*
        userId 不是用户名，也不是抖音ID
        获取方法：
            从抖音app分享个人主页链接到其他平台
            在电脑上用浏览器该链接
            查看浏览器的url，htps://www.amemv.com/share/user/后面的一串数字就是 userId
         */
        String userId = "50227438882";

        Map<String, String> userInfo = DouyinDataHelper.getInstance().getUserInfo(userId);
        String dytk = userInfo.get("dytk");
        String account = userInfo.get("nickname");
        JSONArray postArray = new JSONArray();
        String maxCursor = "0";
        while(true) {
            String resp = DouyinDataHelper.getInstance().getPost(userId, dytk, maxCursor);
            JSONObject jsonObject = JSONObject.parseObject(resp);
            postArray.addAll(jsonObject.getJSONArray("aweme_list"));
            if(jsonObject.containsKey("max_cursor")) {
                maxCursor = jsonObject.getString("max_cursor");
                if("0".equals(maxCursor)) {
                    break;
                }
            }
        }
        System.out.println(postArray.toJSONString());
    }
}
