package net.ellise.fizzBuzz.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Records the FizzBuzz answer for the current number.
 *
 * <p>The BPMN decides <em>which</em> answer is right (the exclusive gateway);
 * this class only writes it down. Every branch of the process calls this same
 * bean and passes its answer in an input parameter called {@code word}.
 *
 * <p>The bean name in {@code @Component} is what the BPMN refers to in
 * {@code camunda:delegateExpression="${recordResultDelegate}"}. Spring beans
 * are singletons, so this class keeps no state of its own: everything it needs
 * comes from the {@link DelegateExecution}.
 */
@Component("recordResultDelegate")
public class RecordResultDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(RecordResultDelegate.class);

    /** Name of the input parameter each service task sets. */
    static final String WORD = "word";

    /** Name of the process variable the caller reads back. */
    public static final String RESULT = "result";

    @Override
    public void execute(DelegateExecution execution) {
        // "word" is local to this activity: it exists only while the task runs.
        // For the "Use number" branch it holds n (an Integer), hence valueOf.
        String word = String.valueOf(execution.getVariableLocal(WORD));

        log.info("n={} -> {}", execution.getVariable("n"), word);

        // Write the result to the process instance itself, so it is still there
        // after this task's local variables (such as "word") have gone.
        execution.getProcessInstance().setVariable(RESULT, word);
    }
}
