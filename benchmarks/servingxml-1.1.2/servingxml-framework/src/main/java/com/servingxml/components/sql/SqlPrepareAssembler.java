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

package com.servingxml.components.sql;

import com.servingxml.app.ParameterDescriptor;
import com.servingxml.ioc.components.ConfigurationContext;
import com.servingxml.components.quotesymbol.QuoteSymbol;

/**
 * The <code>SqlPrepareAssembler</code> implements an assembler for
 * assembling system <code>SqlStatement</code> objects.
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class SqlPrepareAssembler {
  private ParameterDescriptor[] parameterDescriptors = ParameterDescriptor.EMPTY_ARRAY;
  private QuoteSymbol quoteSymbol = new QuoteSymbol('\'', "''");

  public void injectComponent(ParameterDescriptor[] parameterDescriptors) {
    this.parameterDescriptors = parameterDescriptors;
  }

  public void injectComponent(QuoteSymbol quoteSymbol) {

    this.quoteSymbol = quoteSymbol;
  }
  
  public SqlPreparedStatement assemble(final ConfigurationContext context) {
    
    SqlPreparedStatementFactory factory = new SqlPreparedStatementFactory(quoteSymbol);
    SqlPreparedStatement statement = factory.createSqlStatement(context);
    return statement;
  }
}

