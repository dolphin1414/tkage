package tkpage.temp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

    private Calculator calculator; 
    @Before
    public void setUp() throws Exception {
        calculator = new Calculator();
    }

    @Test
    public final void testAdd() {
        assertEquals(5, calculator.add(2, 2));
    }

}
