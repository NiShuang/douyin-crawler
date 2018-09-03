import net.dongliu.requests.Requests;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ciel on 2018/8/27
 */
public class DouyinDataHelper {
    private String aid;
    private String count;
    private String postUrl;
    private String indexUrl;

    private static DouyinDataHelper instance = new DouyinDataHelper();

    public static DouyinDataHelper getInstance() {
        return instance;
    }

    private DouyinDataHelper() {
        aid = "1128";
        count = "21";
        indexUrl = "https://www.douyin.com/share/user/";
        postUrl = "https://www.douyin.com/aweme/v1/aweme/post/";
    }

    public Map<String, String> getUserInfo(String userId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("accept-language", "zh-CN,zh;q=0.9");
        headers.put("cache-control", "no-cache");
        headers.put("upgrade-insecure-requests", "1");
        headers.put("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        String resp = Requests.get(indexUrl + userId).headers(headers).send().readToText();
        Pattern pattern = Pattern.compile("dytk: '(.*?)'");
        Matcher matcher =  pattern.matcher(resp);
        if(!matcher.find()) return null;
        String result = matcher.group();
        String dytk = result.substring(result.indexOf('\'') + 1, result.lastIndexOf('\''));
        pattern = Pattern.compile("class=\"nickname\">(.*?)</p>");
        matcher =  pattern.matcher(resp);
        if(!matcher.find()) return null;
        String result1 = matcher.group();
        String nickname = result1.substring(result1.indexOf('>') + 1, result1.lastIndexOf('<'));
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("dytk", dytk);
        userInfo.put("nickname", nickname);
        return userInfo;
    }

    public String getPost(String userId,String dytk, String maxCursor) {
        String signature = getSignature(userId);
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("accept-language", "zh-CN,zh;q=0.9");
        headers.put("cache-control", "no-cache");
        headers.put("pragma", "no-cache");
        headers.put("x-requested-with", "XMLHttpRequest");
        headers.put("referer", "https://www.douyin.com/share/user/" + userId);
        headers.put("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");

        Map<String, String> paras = new HashMap<>();
        paras.put("user_id", userId);
        paras.put("count", count);
        paras.put("max_cursor", maxCursor);
        paras.put("aid", aid);
        paras.put("_signature", signature);
        paras.put("dytk", dytk);
        String resp = Requests.get(postUrl).params(paras).headers(headers).send().readToText();
        return resp;
    }

    private String getSignature(String userId) {
        String filePath = System.getProperty("user.dir") + "/src/main/js/fuck.js";
        return JsUtil.getInstance().execute(filePath, userId);
    }
}
