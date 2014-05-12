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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class JCypherCompletionProposal implements IJavaCompletionProposal, ICompletionProposalExtension,
		ICompletionProposalExtension2, ICompletionProposalExtension3, ICompletionProposalExtension5,
		ICompletionProposalExtension6 {

	private IJavaCompletionProposal delegate;
	private int relevance;
	
	public JCypherCompletionProposal(IJavaCompletionProposal delegate, int relevance) {
		super();
		this.delegate = delegate;
		this.relevance = relevance;
	}

	@Override
	public void apply(IDocument document) {
		delegate.apply(document);
	}

	@Override
	public Point getSelection(IDocument document) {
		return delegate.getSelection(document);
	}

	@Override
	public String getAdditionalProposalInfo() {
		return delegate.getAdditionalProposalInfo();
	}

	@Override
	public String getDisplayString() {
		return delegate.getDisplayString();
	}

	@Override
	public Image getImage() {
		return ImageResource.ICON_JCYPHER;
	}

	@Override
	public IContextInformation getContextInformation() {
		return delegate.getContextInformation();
	}

	@Override
	public int getRelevance() {
		return this.relevance;
	}

	@Override
	public StyledString getStyledDisplayString() {
		if (delegate instanceof ICompletionProposalExtension6)
			return ((ICompletionProposalExtension6)delegate).getStyledDisplayString();
		return new StyledString(getAdditionalProposalInfo());
	}

	@Override
	public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
		if (delegate instanceof ICompletionProposalExtension5)
			return ((ICompletionProposalExtension5)delegate).getAdditionalProposalInfo(monitor);
		return null;
	}

	@Override
	public IInformationControlCreator getInformationControlCreator() {
		if (delegate instanceof ICompletionProposalExtension3)
			return ((ICompletionProposalExtension3)delegate).getInformationControlCreator();
		return null;
	}

	@Override
	public CharSequence getPrefixCompletionText(IDocument document,
			int completionOffset) {
		if (delegate instanceof ICompletionProposalExtension3)
			return ((ICompletionProposalExtension3)delegate).getPrefixCompletionText(document, completionOffset);
		return null;
	}

	@Override
	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
		if (delegate instanceof ICompletionProposalExtension3)
			return ((ICompletionProposalExtension3)delegate).getPrefixCompletionStart(document, completionOffset);
		return 0;
	}

	@Override
	public void apply(IDocument document, char trigger, int offset) {
		if (delegate instanceof ICompletionProposalExtension) {
			((ICompletionProposalExtension)delegate).apply(document, trigger, offset);
		}
	}

	@Override
	public boolean isValidFor(IDocument document, int offset) {
		if (delegate instanceof ICompletionProposalExtension)
			return ((ICompletionProposalExtension)delegate).isValidFor(document, offset);
		return false;
	}

	@Override
	public char[] getTriggerCharacters() {
		if (delegate instanceof ICompletionProposalExtension)
			return ((ICompletionProposalExtension)delegate).getTriggerCharacters();
		return null;
	}

	@Override
	public int getContextInformationPosition() {
		if (delegate instanceof ICompletionProposalExtension)
			return ((ICompletionProposalExtension)delegate).getContextInformationPosition();
		return 0;
	}

	@Override
	public void apply(final ITextViewer viewer, char trigger, int stateMask,
			int offset) {
		if (delegate instanceof ICompletionProposalExtension2) {
			((ICompletionProposalExtension2)delegate).apply(viewer, trigger, stateMask, offset);
			ISelection sel = viewer.getSelectionProvider().getSelection();
			if (sel instanceof ITextSelection) {
				final String txt = ((ITextSelection)sel).getText();
				if (FactoryClassSymbolsConfig.isFactoryClassSymbol(txt)) {
					Runnable r = new Runnable() {
						@Override
						public void run() {
							Point psel = viewer.getSelectedRange();
							viewer.setSelectedRange(psel.x + txt.length(), 0);
						}
					};
					Display.getCurrent().asyncExec(r);
				}
			}
		}
	}

	@Override
	public void selected(ITextViewer viewer, boolean smartToggle) {
		if (delegate instanceof ICompletionProposalExtension2)
			((ICompletionProposalExtension2)delegate).selected(viewer, smartToggle);
		
	}

	@Override
	public void unselected(ITextViewer viewer) {
		if (delegate instanceof ICompletionProposalExtension2)
			((ICompletionProposalExtension2)delegate).unselected(viewer);
		
	}

	@Override
	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		if (delegate instanceof ICompletionProposalExtension2)
			return ((ICompletionProposalExtension2)delegate).validate(document, offset, event);
		return false;
	}

}
