package Com.Server.Membership.Api;

import Com.Server.Base.BaseWork;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MemberPo {
    /**
     * 获取成员信息
     *
     * @param userid
     * @return
     */
    public Response getUserInfo(String userid) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        return BaseApi.getInstance().sendRequest("getUserInfo", params);

        // 没有数据驱动代码
       /*
       return given().queryParam("access_token", BaseWork.getInstance().getToken())
                .queryParam("userid", userid)
                .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then().log().all()
                .body("errcode", equalTo(0)).extract().response();
        */


    }

    /**
     * 获取部门成员列表
     *
     * @param department_id
     * @return
     */
    public Response getDepartmentMember(String department_id) {
        return given().queryParam("access_token", BaseWork.getInstance().getToken())
                .queryParam("department_id", department_id)
                .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist")
                .then().log().all()
                .body("errcode", equalTo(0)).extract().response();
    }

    /**
     * 批量删除部门成员
     *
     * @param map
     * @return
     */
    public Response deleteMembers(HashMap<String, Object> map) {
        return given().queryParam("access_token", BaseWork.getInstance().getToken())
                .contentType(ContentType.JSON)
                .body(map)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete")
                .then().log().all()
                .body("errcode", equalTo(0)).extract().response();
    }

    /**
     * 简单创建成员
     *
     * @param map
     * @return
     */
    public Response createMember(Object map) {
        System.out.println("map :-------" + map.toString());
        return BaseApi.getInstance().sendRequest("createMember", (HashMap<String, Object>) map);
//        return given().queryParam("access_token", BaseWork.getInstance().getToken())
//                .contentType(ContentType.JSON)
//                .body(map)
//                .when().log().all()
//                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
//                .then().log().all()
//                .body("errcode", equalTo(0)).extract().response();
    }

    /**
     * 更新用户信息
     *
     * @param map
     * @return
     */
    public Response updateMember(Object map) {
        System.out.println("map :-------" + map.toString());
        return given().queryParam("access_token", BaseWork.getInstance().getToken())
                .contentType(ContentType.JSON)
                .body(map)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/update")
                .then().log().all()
                .body("errcode", equalTo(0)).extract().response();
    }

}
