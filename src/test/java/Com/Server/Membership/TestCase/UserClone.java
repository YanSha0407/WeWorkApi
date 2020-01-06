package Com.Server.Membership.TestCase;

import com.github.mustachejava.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

public class UserClone {
    public static String template(String path, HashMap<String,Object> map){
        System.out.println("path --------" + UserClone.class.getResource(path).getPath());
        Writer writer = new OutputStreamWriter(System.out);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(UserClone.class.getResource(path).getPath());
        mustache.execute(writer, map);
        try {
            writer.flush();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
