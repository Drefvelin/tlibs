package me.Plugins.TLibs.Objects.API.SubAPI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class StringFormatter {
	
	public static String formatHex(String s) {
		String formatted = "";
		List<String> split = List.of(s.split(""));
		for(int i = 0; i<split.size(); i++) {
			String bit = split.get(i);
			if(!bit.equalsIgnoreCase("#")) {
				formatted = formatted+bit;
			} else {
				String code = "#";
				int c = 0;
				while(c < 6 && i<split.size()) {
					i++;
					code = code+split.get(i);
					c++;
				}
				try {
					formatted = formatted+ChatColor.of(code);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return formatted;
	}

	public static String clean(String s) {
		StringBuilder cleaned = new StringBuilder();
		int i = 0;
		while (i < s.length()) {
			char ch = s.charAt(i);
			if (ch == '#' && i + 6 < s.length()) {
				// Skip the color code (7 characters total: '#' + 6 hex digits)
				String potentialHex = s.substring(i + 1, i + 7);
				if (potentialHex.matches("[0-9a-fA-F]{6}")) {
					i += 7;
					continue;
				}
			}
			cleaned.append(ch);
			i++;
		}
		return cleaned.toString();
	}

	public static String getName(ItemStack i) {
		ItemMeta m = i.getItemMeta();
		if(m == null) return "none";
		if(m.hasDisplayName()) return m.getDisplayName();
		return getVanillaName(i.getType());
	}

	public static String getVanillaName(Material material) {
		String[] words = material.name().toLowerCase().split("_");
		return Arrays.stream(words)
					.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
					.collect(Collectors.joining(" "));
	}

	public static String extractHexColor(String s) {
		if (s == null) return null;

		// --- Adventure hex format (#RRGGBB) ---
		int hashIndex = s.indexOf('#');
		if (hashIndex != -1 && hashIndex + 7 <= s.length()) {
			String hex = s.substring(hashIndex, hashIndex + 7);
			if (hex.matches("^#[0-9a-fA-F]{6}$"))
				return hex;
		}

		// --- Vanilla RGB formatting: §x§R§R§G§G§B§B ---
		// We search for "§x" + 12 characters (6 hex digits, each prefixed by §)
		int x = s.indexOf("§x");
		if (x != -1 && x + 14 <= s.length()) {
			StringBuilder hex = new StringBuilder("#");
			for (int i = x + 2; i < x + 14; i += 2) {
				char c = s.charAt(i + 1);
				if (!Character.toString(c).matches("[0-9a-fA-F]")) return null;
				hex.append(c);
			}
			return hex.toString();
		}

		return null; // No hex color found
	}
}
