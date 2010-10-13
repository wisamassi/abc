/**
 *  ServingXML
 *  
 *  Copyright (C) 2006  Daniel Parker
 *    daniel.parker@servingxml.com 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 **/

package com.servingxml.components.flatfile.scanner.bytes;

import java.nio.charset.Charset;

import com.servingxml.util.ByteArrayBuilder;
import com.servingxml.components.flatfile.options.ByteTrimmer;
import com.servingxml.components.flatfile.FlatContentReceiver;

public class RecordEvent {
  RecordEventType eventType = RecordEventType.DATA_EVENT;
  int start = 0;
  int length = 0;

  RecordEvent() {
  }

  void initialize(RecordEventType eventType, int start, int length) {
    this.eventType = eventType;
    this.start = start;
    this.length = length;
  }

  void setDataType(RecordEventType eventType) {
    this.eventType = eventType;
  }

  void write(ByteArrayBuilder builder, Charset charset, FlatContentReceiver receiver) {
    eventType.write(builder.buffer(), start, length, charset, receiver);
  }

  boolean isEmptyLine(ByteArrayBuilder builder, ByteTrimmer byteTrimmer) {
    return eventType.isEmptyLine(builder.buffer(), start, length, byteTrimmer);
  }

  public String toString() {
    return eventType.getClass().getName() + "-" + length;
  }
}




