package Com.Server.Membership.TestCase;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

public class TestMustache {
    /**
     * 测试模板
     */
    @Test
    public void testMustache(){
        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("name", "Mustache");
        scopes.put("feature", new Feature("Perfect!"));

        Writer writer = new OutputStreamWriter(System.out);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader("{{name}}, {{feature.description}}!"), "example");
        mustache.execute(writer, scopes);
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class Feature {
        Feature(String description) {
            this.description = description;
        }
        String description;
    }
}
