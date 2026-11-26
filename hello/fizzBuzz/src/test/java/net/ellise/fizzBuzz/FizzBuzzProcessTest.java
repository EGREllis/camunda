package net.ellise.fizzBuzz;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import net.ellise.fizzBuzz.delegate.RecordResultDelegate;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Starts the real Spring Boot app (with its embedded Camunda engine and
 * in-memory H2 database) and runs the "fizzBuzz" process once per number.
 */
@SpringBootTest
class FizzBuzzProcessTest {

    /** The expected answer for n = 1 .. 15, in order. */
    private static final List<String> EXPECTED = List.of(
            "1", "2", "Fizz", "4", "Buzz",
            "Fizz", "7", "8", "Fizz", "Buzz",
            "11", "Fizz", "13", "14", "FizzBuzz");

    @Autowired
    private RuntimeService runtimeService;

    @Test
    void producesFizzBuzzForOneToFifteen() {
        for (int n = 1; n <= 15; n++) {
            // The process has no wait states, so it runs to the end inside
            // this call. executeWithVariablesInReturn() hands back the
            // variables as they were when it finished.
            ProcessInstanceWithVariables instance = runtimeService
                    .createProcessInstanceByKey("fizzBuzz")
                    .businessKey("n-" + n)
                    .setVariable("n", n)
                    .executeWithVariablesInReturn();

            assertThat(instance.isEnded())
                    .as("process for n=%d should have finished", n)
                    .isTrue();
            assertThat(instance.getVariables().get(RecordResultDelegate.RESULT))
                    .as("result for n=%d", n)
                    .isEqualTo(EXPECTED.get(n - 1));
        }
    }
}
