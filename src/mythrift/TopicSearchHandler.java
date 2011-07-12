package mythrift;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import maui.main.MauiWrapper;

import org.apache.thrift.TException;

import com.mysql.jdbc.log.Log4JLogger;

public class TopicSearchHandler implements TopicSearch.Iface {
  MauiWrapper wrapper;
  String vocabularyName;
  String modelName;
  String dataDirectory;
  String storedRequestDirectory;
  static public Log4JLogger logger = new Log4JLogger(TopicSearchHandler.class.getName());
  static int numrequest = 0;
  
  
  public TopicSearchHandler() {
    vocabularyName = "agrovoc_en";
    modelName = "fao30";
    dataDirectory = ".";
    storedRequestDirectory = "./requests/";
    boolean success = new File(storedRequestDirectory).mkdir();
    if ( success )
      logger.logInfo("Succeed to create hte directory " + storedRequestDirectory);
    
    wrapper = new MauiWrapper(dataDirectory, vocabularyName, modelName);
  }
  
  public void writeToFile(String filePath, String text) throws IOException {
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
    out.write(text);
    out.close();
  }

  @Override
  public List<String> search(String text) throws TException {
    logger.logInfo("received a text");
    List<String> res = new ArrayList<String>();
    String filePath = storedRequestDirectory + System.currentTimeMillis() + "_"  + numrequest;
    numrequest++;
    try {
      writeToFile(filePath, text);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return null;
    }
    //TSerializer serializer = new TSerializer(new TSimpleJSONProtocol.Factory());
    //serializer.toString(base)
    try {
      
      ArrayList<String> keywords = wrapper.extractTopicsFromFile(filePath, 15);
      for (String keyword : keywords) {
        //System.out.println("Keyword: " + keyword);
        res.add(keyword);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return res;
  }

}
