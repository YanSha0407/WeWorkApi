package Com.Server.Membership.Model;

import Com.Server.Base.BaseWork;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseMethodModel {
    private HashMap<String, Object> params; // 用户传入的参数
    public HashMap<String, Object> query; // yaml文件中自动映射的query参数
    public HashMap<String, Object> header;
    public HashMap<String, Object> postBody; // postBody是post传的参数体  如果是post传参 参数传在body体中
    public String method;
    public String url;


    /**
     * 非模板 传参
     * @param params
     * @return
     */
    public Response run(HashMap<String, Object> params) {
        this.params = params;
        RequestSpecification request = given();
        request.queryParam("access_token", BaseWork.getInstance().getToken());
        if (query != null) {
            query.entrySet().forEach(entry -> {
                request.queryParam(entry.getKey(), replaceValue(entry.getValue().toString()));
            });
        }
        if (header != null) {
            header.entrySet().forEach(entry -> {
                request.header(entry.getKey(), replaceValue(entry.getValue().toString()));
            });
        }
        if (postBody != null) {
            request.contentType(ContentType.JSON);
            HashMap<String, Object> map = new HashMap<>();
            ArrayList<String> list = new ArrayList<>();
            postBody.entrySet().forEach(entry -> {
                // 如果是部门列表 部门列表为一个ArrayList 不能简单做值替换 需要替换成一个数组
                // 示例 ：正常参数：Body:
                   /* {
                        "name": "燕莎15782964109",
                        "mobile": "15782964109",
                        "department": [
                            "3",
                            "1"
                        ],
                        "userid": "78296410964"
                    }
                    */
                //仅做值替换的body参数体（错误参数）
                  /*  Body:
                    {
                        "name": "燕莎15782964588",
                            "mobile": "15782964588",
                            "department": "[3, 1]",
                            "userid": "78296458894"
                    }

                   */
                map.put(entry.getKey(), replaceValue(entry.getValue().toString()));
                if (map.containsKey("department")){
                    map.remove("department");
                    map.put("department", params.get("department"));
                }
                if (map.containsKey("useridlist")){
                    map.remove("useridlist");
                    map.put("useridlist", params.get("useridlist"));
                }
            });
            System.out.println("map" + map);
            request.body(map);
        }
        return request
                .when().log().all()
                .request(method, url)
                .then().log().all()
                .extract().response();
    }

    /**
     * 模板克隆使用此方法
     * @param params
     * @return
     */
    public Response run(Object params) {
        RequestSpecification request = given();
        request.queryParam("access_token", BaseWork.getInstance().getToken());
        if (query != null) {
            query.entrySet().forEach(entry -> {
                request.queryParam(entry.getKey(), replaceValue(entry.getValue().toString()));
            });
        }
        if (header != null) {
            header.entrySet().forEach(entry -> {
                request.header(entry.getKey(), replaceValue(entry.getValue().toString()));
            });
        }
        request.contentType(ContentType.JSON);
        request.body(params);
        return request
                .when().log().all()
                .request(method, url)
                .then().log().all()
                .extract().response();
    }

    /**
     * 替换yaml文件中的参数
     * @param yamlParam
     * @return
     */
    public String replaceValue(String yamlParam) {
        // 循环替换yaml文件中需要传入的参数
        for (Map.Entry<String, Object> kv : params.entrySet()) {
            // 拼接用户传入的参数
            String mather = "${" + kv.getKey() + "}";
            // 判断yaml文件中是否包含用户传入的参数
            // 如果包含 则用用户传入的参数 去替换yaml文件中原来的值。
            if (yamlParam.contains(mather)) {
                yamlParam = yamlParam.replace(mather, kv.getValue().toString());
            }
        }
        return yamlParam;
    }
}
