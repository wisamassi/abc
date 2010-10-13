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

package com.servingxml.components.task;

import com.servingxml.app.ServiceContext;
import com.servingxml.util.record.Record;
import com.servingxml.app.ParameterDescriptor;
import com.servingxml.app.Flow;
import com.servingxml.util.Name;

/**
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */                                                   
                                      
public class TaskPrefilter implements Task {
  private final Task[] tasks;
  private final ParameterDescriptor[] parameterDescriptors;

  public TaskPrefilter(Task[] tasks, ParameterDescriptor[] parameterDescriptors) {

    this.tasks = tasks;
    this.parameterDescriptors = parameterDescriptors;
  }

  public TaskPrefilter(Task task, ParameterDescriptor[] parameterDescriptors) {

    this.tasks = new Task[]{task};
    this.parameterDescriptors = parameterDescriptors;
  }

  public void execute(ServiceContext context, Flow flow) {
    Flow newFlow = flow.augmentParameters(context, parameterDescriptors);
    for (int i = 0; i < tasks.length; ++i) {
      Task task = tasks[i];
      task.execute(context, newFlow);
    }
  }

  public String createString(ServiceContext context, Flow flow) {
    Flow newFlow = flow.augmentParameters(context, parameterDescriptors);
    return tasks.length == 0 ? "" : tasks[0].createString(context,newFlow);
  }
}

