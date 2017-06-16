
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Generator {

	public void generator() throws Exception{

		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		//指定 逆向工程配置文件

        URL resource = Generator.class.getResource("generatorConfig.xml");
        File configFile = new File(resource.getPath());
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);
		myBatisGenerator.generate(null);

	} 
	public static void main(String[] args) throws Exception {
		try {
			Generator generatorSqlmap = new Generator();
			generatorSqlmap.generator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
