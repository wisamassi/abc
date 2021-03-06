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

package com.servingxml.components.transform;

import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;
import com.servingxml.io.saxsource.SaxSource;
import com.servingxml.util.Name;
import com.servingxml.util.record.Record;
import com.servingxml.util.ServingXmlException;
import com.servingxml.app.xmlpipeline.XmlFilterChain;
import com.servingxml.app.xmlpipeline.XmlPipeline;
import com.servingxml.components.error.CatchError;
import com.servingxml.io.streamsink.StreamSink;

/**
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class ContentTaskCatchError implements ContentTask {
  private final ContentTask contentTask;
  private final CatchError catchError;

  public ContentTaskCatchError(ContentTask contentTask, CatchError catchError) {
    this.contentTask = contentTask;
    this.catchError = catchError;
  }

  public String createString(ServiceContext context, Flow flow) {
    return contentTask.createString(context,flow);
  }

  public SaxSource createSaxSource(ServiceContext context, Flow flow) {
    return contentTask.createSaxSource(context, flow);
  }

  public XmlPipeline createXmlPipeline(ServiceContext context, Flow flow) {
    return contentTask.createXmlPipeline(context,flow);
  }

  public void appendToXmlPipeline(ServiceContext context, Flow flow, XmlFilterChain pipeline) {
    contentTask.appendToXmlPipeline(context,flow,pipeline);
  }

  public void execute(ServiceContext context, Flow flow) {
    try {
      contentTask.execute(context, flow);
    } catch (ServingXmlException e) {
      StreamSink errOutput = flow.getDefaultStreamSink();
      catchError.catchError(context, flow, e, errOutput);
    }
  }
}

