package operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadObject {
	public Properties getObjectRepository() throws IOException{
	Properties p = new Properties();
	File file = new File(System.getProperty("user.dir")+"\\src\\objects.properties");
    FileInputStream inputstream = new FileInputStream(file);
    p.load(inputstream);
    return p;
}
}