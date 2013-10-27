package com.mysliborski.tools.helper;

import java.util.Collection;

public class GeneralHelper {

	public static String concat(Object... objects) {
		StringBuilder str = new StringBuilder();
		for (Object obj : objects) {
			str.append(obj);
		}
		return str.toString();
	}


    /**
     * Use android.text.TextUtils instead
     * @param str
     * @return
     */
    @Deprecated
	public static boolean empty(String str) {
		return str == null || "".equals(str);
	}

	public static boolean empty(Collection<?> col) {
		return col==null || col.size()==0;
	}
	
	public static String getFileNameFromURL(String url) {
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}

	/**
	 * Returns original value if not null, or default value
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static <X> X defVal(X value, X defaultValue) {
		return value != null ? value : defaultValue;
	}
}
