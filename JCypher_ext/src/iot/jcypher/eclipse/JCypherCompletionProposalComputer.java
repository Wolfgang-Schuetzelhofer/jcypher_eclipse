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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jcypher_ext.Activator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.java.AbstractJavaCompletionProposal;
import org.eclipse.jdt.internal.ui.text.java.FillArgumentNamesCompletionProposalCollector;
import org.eclipse.jdt.internal.ui.text.java.JavaMethodCompletionProposal;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class JCypherCompletionProposalComputer implements IJavaCompletionProposalComputer {

	private static final long JAVA_CODE_ASSIST_TIMEOUT= Long.getLong("org.eclipse.jdt.ui.codeAssistTimeout", 5000).longValue(); // ms //$NON-NLS-1$
	
	private String fErrorMessage;
	private final IProgressMonitor fTimeoutProgressMonitor;
	
	public JCypherCompletionProposalComputer() {
		super();
		fTimeoutProgressMonitor= createTimeoutProgressMonitor(JAVA_CODE_ASSIST_TIMEOUT);
	}

	@Override
	public void sessionStarted() {
	}

	@Override
	public List<ICompletionProposal> computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		
		try {
			IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
			boolean enabled = prefs.getBoolean(JCypherConstants.PREF_ENABLE_COMPLETION);
			
			if (enabled) {
				if (context instanceof JavaContentAssistInvocationContext) {
					JavaContentAssistInvocationContext javaContext= (JavaContentAssistInvocationContext) context;
					return internalComputeCompletionProposals(context.getInvocationOffset(), javaContext);
				}
			}
		} catch(Throwable e ) {
			// do nothing, just prevent destroying original proposals
		}
		return Collections.emptyList();
	}

	private List<ICompletionProposal> internalComputeCompletionProposals(
			int offset, JavaContentAssistInvocationContext context) {
		ICompilationUnit unit= context.getCompilationUnit();
		if (unit == null)
			return Collections.emptyList();

		ITextViewer viewer= context.getViewer();

		CompletionProposalCollector collector= createCollector(context);
		collector.setInvocationContext(context);

		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF, CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF, CompletionProposal.TYPE_IMPORT, true);
		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF, CompletionProposal.FIELD_IMPORT, true);

		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF, CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF, CompletionProposal.TYPE_IMPORT, true);
		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF, CompletionProposal.METHOD_IMPORT, true);

		collector.setAllowsRequiredProposals(CompletionProposal.CONSTRUCTOR_INVOCATION, CompletionProposal.TYPE_REF, true);

		collector.setAllowsRequiredProposals(CompletionProposal.ANONYMOUS_CLASS_CONSTRUCTOR_INVOCATION, CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.ANONYMOUS_CLASS_DECLARATION, CompletionProposal.TYPE_REF, true);

		collector.setAllowsRequiredProposals(CompletionProposal.TYPE_REF, CompletionProposal.TYPE_REF, true);

		try {
			Point selection= viewer.getSelectedRange();
			if (selection.y > 0)
				collector.setReplacementLength(selection.y);
			unit.codeComplete(offset, collector, fTimeoutProgressMonitor);
		} catch (OperationCanceledException x) {
			fErrorMessage= "Code assist took too long and is incomplete. Please close the list and type more characters";
		} catch (JavaModelException x) {
			Shell shell= viewer.getTextWidget().getShell();
			if (x.isDoesNotExist() && !unit.getJavaProject().isOnClasspath(unit))
				MessageDialog.openInformation(shell, "Cannot Perform Operation", "The compilation unit is not on the build path of a Java project.");
			else
				ErrorDialog.openError(shell, "Error Accessing Compilation Unit", "Cannot access compilation unit", x.getStatus());
		}

		ICompletionProposal[] javaProposals= collector.getJavaCompletionProposals();

		List<ICompletionProposal> proposals= new ArrayList<ICompletionProposal>(Arrays.asList(javaProposals));
		if (proposals.size() == 0) {
			String error= collector.getErrorMessage();
			if (error.length() > 0)
				fErrorMessage= error;
		}
		return buildJCypherProposals(proposals);
	}
	
	private List<ICompletionProposal> buildJCypherProposals(
			List<ICompletionProposal> proposals) {
		List<ICompletionProposal> ret = new ArrayList<>(proposals.size());
		List<String> classNames = new ArrayList<String>();
		List<String> classNames_ext = new ArrayList<String>();
		for (ICompletionProposal prop : proposals) {
			if (prop instanceof IJavaCompletionProposal) {
				if (prop instanceof JavaMethodCompletionProposal) {
					JavaMethodCompletionProposal jmcp = (JavaMethodCompletionProposal)prop;
					IJavaElement elem = jmcp.getJavaElement();
					IJavaElement classfile = elem.getParent().getParent();
					if (classfile.getElementType() == IJavaElement.CLASS_FILE ||
							classfile.getElementType() == IJavaElement.COMPILATION_UNIT) {
						IJavaElement pkg = classfile.getParent();
						if (JCypherPackages.addsToProposal(pkg.getElementName())) {
							IJavaCompletionProposal delegate = (IJavaCompletionProposal)prop;
							IJavaElement mthd = ((AbstractJavaCompletionProposal)delegate).getJavaElement();
							IJavaElement clsfile = elem.getParent().getParent();
							String className = clsfile.getElementName();
							if (!classNames_ext.contains(className)) {
								classNames_ext.add(className);
								// trim className extension which can either be .class or .java
								classNames.add(className.substring(0, className.indexOf('.')));
							}
							// classNames always contains (class, superClass, superSuperClass, ..)
							int relevance = ProposalOrderConfig.INSTANCE.getRelevance(
									classNames.get(0), mthd.getElementName(), JCypherConstants.DEFAULT_RELEVANCE);
							ret.add(new JCypherCompletionProposal((IJavaCompletionProposal)prop, relevance));
						}
					}
				}
			}
		}
		if (ret.size() > 0) {
			ProposalOrderConfig.INSTANCE.addSeparators(classNames.get(0), ret, JCypherConstants.DEFAULT_RELEVANCE);
			ret.add(CompletionSeparator.INSTANCE);
		}
		return ret;
	}

	protected CompletionProposalCollector createCollector(JavaContentAssistInvocationContext context) {
		CompletionProposalCollector collector;
		if (PreferenceConstants.getPreferenceStore().getBoolean(PreferenceConstants.CODEASSIST_FILL_ARGUMENT_NAMES))
			collector = new FillArgumentNamesCompletionProposalCollector(context);
		else
			collector = new CompletionProposalCollector(context.getCompilationUnit(), true);
		
		collector.setIgnored(CompletionProposal.METHOD_NAME_REFERENCE, false);
		collector.setIgnored(CompletionProposal.METHOD_REF, false);
		
		return collector;
	}
	
	private IProgressMonitor createTimeoutProgressMonitor(final long timeout) {
		return new IProgressMonitor() {

			private long fEndTime;
			
			public void beginTask(String name, int totalWork) {
				fEndTime= System.currentTimeMillis() + timeout;
			}
			public boolean isCanceled() {
				return fEndTime <= System.currentTimeMillis();
			}
			public void done() {
			}
			public void internalWorked(double work) {
			}
			public void setCanceled(boolean value) {
			}
			public void setTaskName(String name) {
			}
			public void subTask(String name) {
			}
			public void worked(int work) {
			}
		};
	}

	@Override
	public List<IContextInformation> computeContextInformation(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		return fErrorMessage;
	}

	@Override
	public void sessionEnded() {
		fErrorMessage= null;
	}

}
