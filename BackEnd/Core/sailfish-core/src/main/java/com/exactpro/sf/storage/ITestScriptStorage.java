/******************************************************************************
 * Copyright 2009-2018 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.exactpro.sf.storage;

import com.exactpro.sf.scriptrunner.IScriptRunListener;
import com.exactpro.sf.scriptrunner.TestScriptDescription;

import java.util.List;

public interface ITestScriptStorage extends IScriptRunListener {

    void updateTestScriptProperties(TestScriptDescription testScript);

    void setScriptRunListener(IScriptRunListener scriptRunListener);

    /**
     * Returns a list of the last few script runs stored in the workspace.
     * @param limit defines the script run description cap. Omitted descriptions won't be parsed. 0 means that all script runs should be loaded.
     * @return {@link LoadedTestScriptDescriptions}
     */
    LoadedTestScriptDescriptions getTestScriptList(int limit);

	void clear(boolean deleteOnDisk);

    /**
     * @return {@link TestScriptDescription} that failed to delete
     */
    List<TestScriptDescription> remove(boolean deleteOnDisk, List<TestScriptDescription> testScriptDescriptions);

}
