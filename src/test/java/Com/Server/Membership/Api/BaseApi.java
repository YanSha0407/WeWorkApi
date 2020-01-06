package Com.Server.Membership.Api;

import Com.Server.Membership.Model.BaseApiModel;
import Com.Server.Membership.Model.BaseMethodModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;

public class BaseApi {
    private static BaseApi baseApi;
    private BaseApiModel baseModel;
    private BaseMethodModel methodModel;
    public static BaseApi getInstance(){
        if (baseApi == null){
             baseApi = new BaseApi();
        }
        return baseApi;
    }
    //  解析yaml文件
    public void resolveYaml(String path){
        String path1 = this.getClass().getResource(path).getPath();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // yaml 解析完成后 类型为自定义 ApiObjectModel
        try {
            baseModel = mapper.readValue(
                    BaseApi.class.getResourceAsStream(path), BaseApiModel.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Response sendRequest(String method, HashMap<String, Object> params ){
            // 获取方法对应的步骤数据
            resolveYaml("/MemberModel.yaml");
            methodModel = baseModel.methodModel.get(method);
            return methodModel.run(params);
    }
    public Response sendRequest(String method, Object params){
        // 获取方法对应的步骤数据
        resolveYaml("/MemberModel.yaml");
        methodModel = baseModel.methodModel.get(method);
        return methodModel.run(params);
    }
}
