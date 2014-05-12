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

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class ProposalOrderConfig {

	public static final ProposalOrderConfig INSTANCE = new ProposalOrderConfig();
	
	private HashMap<String, ProposalOrder> proposalOrders = new HashMap<String, ProposalOrder>();
	
	private ProposalOrderConfig() {
		super();
		load();
	}
	
	public int getRelevance(String className, String methodName, int defaultRelevance) {
		ProposalOrder po = this.proposalOrders.get(className);
		if (po != null) {
			return po.getRelavance(methodName, defaultRelevance);
		}
		return defaultRelevance;
	}
	
	public void addSeparators(String className, List<ICompletionProposal> proposals, int defaultRelevance) {
		ProposalOrder po = this.proposalOrders.get(className);
		if (po != null) {
			po.addSeparators(proposals, defaultRelevance);
		}
	}

	/********************************************/
	public static class ProposalOrder {
		private String[] order;

		ProposalOrder(String[] order) {
			super();
			this.order = order;
		}
		
		private int getRelavance(String methodName, int defaultRelevance) {
			int position = -1;
			int sz = 0;
			if (this.order != null) {
				sz = this.order.length;
				for (int i = 0; i < this.order.length; i++) {
					if (this.order[i].equals(methodName)) {
						position = i;
						break;
					}
				}
			}
			return position != -1 ? defaultRelevance + sz - position : defaultRelevance;
		}
		
		private void addSeparators(List<ICompletionProposal> proposals, int defaultRelevance) {
			if (this.order != null) {
				int sz = this.order.length;
				for (int i = 0; i < this.order.length; i++) {
					if (this.order[i].equals(JCypherConstants.PROPOSAL_SEPARATOR_KEY)) {
						proposals.add(new CompletionSeparator(defaultRelevance + sz - i));
					}
				}
			}
		}
	}
	
	private void load() {
		
		// order list must contain relevant methods of super classes too !!!
		
		this.proposalOrders.put("Relation", new ProposalOrder(new String[] {
				"node",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"property","type", "in", "out",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY
		}));
		this.proposalOrders.put("Node", new ProposalOrder(new String[] {
				"relation",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"property","label"
		}));
		this.proposalOrders.put("MATCH", new ProposalOrder(new String[] {
				"node",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"relation",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"path", "shortestPath"
		}));
		this.proposalOrders.put("WHERE", new ProposalOrder(new String[] {
				"valueOf", "existsPattern", "has", "holdsTrue",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"NOT"
//				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
//				"BR_OPEN"
		}));
		this.proposalOrders.put("Concat", this.proposalOrders.get("WHERE"));
		this.proposalOrders.put("Concatenator", new ProposalOrder(new String[] {
				"AND", "OR", "XOR",
				JCypherConstants.PROPOSAL_SEPARATOR_KEY,
				"BR_CLOSE"
		}));
	}
}
