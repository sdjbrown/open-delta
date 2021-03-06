/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.OnOffDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public abstract class OnOffDirective extends StandardIntkeyDirective {
    
    public OnOffDirective(boolean errorIfNoDatasetLoaded, String... controlWords) {
        super(errorIfNoDatasetLoaded, controlWords);
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        //arguments.add(new OnOffArgument("value", null, context.displayComments()));
        arguments.add(new OnOffArgument("value", null, getInitialOnOffValue(context)));
        return arguments;
    }
    
    abstract boolean getInitialOnOffValue(IntkeyContext context);

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList() {
        return null;
    }

    @Override
    public abstract OnOffDirectiveInvocation buildCommandObject();
}
