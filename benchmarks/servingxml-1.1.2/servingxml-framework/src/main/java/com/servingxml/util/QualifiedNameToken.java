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

package com.servingxml.util;

/**
 *
 *  01/05/15
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 * 
 */

public class QualifiedNameToken extends NameTest {

  private final Name name;

  /**
   * Creates a name token
   */

  public QualifiedNameToken(Name name) {
    this.name = name;
  }

  public boolean matches(String namespaceUri, String localName) {
    return namespaceUri.equals(namespaceUri) && localName.equals(localName);
  }
}