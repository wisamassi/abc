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

package com.servingxml.components.streamsink.file;

import java.net.URI;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import com.servingxml.ioc.components.ConfigurationContext;
import com.servingxml.util.ServingXmlException;
import com.servingxml.util.MessageFormatter;
import com.servingxml.util.ServingXmlMessages;
import com.servingxml.components.streamsink.StreamSinkFactory;
import com.servingxml.expr.substitution.SubstitutionExpr;

public class FileSinkFactoryAssembler {
  
  private String directory = "";
  private String filename = null;
  private String charsetName = null;

  public void setDirectory(String directory) {
    this.directory = directory;
  }
  
  public void setFile(String filename) {
    this.filename = filename;
  }

  public void setEncoding(String charsetName) {
    this.charsetName = charsetName;
  }

  public StreamSinkFactory assemble(ConfigurationContext context) {
    
    if (filename == null || filename.length() == 0) {
      String message = MessageFormatter.getInstance().getMessage(ServingXmlMessages.COMPONENT_ATTRIBUTE_REQUIRED,context.getElement().getTagName(),"file");
      throw new ServingXmlException(message);
    }

    Charset charset = null;
    if (charsetName != null) {
      try {
        charset = Charset.forName(charsetName);
      } catch (IllegalCharsetNameException e) {
        String message = MessageFormatter.getInstance().getMessage(ServingXmlMessages.COMPONENT_ATTRIBUTE_VALUE_INVALID,context.getElement().getTagName(),"charset");
        throw new ServingXmlException(message + ".  " + e.getMessage());
      } catch (UnsupportedCharsetException e) {
        String message = MessageFormatter.getInstance().getMessage(ServingXmlMessages.COMPONENT_ATTRIBUTE_VALUE_INVALID,context.getElement().getTagName(),"charset");
        throw new ServingXmlException(message + ".  " + e.getMessage());
      }
    }
    
    SubstitutionExpr directoryResolver = SubstitutionExpr.parseString(context.getQnameContext(),directory);
    SubstitutionExpr fileResolver = SubstitutionExpr.parseString(context.getQnameContext(),filename);

    File parent = null;
    try {
      URI uri = new URI(context.getQnameContext().getBase());
      String scheme = uri.getScheme();
      if (scheme != null && scheme.equals("file")) {
        File file = new File(uri);
        parent = file.isDirectory() ? file : file.getParentFile();
      }
    } catch (Exception e) {
      parent = null;
    }
    
    return new FileSinkFactory(directoryResolver,fileResolver,parent,charset);
  }
}
