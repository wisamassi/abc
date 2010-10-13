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

package com.servingxml.components.inverserecordmapping;

import com.servingxml.app.ServiceContext;
import com.servingxml.util.Name;
import com.servingxml.app.Flow;

/**                                 
 * This class supports mapping a stream of SAX events to a record stream.
 * 
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public interface ShredXmlFactory {
  public static final ShredXmlFactory[] EMPTY_ARRAY = new ShredXmlFactory[0];

  ShredXml createShredXml(ServiceContext context, Flow flow);

  SubtreeFieldMap createSubtreeFieldMap(Name fieldName, String matchExpr);
}

