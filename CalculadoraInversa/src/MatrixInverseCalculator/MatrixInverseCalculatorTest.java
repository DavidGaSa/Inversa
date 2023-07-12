package MatrixInverseCalculator;

import javax.management.InvalidApplicationException;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class MatrixInverseCalculatorTest {

    @Test
    public void testInverseMatrixCalculation() {
        // Definir una matriz de prueba
        double[][] matrix = {{1, 2}, {3, 4}};

        try {
            double[][] inverseMatrix = MatrixInverseCalculator.calculateInverseMatrix(matrix);

            // Verificar el resultado esperado de la matriz inversa
            double[][] expectedInverseMatrix = {{-2, 1}, {1.5, -0.5}};
            Assertions.assertArrayEquals(expectedInverseMatrix, inverseMatrix);
        } catch (InvalidApplicationException e) {
            Assertions.fail("La matriz de prueba debería ser invertible");
        }
    }
}
