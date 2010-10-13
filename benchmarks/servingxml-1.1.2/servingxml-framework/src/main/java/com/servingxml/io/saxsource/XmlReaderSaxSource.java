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

package com.servingxml.io.saxsource;

import java.util.Properties;

import javax.xml.transform.TransformerFactory;

import org.xml.sax.XMLReader;

import com.servingxml.io.cache.Expirable;
import com.servingxml.io.cache.Key;
import com.servingxml.io.cache.DefaultKey;
import com.servingxml.util.xml.SourceXmlReader;

/**
 * Implements a simple SaxSource. 
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class XmlReaderSaxSource extends AbstractSaxSource {
  private final XMLReader xmlReader;
  private final String systemId;
  private final Key key;
  private final Expirable expirable;

  public XmlReaderSaxSource(XMLReader xmlReader, TransformerFactory transformerFactory) {
    super(transformerFactory);
    this.xmlReader = xmlReader;
    this.key = DefaultKey.newInstance();
    this.systemId = key.toString();
    this.expirable = Expirable.IMMEDIATE_EXPIRY;
  }

  public XmlReaderSaxSource(XMLReader xmlReader, Properties outputProperties,
    TransformerFactory transformerFactory) {
    super(outputProperties, transformerFactory);
    this.xmlReader = xmlReader;
    this.key = DefaultKey.newInstance();
    this.systemId = key.toString();
    this.expirable = Expirable.IMMEDIATE_EXPIRY;
  }

  public XmlReaderSaxSource(XMLReader xmlReader, Key key, Expirable expirable, 
    Properties outputProperties, TransformerFactory transformerFactory) {
    super(outputProperties, transformerFactory);
    this.xmlReader = xmlReader;
    this.key = key;
    this.systemId = key.toString();
    this.expirable = expirable;
  }

  public XMLReader createXmlReader() {
    return xmlReader;
  }

  public Key getKey() {
    return key;
  }

  public Expirable getExpirable() {
    return expirable;
  }

  public String getSystemId() {
    return systemId;
  }
}
