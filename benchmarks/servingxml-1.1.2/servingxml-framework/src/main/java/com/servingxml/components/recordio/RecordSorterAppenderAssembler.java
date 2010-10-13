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

package com.servingxml.components.recordio;

import java.util.Comparator;

import com.servingxml.app.ParameterDescriptor;
import com.servingxml.ioc.components.ConfigurationContext;

/**
 *
 * 
 * @author  Daniel A. Parker
 */

public class RecordSorterAppenderAssembler {

  private ParameterDescriptor[] parameterDescriptors = ParameterDescriptor.EMPTY_ARRAY;
  private SortOn[] sortOnFields;

  public void injectComponent(ParameterDescriptor[] parameterDescriptors) {

    this.parameterDescriptors = parameterDescriptors;
  }

  public void injectComponent(SortOn[] sortOnFields) {
    this.sortOnFields = sortOnFields;
  }

  public RecordFilterAppender assemble(ConfigurationContext context) {

    Comparator comparator = new SortOnComparator(sortOnFields);
    RecordFilterAppender recordFilterAppender = new RecordSorterAppender(comparator);
    if (parameterDescriptors.length > 0) {
      recordFilterAppender = new RecordFilterAppenderPrefilter(recordFilterAppender,parameterDescriptors);
    }

    return recordFilterAppender;

  }
}

