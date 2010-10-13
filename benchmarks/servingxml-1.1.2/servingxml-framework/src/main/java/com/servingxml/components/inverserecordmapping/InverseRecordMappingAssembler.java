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

import com.servingxml.app.ParameterDescriptor;
import com.servingxml.util.MessageFormatter;
import com.servingxml.util.ServingXmlMessages;
import com.servingxml.util.ServingXmlException;
import com.servingxml.ioc.components.ConfigurationContext;

/**
 * The <code>InverseRecordMappingAssembler</code> implements an assembler for
 * assembling <code>InverseRecordMapping</code> objects.
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class InverseRecordMappingAssembler {

  private ParameterDescriptor[] parameterDescriptors = ParameterDescriptor.EMPTY_ARRAY;
  private ShredXmlFactory[] flattenerFactories = ShredXmlFactory.EMPTY_ARRAY;

  public void injectComponent(ParameterDescriptor[] parameterDescriptors) {

    this.parameterDescriptors = parameterDescriptors;
  }

  public void injectComponent(ShredXmlFactory[] flattenerFactories) {
    this.flattenerFactories = flattenerFactories;
  }
                                                       
  public InverseRecordMapping assemble(ConfigurationContext context) {

    if (flattenerFactories.length == 0) {
      String message = MessageFormatter.getInstance().getMessage(ServingXmlMessages.COMPONENT_ELEMENT_REQUIRED,
        context.getElement().getTagName(),"sx:onSubtree");
      throw new ServingXmlException(message);
    }
    
    InverseRecordMapping inverseRecordMapping = new InverseRecordMappingImpl(
      flattenerFactories);

    if (parameterDescriptors.length > 0) {
      inverseRecordMapping = new InverseRecordMappingPrefilter(inverseRecordMapping,parameterDescriptors);
    }

    return inverseRecordMapping;
  }
}


