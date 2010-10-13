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

package com.servingxml.components.saxfilter;

import java.util.Properties;
import com.servingxml.app.Flow;
import com.servingxml.app.ServiceContext;
import com.servingxml.app.xmlpipeline.XmlFilterChain;
import com.servingxml.components.content.Content;
import com.servingxml.components.saxfilter.AbstractXmlFilterAppender;
import com.servingxml.components.task.Task;
import com.servingxml.expr.ExpressionException;
import com.servingxml.expr.saxpath.RestrictedMatchPattern;
import org.xml.sax.XMLFilter;

class SubtreeFilterAppender extends AbstractXmlFilterAppender implements Content {
  private final RestrictedMatchPattern expr;
  private final Content[] xmlComponents;

  public SubtreeFilterAppender(RestrictedMatchPattern expr, Content[] xmlComponents) {
    this.expr = expr;
    this.xmlComponents = xmlComponents;
  }

  public void appendToXmlPipeline(ServiceContext context, Flow flow,
  XmlFilterChain pipeline) {
    SubtreeFilter subPipeline = new SubtreeFilter(context, flow, expr);
    for (int i = 0; i < xmlComponents.length; ++i) {
      xmlComponents[i].appendToXmlPipeline(context,flow,subPipeline);
    }
    pipeline.addXmlFilter(subPipeline);
  }
}


