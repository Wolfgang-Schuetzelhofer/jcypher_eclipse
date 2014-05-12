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

package iot.jcypher.eclipse;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class CompletionSeparator implements IJavaCompletionProposal, ICompletionProposal {

	public static final CompletionSeparator INSTANCE = new CompletionSeparator(JCypherConstants.DEFAULT_RELEVANCE - 1);
	
	private int relevance;
	
	CompletionSeparator(int relevance) {
		super();
		this.relevance = relevance;
	}

	@Override
	public void apply(IDocument document) {
	}

	@Override
	public Point getSelection(IDocument document) {
		return null;
	}

	@Override
	public String getAdditionalProposalInfo() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return "----------------------------";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IContextInformation getContextInformation() {
		return null;
	}

	@Override
	public int getRelevance() {
		return this.relevance;
	}

}
