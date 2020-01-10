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
      testMap.put("noarraylist", "classDoesNotUseArrayList");
      testMap.put("nopackage", "classDoesNotUsePackages");
      testMap.put("comptests", "comparisonTests");
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
      pw.println("\tpublic static void main(String[] args) throws Exception {");
      pw.println("\t\tAutograder gr = new Autograder();");
      if (obj.containsKey("tests")) {
         JSONArray arr = (JSONArray) obj.get("tests");
         addTest(pw, arr, testMap, visibilMap, conditional);
      } else {
         throw new Exception("Missing base tests tag.");
      }
      pw.println("gr.testRunFinished();");
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
         String parameters = getParams(type, params);
         
         String test = "gr." + method + "(" + parameters +")";
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

   public static String getParams(String type, JSONObject params) throws Exception {
      String file, name, log, sample, input, typeName;
      Boolean field;
      Number count, points, visCount;
      switch(type) {
        case "noarraylist":
        case "nopackage":
        case "fileexists":
        case "junittests":
        case "compiles":
        case "nopiv":
        case "checkstylepf":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           file = (String) params.get("classname");
           return "\"" + file + "\"";
        case "constructcount":
        case "methodcount":
        case "comptests":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("count")) {
              throw new Exception("Test of type " + type + " does not have parameter count");
           }
           file = (String) params.get("classname");
           count = (Number) params.get("count");
           return "\"" + file + "\", " + count;
        case "compfiles":
           if (!params.containsKey("first")) {
              throw new Exception("Test of type " + type + " does not have parameter first");
           }
           if (!params.containsKey("second")) {
              throw new Exception("Test of type " + type + " does not have parameter second");
           }
           String first = (String) params.get("first");
           String second = (String) params.get("second");
           return "\"" + first + "\", \"" + second + "\"";
        case "hasfield":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("classtype")) {
              throw new Exception("Test of type " + type + " does not have parameter classtype");
           }
           if (!params.containsKey("field")) {
              throw new Exception("Test of type " + type + " does not have parameter field");
           }
           name = (String) params.get("classname");
           typeName = (String) params.get("classtype");
           String fieldName = (String) params.get("field");
           return "\"" + name + "\", \"" + fieldName + "\", \"" + typeName +"\"";
        case "hasmethod":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("method")) {
              throw new Exception("Test of type " + type + " does not have parameter method");
           }
           if (!params.containsKey("arguments")) {
              throw new Exception("Test of type " + type + " does not have parameter arguments");
           }
           name = (String) params.get("classname");
           String method = (String) params.get("method");
           JSONArray arguments = (JSONArray) params.get("arguments");
           return "\"" + name + "\", \"" + method + "\", \"" + arguments.toJSONString() +"\"";

        case "logdifftest":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("logname")) {
              throw new Exception("Test of type " + type + " does not have parameter logname");
           }
           if (!params.containsKey("samplename")) {
              throw new Exception("Test of type " + type + " does not have parameter samplename");
           }
           if (!params.containsKey("inputname")) {
              throw new Exception("Test of type " + type + " does not have parameter inputname");
           }
           name = (String) params.get("classname");
           log = (String) params.get("logname");
           sample = (String) params.get("samplename");
           input = (String) params.get("inputname");
           return "\"" + name + "\", \"" + log + "\", \"" + sample + "\", \"" + input +"\"";
        case "logdifftests":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("logname")) {
              throw new Exception("Test of type " + type + " does not have parameter logname");
           }
           if (!params.containsKey("samplename")) {
              throw new Exception("Test of type " + type + " does not have parameter samplename");
           }
           if (!params.containsKey("count")) {
              throw new Exception("Test of type " + type + " does not have parameter count");
           }
           name = (String) params.get("classname");
           log = (String) params.get("logname");
           sample = (String) params.get("samplename");
           count = (Number) params.get("count");
           visCount = 0;
           if (params.containsKey("viscount")) {
              visCount = (Number) params.get("viscount");
           }
           return "\"" + name + "\", " + count.intValue() + ", " + visCount.intValue() +", \"" + log + "\", \"" + sample + "\"";
        case "stddifftest":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("count")) {
              throw new Exception("Test of type " + type + " does not have parameter count");
           }
           if (!params.containsKey("sample")) {
              throw new Exception("Test of type " + type + " does not have parameter sample");
           }
           name = (String) params.get("classname");
           count = (Number) params.get("count");
           field = (Boolean) params.get("sample");
           return  "\"" + name + "\", " + count.intValue() + ", " + field;
        case "stddifftests":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("count")) {
              throw new Exception("Test of type " + type + " does not have parameter count");
           }
           if (!params.containsKey("sample")) {
              throw new Exception("Test of type " + type + " does not have parameter sample");
           }
           name = (String) params.get("classname");
           count = (Number) params.get("count");
           field = (Boolean) params.get("sample");
           String line =  "\"" + name + "\", " + count.intValue() + ", " + field;
           if (params.containsKey("viscount")) {
              visCount = (Number) params.get("viscount");
              line +=  ", " + visCount.intValue();
           }
           return line;
        case "checkstylepererr":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("pointper")) {
              throw new Exception("Test of type " + type + " does not have parameter pointper");
           }
           file = (String) params.get("classname");
           points = (Number) params.get("pointper");
           return "\"" + file + "\", " + points + ", false";
        case "checkstylepertype":
           if (!params.containsKey("classname")) {
              throw new Exception("Test of type " + type + " does not have parameter classname");
           }
           if (!params.containsKey("pointper")) {
              throw new Exception("Test of type " + type + " does not have parameter pointper");
           }
           file = (String) params.get("classname");
           points = (Number) params.get("pointper");
           return "\"" + file + "\", " + points + ", false";
      }
      return "";
   }
}
