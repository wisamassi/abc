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

package com.servingxml.components.flatfile.recordtype;

import com.servingxml.app.Flow;
import com.servingxml.app.ServiceContext;
import com.servingxml.components.flatfile.RecordOutput;
import com.servingxml.components.flatfile.options.FlatFileOptions;
import com.servingxml.components.parameter.DefaultValue;
import com.servingxml.expr.substitution.IntegerSubstitutionExpr;
import com.servingxml.util.Alignment;
import com.servingxml.util.HexBin;
import com.servingxml.util.Name;
import com.servingxml.util.record.Record;

public class BinaryFieldWriter implements FlatRecordFieldWriter {
  private final Name fieldName;
  private final int start;
  private final IntegerSubstitutionExpr fieldWidthExpr;
  private final DefaultValue defaultValueEvaluator;
  private final char padCharacter;
  private final Alignment alignment;
  private final FlatFileOptions flatFileOptions;

  public BinaryFieldWriter(Name fieldName, int start, IntegerSubstitutionExpr fieldWidthExpr, 
		    DefaultValue defaultValueEvaluator, FlatFileOptions flatFileOptions) {
    this.fieldName = fieldName;
    this.start = start;
    this.fieldWidthExpr = fieldWidthExpr;
    this.defaultValueEvaluator = defaultValueEvaluator;
    this.padCharacter = flatFileOptions.getPadCharacter();
    this.alignment = flatFileOptions.getAlignment();
    this.flatFileOptions = flatFileOptions;
  }

  public void writeField(ServiceContext context, Flow flow, RecordOutput recordOutput) {
    writeField(context, flow, fieldName, recordOutput);
  }

  public void writeField(ServiceContext context, Flow flow, Name fieldName, RecordOutput recordOutput) {

    Record record = flow.getRecord();

    String value = record.getString(fieldName);
    if (value == null) {
      value = defaultValueEvaluator.evaluateString(context, flow);
    }

    byte[] data = HexBin.decode(value);
    int offset = flatFileOptions.rebaseIndex(start);
    if (offset >= 0) {
      recordOutput.setPosition(offset);
    }
    recordOutput.writeBytes(data);
  }

  public void writeEndDelimiterTo(RecordOutput recordOutput) {
  }
}
