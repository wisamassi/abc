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

package com.servingxml.components.content;

import javax.xml.transform.TransformerFactory;

import org.xml.sax.XMLReader;

import com.servingxml.util.SystemConstants;
import com.servingxml.io.cache.Expirable;
import com.servingxml.io.cache.Key;
import com.servingxml.io.cache.DefaultKey;
import com.servingxml.util.record.Record;
import com.servingxml.util.record.RecordImpl;
import com.servingxml.util.QualifiedName;
import com.servingxml.io.saxsource.SaxSource;
import com.servingxml.io.saxsource.AbstractSaxSource;
import com.servingxml.util.QnameContext;
import com.servingxml.util.SimpleQnameContext;

/**
 * A <code>EmptySaxSource</code> instance may be used as a default.
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

class EmptySaxSource extends AbstractSaxSource implements SaxSource {     
  private static final Record record = new RecordImpl(
    new QualifiedName(SystemConstants.SERVINGXML_NS_URI, "document"));

  private final static Key key = DefaultKey.newInstance();
  private final static QnameContext context = new SimpleQnameContext();

  public EmptySaxSource(TransformerFactory transformerFactory) {
    super(transformerFactory);
  }

  public Expirable getExpirable() {
    return Expirable.IMMEDIATE_EXPIRY;
  }                      

  public XMLReader createXmlReader() {
    
    return record.createXmlReader(context.getPrefixMap());
  }

  public String getSystemId() {
    return key.toString();
  }

  public Key getKey() {
    return key;
  }
}

