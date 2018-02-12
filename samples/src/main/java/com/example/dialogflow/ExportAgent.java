package com.example.dialogflow;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.cloud.dialogflow.v2beta1.*;
import com.google.protobuf.ByteString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ExportAgent {
    public static ByteString ExportAgent(String projectID) throws IOException, ExecutionException, InterruptedException {
        System.out.print("args[1] - projectID = " + projectID);
//        Object result;

        ExportAgentResponse response = null;
        try (AgentsClient agentsClient = AgentsClient.create()) {
//            String parent = projectID;
            ProjectName parent = ProjectName.of(projectID);

            response = agentsClient.exportAgentAsync(parent).get();

            System.out.print("\nresults:\n " + response.toString());
            return response.getAgentContent();

        } catch (Exception e) {
            System.out.print("\nGot Exception:\n");
            e.printStackTrace();
        }
        if (response.getAgentContent()!=null)
            return response.getAgentContent();
        else
            return null;
    }



    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        System.out.println("mvn exec:java -DExportAgent "
                + "-Dexec.args='--projectID <projectID>'\n");

        String command = null, projectID = null, filename = null;
        byte[] encoded64Data = null;

        command = args[0];
        if (command.equals("--projectID"))
        {
            projectID = args[1];
        }


        // "agentContent": "d7116893-2e06-4faf-8d52-738b87ba8fe8"
        // agentName = "videoplanstaging"

        ExportAgent(projectID);
//        ImportAgent("d7116893-2e06-4faf-8d52-738b87ba8fe8", "videoplanstaging");
    }
}

