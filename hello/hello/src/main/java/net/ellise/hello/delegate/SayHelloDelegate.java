package net.ellise.hello.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("sayHelloDelegate")
public class SayHelloDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(SayHelloDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("Hello, document {}", execution.getBusinessKey());
        execution.setVariable("greeting", "Hello " + execution.getBusinessKey());
    }
}
