package org.exp;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ConsoleMain {
	public static void main(String[] args) {
		System.out.println(StringUtils.isEmpty("") ? "empty" : "not empty");
		
		Table<String, String, String> table = HashBasedTable.create();
		table.put("X1", "Y1", "1");
		
		System.out.println(table.get("X1", "Y1"));
		
		//mvn exec:java -Dexec.mainClass="org.exp.ConsoleMain"
	}
}
