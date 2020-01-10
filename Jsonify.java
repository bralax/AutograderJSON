import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;
//import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class Jsonify {

   public static void main(String args[]) throws Exception {
      HashMap<String, String> testMap = new HashMap<>();
      testMap.put("noarraylist", "classDoesNotUseArrayLists");
      testMap.put("nopackage", "classDoesNotUsePackages");
      testMap.put("comptests", "comparisonTests");
      testMap.put("comptest","compTest");
      testMap.put("compfiles","diffFiles");
      testMap.put("hasfield","hasFieldTest");
      testMap.put("hasmethod","hasMethodTest");
      testMap.put("junittests","junitTests");
      testMap.put("logdifftest","logFileDiffTest");
      testMap.put("logdifftests","logFileDiffTests");
      testMap.put("stddifftests","stdOutDiffTest");
      testMap.put("stddifftests","stdOutDiffTests");
      testMap.put("checkstylepf","testCheckstyle");
      testMap.put("compiles","testCompiles");
      testMap.put("constructcount","testConstructorCount");
      testMap.put("methodcount","testMethodCount");
      testMap.put("nopiv","testPublicInstanceVariables");
      testMap.put("fileexists","testSourceExists");
      testMap.put("checkstylepererr","testSortedCheckstyle");
      testMap.put("checkstylepertype","testSortedCheckstyle");
      HashMap<String, Integer> visibilMap = new HashMap<>();
      visibilMap.put("visible", 0);
      visibilMap.put("hidden", 1);
      visibilMap.put("after_due_date", 2);
      visibilMap.put("after_published", 3);
      HashSet<String> conditional = new HashSet<>();
      conditional.add("noarraylist");
      conditional.add("nopackage");
      conditional.add("hasfield");
      conditional.add("compiles");
      conditional.add("constructcount");
      conditional.add("methodcount");
      conditional.add("fileexists");
      JSONObject obj = (JSONObject)(new JSONParser().parse(new FileReader("JSONExample.json")));
      PrintWriter pw = new PrintWriter(new FileWriter("Main.java"));
      pw.println("public class Main {\n");
      pw.println("\tpublic static void main(String[] args) {");
      pw.println("\t\tAutograder gr = new Autograder();");
      if (obj.containsKey("tests")) {
         JSONArray arr = (JSONArray) obj.get("tests");
         addTest(pw, arr, testMap, visibilMap, conditional);
      } else {
         throw new Exception("Missing base tests tag.");
      }
      pw.println("\t}\n}");
      pw.flush();
      pw.close();
   }


   public static void addTest(PrintWriter pw, JSONArray arr, HashMap<String, String> testMap, HashMap<String, Integer> visMap, HashSet<String> condition) throws Exception{
      for (Object ob: arr) {
         JSONObject obj = (JSONObject) ob;
         if (!obj.containsKey("type") || !obj.containsKey("parameters") || !obj.containsKey("score") || !obj.containsKey("visibility")) {
            throw new Exception("Missing one of the mandatory parameters in the following test" + obj.toJSONString());
         }
         String type = (String) obj.get("type");
         JSONObject params = (JSONObject) obj.get("parameters");
         Number score = (Number) obj.get("score");
         Integer visibility = visMap.get(((String)obj.get("visibility")));
         if (!testMap.containsKey(type)) {
            throw new Exception("There does not exist a test for the type: " + type);
         }
         String method = testMap.get(type);
         String test = "gr." + method + "()";
         pw.printf("gr.setScore(%f);\n", score.doubleValue());
         pw.printf("gr.setVisibility(%d);\n", visibility);
         if (condition.contains(type) && obj.containsKey("conditiontests") && obj.get("conditiontests") != null) {
            pw.println("if(" + test + ") {");
            JSONArray newArr = (JSONArray) obj.get("conditiontests");
            addTest(pw, newArr, testMap, visMap, condition);
            pw.println("}");
         } else {
            pw.printf("%s;\n", test);
         }
      }
   }
}
