package MatrixInverseCalculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.management.InvalidApplicationException;

public class MatrixInverseCalculator {

    public static void main(String[] args) {
        try {
            double[][] matrixA = loadMatrixFromFile("matrixA.txt");
            double[][] inverseMatrixA = calculateInverseMatrix(matrixA);
            saveMatrixToFile(inverseMatrixA, "inverse.txt");
            System.out.println("Matriz inversa calculada y guardada con éxito.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al procesar los archivos: " + e.getMessage());
        } catch (InvalidApplicationException e) {
            System.out.println("La matriz de entrada no es invertible: " + e.getMessage());
        }
    }

    private static double[][] loadMatrixFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int rows = 0;
        int columns = 0;
        while ((line = reader.readLine()) != null) {
            String[] elements = line.trim().split("\\s+");
            if (columns == 0) {
                columns = elements.length;
            } else if (columns != elements.length) {
                reader.close();
                throw new IOException("La matriz no tiene un número consistente de columnas en todas las filas.");
            }
            rows++;
        }
        reader.close();

        double[][] matrix = new double[rows][columns];
        reader = new BufferedReader(new FileReader(filePath));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            String[] elements = line.trim().split("\\s+");
            for (int col = 0; col < columns; col++) {
                matrix[row][col] = Double.parseDouble(elements[col]);
            }
            row++;
        }
        reader.close();
        return matrix;
    }

    private static void saveMatrixToFile(double[][] matrix, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (double[] row : matrix) {
            for (double element : row) {
                writer.write(String.valueOf(element));
                writer.write(" ");
            }
            writer.newLine();
        }
        writer.close();
    }

    static double[][] calculateInverseMatrix(double[][] matrix) throws InvalidApplicationException {
        int n = matrix.length;
        
        // Comprobamos si la matriz es cuadrada
        if (n != matrix[0].length) {
            throw new InvalidApplicationException("La matriz no es cuadrada y no tiene inversa.");
        }

        // Creamos una matriz identidad del mismo tamaño que la matriz original
        double[][] identityMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            identityMatrix[i][i] = 1.0;
        }

        // Realizamos eliminación Gaussiana para obtener la matriz triangular superior
        for (int i = 0; i < n; i++) {
            if (matrix[i][i] == 0.0) {
                // Si el elemento diagonal es cero, intercambiamos filas para evitar divisiones por cero
                boolean rowSwapped = false;
                for (int j = i + 1; j < n; j++) {
                    if (matrix[j][i] != 0.0) {
                        swapRows(matrix, i, j);
                        swapRows(identityMatrix, i, j);
                        rowSwapped = true;
                        break;
                    }
                }
                if (!rowSwapped) {
                    throw new InvalidApplicationException("La matriz no es invertible.");
                }
            }

            double pivot = matrix[i][i];
            for (int j = i + 1; j < n; j++) {
                double ratio = matrix[j][i] / pivot;
                subtractRow(matrix, j, i, ratio);
                subtractRow(identityMatrix, j, i, ratio);
            }
        }

        // Realizamos eliminación hacia atrás para obtener la matriz identidad en la parte izquierda
        for (int i = n - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                double ratio = matrix[j][i] / matrix[i][i];
                subtractRow(matrix, j, i, ratio);
                subtractRow(identityMatrix, j, i, ratio);
            }
        }

        // Dividimos cada fila por el elemento diagonal para obtener la matriz inversa
        for (int i = 0; i < n; i++) {
            double diagonalElement = matrix[i][i];
            for (int j = 0; j < n; j++) {
                matrix[i][j] /= diagonalElement;
                identityMatrix[i][j] /= diagonalElement;
            }
        }

        return identityMatrix;
    }

    private static void swapRows(double[][] matrix, int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private static void subtractRow(double[][] matrix, int targetRow, int sourceRow, double ratio) {
        int n = matrix[0].length;
        for (int i = 0; i < n; i++) {
            matrix[targetRow][i] -= ratio * matrix[sourceRow][i];
        }
    }

}
