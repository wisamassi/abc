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

package com.servingxml.components.common;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;
import com.servingxml.util.Name;                    
import com.servingxml.util.ServingXmlException;
import com.servingxml.util.record.Record;    
import com.servingxml.util.record.Value;
import com.servingxml.util.SystemConstants;    
import com.servingxml.util.xml.XsltEvaluatorFactory;

/**
 *  
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public interface ChildEvaluator {

  String evaluateString(ServiceContext context, Flow flow, Record variables);

  String[] evaluateStringArray(ServiceContext context, Flow flow, Record variables);

  Value evaluateValue(ServiceContext context, Flow flow, Record variables);

  void addToXsltEvaluator(String mode, XsltEvaluatorFactory recordTemplatesFactory);
}

