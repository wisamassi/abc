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

package com.servingxml.components.recordmapping;

import com.servingxml.components.recordio.RecordAccepter;
import com.servingxml.util.xml.XsltEvaluatorFactory;
import com.servingxml.app.ServiceContext;

/**
 * Implements a factory class for creating new <tt>InverseRecordContent</tt> instances.
 * 
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */                                                             

class OnRecordFactory implements MapXmlFactory {

  private final MapXmlFactory tailFactories;
  private final RecordAccepter accepter;  

  public OnRecordFactory(RecordAccepter accepter, MapXmlFactory tailFactories) {

    this.accepter = accepter;
    this.tailFactories = tailFactories;
  }

  public MapXml createMapXml(ServiceContext context) {

    MapXml children = tailFactories.createMapXml(context);

    return new OnRecord(accepter, children);
  }

  public void addToXsltEvaluator(String mode, XsltEvaluatorFactory recordTemplatesFactory) {
  }

  public boolean isGroup() {
    return false;
  }

  public boolean isRecord() {
    return true;
  }
}                                 
