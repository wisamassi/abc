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

package com.servingxml.app;

/**
 * The <code>Service</code> defines a set of transformations of content.
 *
 *  
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public interface Service {
  
  /**
   * Called by the presenter servlet to handle the request for the service.
   */
  void execute(ServiceContext context, Flow flow);
}
