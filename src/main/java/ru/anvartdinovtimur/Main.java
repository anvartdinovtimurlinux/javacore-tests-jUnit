package ru.anvartdinovtimur;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileCSVName = "dataCorrect.csv";
        String fileJSONName = "data.json";
        String fileXMLName = "data.xml";

        List<Employee> employees = parseCSV(columnMapping, fileCSVName);
        String json = listToJson(employees);
        writeString(fileJSONName, json);

        fileJSONName = "data2.json";
        employees = parseXML(fileXMLName);
        json = listToJson(employees);
        writeString(fileJSONName, json);

        json = readString("data.json");
        List<Employee> listJSON = jsonToList(json);
        listJSON.forEach(System.out::println);

        json = readString("data2.json");
        listJSON = jsonToList(json);
        listJSON.forEach(System.out::println);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employees = new ArrayList<>();
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            employees = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            NodeList nodeEmployeesList = root.getChildNodes();

            for (int i = 0; i < nodeEmployeesList.getLength(); i++) {
                Node nodeEmployee = nodeEmployeesList.item(i);
                if (nodeEmployee.getNodeType() != Node.TEXT_NODE) {
                    Employee employee = new Employee();
                    NodeList employeeProperties = nodeEmployee.getChildNodes();

                    for (int j = 0; j < employeeProperties.getLength(); j++) {
                        Node nodeProperty = employeeProperties.item(j);
                        String property = nodeProperty.getTextContent();
                        switch (nodeProperty.getNodeName()) {
                            case "id":
                                employee.setId(Integer.parseInt(property));
                                break;
                            case "firstName":
                                employee.setFirstName(property);
                                break;
                            case "lastName":
                                employee.setLastName(property);
                                break;
                            case "country":
                                employee.setCountry(property);
                                break;
                            case "age":
                                employee.setAge(Integer.parseInt(property));
                                break;
                        }
                    }
                    employees.add(employee);
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static String listToJson(List<Employee> employees) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(employees, listType);
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JSONParser parser = new JSONParser();

        try {
            JSONArray array = (JSONArray) parser.parse(json);
            for (int i = 0; i < array.toArray().length; i++) {
                JSONObject s = (JSONObject) array.get(i);
                Employee employee = gson.fromJson(s.toJSONString(), Employee.class);
                employees.add(employee);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static void writeString(String fileName, String data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String fileName) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
