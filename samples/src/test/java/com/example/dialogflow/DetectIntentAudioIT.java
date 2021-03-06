///*
//  Copyright 2017, Google, Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//*/
//
//package com.example.dialogflow;
//
//import static com.google.common.truth.Truth.assertThat;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Integration (system) tests for {@link DetectIntentAudio}.
// */
//@RunWith(JUnit4.class)
//@SuppressWarnings("checkstyle:abbreviationaswordinname")
//public class DetectIntentAudioIT {
//
//  private ByteArrayOutputStream bout;
//  private PrintStream out;
//  private DetectIntentAudio detectIntentAudio;
//  private static String PROJECT_ID = System.getenv().get("GOOGLE_CLOUD_PROJECT");
//  private static String SESSION_ID = "fake_session_for_testing";
//  private static String LANGUAGE_CODE = "en-US";
//  private static List<String> AUDIOS = Arrays.asList(
//      "resources/book_a_room.wav",
//      "resources/mountain_view.wav",
//      "resources/today.wav");
//
//  @Before
//  public void setUp() {
//    bout = new ByteArrayOutputStream();
//    out = new PrintStream(bout);
//    System.setOut(out);
//    detectIntentAudio = new DetectIntentAudio();
//  }
//
//  @After
//  public void tearDown() {
//    System.setOut(null);
//  }
//
//  @Test
//  public void testDetectIntent() throws Exception {
//    for (String audioFilePath : AUDIOS) {
//      detectIntentAudio.detectIntentAudio(PROJECT_ID, audioFilePath, SESSION_ID, LANGUAGE_CODE);
//    }
//
//    String got = bout.toString();
//    assertThat(got).contains("Fulfillment Text: 'What time will the meeting start?'");
//  }
//
//}
