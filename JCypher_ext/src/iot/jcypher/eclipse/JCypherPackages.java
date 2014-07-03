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
import java.util.List;

public class JCypherPackages {

	private static List<String> paths;
	static {
		paths = new ArrayList<String>();
		paths.add(JCypherConstants.JCYPHER_QUERY_API_PACKAGE);
		paths.add(JCypherConstants.JCYPHER_VALUES_PACKAGE);
		paths.add(JCypherConstants.JCYPHER_FACTORIES_PACKAGE);
	}
	
	public static boolean addsToProposal(String path) {
		for (String p : paths) {
			if (path.indexOf(p) == 0)
				return true;
		}
		return false;
	}
}
