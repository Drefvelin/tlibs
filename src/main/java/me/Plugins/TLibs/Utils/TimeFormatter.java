package me.Plugins.TLibs.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeFormatter {
	public static int StringTimeToMillis(String time) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd]|mo)");
        Matcher matcher = pattern.matcher(time);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        int value = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "s":
                return value * 1000;
            case "m":
                return value * 60 * 1000;
            case "h":
                return value * 60 * 60 * 1000;
            case "d":
                return value * 24 * 60 * 60 * 1000;
            case "mo":
                return value * 30 * 24 * 60 * 60 * 1000;
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
	}
	
	public static String formatTime(int seconds) {
	    int days = seconds / 86400;
	    seconds %= 86400;

	    int hours = seconds / 3600;
	    seconds %= 3600;

	    int minutes = seconds / 60;
	    seconds %= 60;

	    StringBuilder result = new StringBuilder();

	    if (days > 0) result.append(days).append("d ");
	    if (hours > 0) result.append(hours).append("h ");
	    if (minutes > 0) result.append(minutes).append("m ");
	    if (seconds > 0 || result.length() == 0) result.append(seconds).append("s");

	    return result.toString().trim();
	}
}
