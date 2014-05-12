/************************************************************************
 * Copyright (c) 2014 IoT-Solutions e.U.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ************************************************************************/

package iot.jcypher.eclipse.preferences;

import iot.jcypher.eclipse.JCypherConstants;
import jcypher_ext.Activator;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JCypherPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private BooleanFieldEditor enableCompletionField;
	
	@Override
	public void init(IWorkbench workbench) {
		setDescription("");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		enableCompletionField = new BooleanFieldEditor(
				JCypherConstants.PREF_ENABLE_COMPLETION,
				"Enable JCypher Completion Proposal", getFieldEditorParent());
		addField(enableCompletionField);
	}

}
