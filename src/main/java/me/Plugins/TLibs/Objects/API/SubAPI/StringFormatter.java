package me.Plugins.TLibs.Objects.API.SubAPI;

import java.util.ArrayList;
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

	/**
	 * Applies solid or gradient colour to plain text.
	 *
	 * @param text visible text without colour codes
	 * @param hexCodes one or more hex strings ({@code #RRGGBB} or {@code RRGGBB}); null/empty returns text unchanged
	 * @return legacy chat string with colour applied per character (gradient) or once (solid)
	 */
	public static String applyColourGradient(String text, List<String> hexCodes) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		String plain = ChatColor.stripColor(text);
		if (plain.isEmpty()) {
			return "";
		}
		if (hexCodes == null || hexCodes.isEmpty()) {
			return plain;
		}

		List<String> normalized = new ArrayList<>();
		for (String code : hexCodes) {
			String hex = normalizeHex(code);
			if (hex != null) {
				normalized.add(hex);
			}
		}
		if (normalized.isEmpty()) {
			return plain;
		}
		if (normalized.size() == 1) {
			return ChatColor.of(normalized.get(0)) + plain;
		}

		StringBuilder out = new StringBuilder(plain.length() * 8);
		int length = plain.length();
		int stops = normalized.size();
		for (int i = 0; i < length; i++) {
			double t = length == 1 ? 0.0 : (double) i / (length - 1);
			int segment = (int) Math.floor(t * (stops - 1));
			if (segment >= stops - 1) {
				segment = stops - 2;
			}
			double localT = t * (stops - 1) - segment;
			int[] rgb = lerpRgb(parseRgb(normalized.get(segment)), parseRgb(normalized.get(segment + 1)), localT);
			out.append(ChatColor.of(rgbToHex(rgb)));
			out.append(plain.charAt(i));
		}
		return out.toString();
	}

	private static String normalizeHex(String code) {
		if (code == null) {
			return null;
		}
		String trimmed = code.trim();
		if (trimmed.isEmpty()) {
			return null;
		}
		if (!trimmed.startsWith("#")) {
			trimmed = "#" + trimmed;
		}
		if (!trimmed.matches("^#[0-9a-fA-F]{6}$")) {
			return null;
		}
		return trimmed.toLowerCase();
	}

	private static int[] parseRgb(String hex) {
		int value = Integer.parseInt(hex.substring(1), 16);
		return new int[] {
				(value >> 16) & 0xFF,
				(value >> 8) & 0xFF,
				value & 0xFF
		};
	}

	private static int[] lerpRgb(int[] from, int[] to, double t) {
		double clamped = Math.max(0.0, Math.min(1.0, t));
		return new int[] {
				(int) Math.round(from[0] + (to[0] - from[0]) * clamped),
				(int) Math.round(from[1] + (to[1] - from[1]) * clamped),
				(int) Math.round(from[2] + (to[2] - from[2]) * clamped)
		};
	}

	private static String rgbToHex(int[] rgb) {
		return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
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
