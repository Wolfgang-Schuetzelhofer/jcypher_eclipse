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

public class FactoryClassSymbolsConfig {

	private static List<String> factoryClassSymbols;
	
	public static boolean isFactoryClassSymbol(String symbol) {
		if (factoryClassSymbols == null)
			factoryClassSymbols = loadConfig();
		for (String sym : factoryClassSymbols) {
			if (sym.equals(symbol))
				return true;
		}
		return false;
	}
	
	private static List<String> loadConfig() {
		List<String> ret = new ArrayList<String>();
		ret.add("C");
		ret.add("F");
		ret.add("I");
		ret.add("P");
		ret.add("X");
		return ret;
	}
}
