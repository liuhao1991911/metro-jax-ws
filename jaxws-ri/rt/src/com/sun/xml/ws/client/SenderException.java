/*
 * $Id: SenderException.java,v 1.2 2005-05-25 20:16:24 kohlert Exp $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.ws.client;

import com.sun.xml.ws.util.exception.JAXWSExceptionBase;
import com.sun.xml.ws.util.localization.Localizable;

/**
 * @author JAX-RPC Development Team
 */
public class SenderException extends JAXWSExceptionBase {

    public SenderException(String key) {
        super(key);
    }

    public SenderException(String key, String arg) {
        super(key, arg);
    }

    public SenderException(String key, Object[] args) {
        super(key, args);
    }

    public SenderException(String key, Localizable arg) {
        super(key, arg);
    }

    public SenderException(Localizable arg) {
        super("sender.nestedError", arg);
    }

    public String getResourceBundleName() {
        return "com.sun.xml.ws.resources.sender";
    }
}
