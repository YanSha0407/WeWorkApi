package Com.Server.Membership.TestCase;

import Com.Server.Membership.Api.MemberPo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

public class MemberTest {
    public static MemberPo user;
    public static String createUserID;

    @BeforeClass
    public static void beforeClass() {
        user = new MemberPo();
    }

    /**
     * 获取成员资料
     */
    @Test
    public void getUserInfo() {
        user.getUserInfo("78205830250");
    }

    /**
     * 获取部门成员
     */
    @Test
    public void getDepartmentMember() {
        user.getDepartmentMember("3");
    }

    /**
     * 批量删除成员
     */
    @Test
    public void deleteMembers() {
        ArrayList<String> list = new ArrayList<>();
        list.add("13001277999");
        list.add("13001277888");
        HashMap<String, Object> map = new HashMap<>();
        map.put("useridlist", list);
        user.deleteMembers(map);
    }

    /**
     * 简单创建成员
     */
    @Test
    public void createMember() {
        String name = "燕莎" + String.valueOf(System.currentTimeMillis()).substring(0, 11);
        String userID = String.valueOf(System.currentTimeMillis())
                .substring(
                        String.valueOf(System.currentTimeMillis()).length() - 11
                );
        String mobile = String.valueOf(System.currentTimeMillis()).substring(0, 11);
        ArrayList<String> list = new ArrayList<>();
        list.add("3");
        list.add("1");
        HashMap<String, Object> map = new HashMap<>();
        map.put("department", list);
        map.put("userid", userID);
        map.put("name", name);
        map.put("mobile", mobile);
        user.createMember(map).then().body("errcode", equalTo(0));
        createUserID = user.getUserInfo(userID).then().body("name",equalTo(name)).extract().path("userid");;
    }

    /**
     * 使用模板创建成员
     */
    @Test
    public void cloneMember() {
        String name = "燕莎" + String.valueOf(System.currentTimeMillis()).substring(0, 11);
        String userID = String.valueOf(System.currentTimeMillis())
                .substring(
                        String.valueOf(System.currentTimeMillis()).length() - 11
                );
        String mobile = String.valueOf(System.currentTimeMillis()).substring(0, 11);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        HashMap<String, Object> map = new HashMap<>();
        map.put("department", list);
        map.put("userid", userID);
        map.put("name", name);
        map.put("mobile", mobile);
        user.createMember(
                UserClone.template("/Member.json",map)
        ).then().body("errcode", equalTo(0));
        user.getUserInfo(userID).then().body("name",equalTo(name));
    }

    /**
     * 更新成员
     */
    @Test(dependsOnMethods = "createMember")
    public void updateMember(){
        String name = "燕莎" + String.valueOf(System.currentTimeMillis()).substring(0, 11);
        String userID = createUserID;
        String mobile = String.valueOf(System.currentTimeMillis()).substring(0, 11);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", userID);
        map.put("name", name);
        map.put("mobile", mobile);
        user.updateMember(map).then().body("errcode", equalTo(0));;
        user.getUserInfo(userID).then().body("name",equalTo(name));
    }
    /**
     * 删除成员
     */
    @Test(dependsOnMethods = "createMember")
    public void deleteUser(){
        String userID = createUserID;
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", userID);
        System.out.println("params ---" + params);
        user.deleteUser(params).then().body("errcode", equalTo(0));
        user.getUserInfo(userID).then().body("errcode",equalTo(60111)); // 用户不存在
    }

}

