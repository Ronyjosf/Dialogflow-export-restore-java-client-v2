package com.example.dialogflow;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.cloud.dialogflow.v2beta1.*;

//import com.google.cloud.dialogflow.v2beta1.Agents;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ImportAgent {
    public static void ImportAgent(byte[] agentContent, String projectID) throws IOException {
        System.out.print("args[1] - projectID = " + projectID);
        Object result;
        System.out.print("\nabout to go and import the agent\n ");

        try (AgentsClient agentsClient = AgentsClient.create()) {
            ProjectName parent = ProjectName.of(projectID);
            ImportAgentRequest request = ImportAgentRequest.newBuilder()
//            RestoreAgentRequest request = RestoreAgentRequest.newBuilder()
                    .setParent(parent.toString())
                    .setAgentContent(ByteString.copyFrom(agentContent))
                    .build();
            Empty response = agentsClient.importAgentAsync(request).get();
//            Empty response = agentsClient.restoreAgentAsync(request).get();

            System.out.print("\naFinish with request to import an agent\n ");

        } catch (Exception e1) {
            System.out.print("\nGot Exception:\n");
            e1.printStackTrace();
        }

    }

    public static void ImportAgent(ByteString agentContent, String projectID) throws IOException {
        System.out.print("args[1] - projectID = " + projectID);
        Object result;
        System.out.print("\nabout to go and import the agent\n ");

        try (AgentsClient agentsClient = AgentsClient.create()) {
            ProjectName parent = ProjectName.of(projectID);
            ImportAgentRequest request = ImportAgentRequest.newBuilder()
//            RestoreAgentRequest request = RestoreAgentRequest.newBuilder()
                    .setParent(parent.toString())
                    .setAgentContent(agentContent)
                    .build();
            Empty response = agentsClient.importAgentAsync(request).get();
//            Empty response = agentsClient.restoreAgentAsync(request).get();

            System.out.print("\naFinish with request to import an agent\n ");

        } catch (Exception e1) {
            System.out.print("\nGot Exception:\n");
            e1.printStackTrace();
        }
    }

    public static byte[] loadAndConvertZipToBase64(String fileName) throws IOException {
//        File originalFile = new File("signature.jpg");
        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();

        byte[] base64EncodedData = Base64.encodeBase64(bytes);
        return base64EncodedData;
    }

    public static String getProjectIDFromJson(String filename) throws FileNotFoundException {
        String projectName = null;
        JSONParser parser = new JSONParser();

        try {
            FileReader fileReader = new FileReader("samples/" + filename);
            Object obj = parser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray devAgents = (JSONArray) jsonObject.get("dev");
            JSONArray stagingAgents = (JSONArray) jsonObject.get("staging");
            if (devAgents.size() != stagingAgents.size())
                return null;

            for (int i = 0; i < devAgents.size(); i++) {

            }

            Iterator<String> iterator = devAgents.iterator();
            while (iterator.hasNext())
                System.out.println(devAgents);


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return projectName;
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        System.out.println("mvn exec:java -DImportAgent "
                + "-Dexec.args='--zipFile filename --projectID <projectIDt>'\n");
        System.out.println("\nOr:\n");
        System.out.println("mvn exec:java -DImportAgent -Dexec.args='--getFromJson <agents.json> --fromEnv <dev/staging/prod/demo> --toEnv <dev/staging/prod/demo>''\n");

        String command = null, projectID = null, filename = null;
        String exportFromProjectID = null, importToProjectID = null;
        byte[] encoded64Data = null;
        command = args[0];

        if (command.equals("--zipFile")) {
            filename = args[1];

            command = args[2];
            if (command.equals("--projectID")) {
                projectID = args[3];

            }
            System.out.print("\nfilename is: " + filename);
            System.out.print("\nabout to encode\n");
            encoded64Data = loadAndConvertZipToBase64(filename);

            ImportAgent(encoded64Data, projectID);
        } else if (command.equals("--exportFromProjectID")) {
            exportFromProjectID = args[1];
            command = args[2];
            if (command.equals("--importToProjectID"))
                importToProjectID = args[3];

            ByteString agent_content = null;

            System.out.print("\nexportFromProject is: " + exportFromProjectID + " importToProjectID is " + importToProjectID);
            agent_content = ExportAgent.ExportAgent(exportFromProjectID);
            ImportAgent(agent_content, importToProjectID);

        } else if (command.equals("--getFromJson")) {
            filename = args[1];
            System.out.println("filename is: "+filename);
            String fromEnvName = null;
            String toEnvName = null;

            command = args[2];
            if (command.equals("--fromEnv")) {
                fromEnvName = args[3];
            }
            command = args[4];
            if (command.equals("--toEnv")) {
                toEnvName = args[5];
            }
//            getProjectIDFromJson(filename);

            JSONParser parser = new JSONParser();

            try {
//                FileReader fileReader = new FileReader("samples/" + filename);

                String currentDirectory;
                currentDirectory = System.getProperty("user.dir");
                System.out.println("Current working directory : "+currentDirectory);

                FileReader fileReader = new FileReader(filename);
                Object obj = parser.parse(fileReader);
                JSONObject jsonObject = (JSONObject) obj;
//                --fromEnv dev --toEnv staging
                JSONArray fromEnv = (JSONArray) jsonObject.get(fromEnvName);
                JSONArray toEnv = (JSONArray) jsonObject.get(toEnvName);

                if (toEnv.size() != fromEnv.size()) {
                    System.out.println("Json doesn't match in size for all enviroments. Break");
                    return;
                }

                for (int i = 0; i < fromEnv.size(); i++) {
                    ByteString agent_content = null;
                    exportFromProjectID = fromEnv.get(i).toString();
                    importToProjectID = toEnv.get(i).toString();

                    System.out.print("\nexportFromProject is: " + exportFromProjectID + " importToProjectID is " + importToProjectID);
                    agent_content = ExportAgent.ExportAgent(exportFromProjectID);
                    ImportAgent(agent_content, importToProjectID);
                }


            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
