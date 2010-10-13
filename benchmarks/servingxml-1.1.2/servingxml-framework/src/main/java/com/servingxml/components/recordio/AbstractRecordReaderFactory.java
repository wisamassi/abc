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

import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;

/**
 * A <code>FlatFileReaderFactory</code> instance may be used to obtain objects that
 * implement the <code>RecordReader</code> interface.
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public abstract class AbstractRecordReaderFactory  
implements RecordReaderFactory, RecordFilterAppender {     

  protected abstract RecordReader createRecordReader(ServiceContext context, Flow flow);
  
  public RecordPipeline createRecordPipeline(ServiceContext context, Flow flow) {

    RecordReader recordReader = createRecordReader(context, flow);
    return new RecordPipeline(flow, recordReader);
  }

  public void appendToRecordPipeline(ServiceContext context, Flow flow,
    RecordFilterChain pipeline) {

    RecordReader recordReader = createRecordReader(context, flow);
    pipeline.addRecordFilter(new RecordReaderFilterAdaptor(recordReader));
  }
}
