package me.Plugins.TLibs.Utils;

import java.util.List;

public class TabCleaner {
	public static void cleanTab(List<String> completions, String[] args) {
		if(args.length >= 1) {
        	for(int i = 0; i<completions.size(); i++) {
        		String s = completions.get(i);
        		if(!s.contains(args[0])) {
        			completions.remove(s);
        			i--;
        		}
        	}
        }
	}
}
