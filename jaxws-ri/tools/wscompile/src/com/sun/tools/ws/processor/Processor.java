/*
 * $Id: Processor.java,v 1.2 2005-05-25 20:16:35 kohlert Exp $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.tools.ws.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.tools.ws.processor.config.Configuration;
import com.sun.tools.ws.processor.config.ModelInfo;
import com.sun.tools.ws.processor.model.Model;
import com.sun.tools.ws.processor.util.ProcessorEnvironment;
import com.sun.xml.ws.util.exception.JAXWSExceptionBase;
import com.sun.xml.ws.util.exception.LocalizableExceptionAdapter;

/**
 *
 * @author JAX-RPC Development Team
 */
public class Processor {

    public Processor(Configuration configuration, Properties options, Model model) {
        _configuration = configuration;
        _options = options;
        _actions = new ArrayList();

        // find the value of the "print stack traces" property
        _printStackTrace = Boolean.valueOf(_options.getProperty(
            ProcessorOptions.PRINT_STACK_TRACE_PROPERTY)).booleanValue();
        _env = (ProcessorEnvironment)_configuration.getEnvironment();
        _model = model;
    }

    public Processor(Configuration configuration, Properties options) {
        _configuration = configuration;
        _options = options;
        _actions = new ArrayList();

        // find the value of the "print stack traces" property
        _printStackTrace = Boolean.valueOf(_options.getProperty(
            ProcessorOptions.PRINT_STACK_TRACE_PROPERTY)).booleanValue();
        _env = (ProcessorEnvironment)_configuration.getEnvironment();
    }

    public void add(ProcessorAction action) {
        _actions.add(action);
    }

    public Model getModel() {
        return _model;
    }

    public void run() {
        runModeler();
        if (_model != null) {
            runActions();
        }
    }

    public void runModeler() {
        try {
            ModelInfo modelInfo = (ModelInfo)_configuration.getModelInfo();
            if (modelInfo == null) {
                throw new ProcessorException("processor.missing.model");
            }

            _model = modelInfo.buildModel(_options);

        } catch (JAXWSExceptionBase e) {
            if (_printStackTrace) {
                _env.printStackTrace(e);
            }
            _env.error(e);
        } catch (Exception e) {
            if (_printStackTrace) {
                _env.printStackTrace(e);
            }
            _env.error(new LocalizableExceptionAdapter(e));
        }
    }

    public void runActions() {
        try {
            if (_model == null) {

                // avoid reporting yet another error here
                return;
            }

            for (Iterator iter = _actions.iterator(); iter.hasNext();) {
                ProcessorAction action = (ProcessorAction) iter.next();
                action.perform(_model, _configuration, _options);
            }
        } catch (JAXWSExceptionBase e) {
            if (_printStackTrace || _env.verbose()) {
                _env.printStackTrace(e);
            }
            _env.error(e);
        } catch (Exception e) {
            if (_printStackTrace || _env.verbose()) {
                _env.printStackTrace(e);
            }
            _env.error(new LocalizableExceptionAdapter(e));
        }
    }

    private Properties _options;
    private Configuration _configuration;
    private List _actions;
    private Model _model;
    private boolean _printStackTrace;
    private ProcessorEnvironment _env;
}
