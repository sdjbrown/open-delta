package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.OutputCommentDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.ShowDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class OutputCommentDirective extends NewIntkeyDirective {

    public OutputCommentDirective() {
        super(false, "output", "comment");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new StringArgument("text", "Enter text", null, false));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList() {
        return null;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new OutputCommentDirectiveInvocation();
    }

}