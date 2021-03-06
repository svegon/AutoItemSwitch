/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.autoitemswitch.exceptions;

import com.google.gson.JsonElement;

public final class JsonException extends Exception {
	private static final long serialVersionUID = 1L;

	public JsonException() {
		super();
	}
	
	public JsonException(JsonElement message) {
		super(message.toString());
	}
	
	public JsonException(JsonElement message, Throwable cause) {
		super(message.toString(), cause);
	}
	
	public JsonException(String message) {
		super(message);
	}
	
	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JsonException(Throwable cause) {
		super(cause);
	}
}
