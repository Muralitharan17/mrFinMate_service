package com.murali.mrFinMate.utils;

import org.springframework.stereotype.Service;

@Service
public class CommonUtils {
	
	public boolean isNull(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public boolean isNotNull(String str) {
		return !isNull(str);
	}

}
