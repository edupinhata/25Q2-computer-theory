package test

import SymmetricBinaryMatrix
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.HashMap

class intSymmetricMatrixTest {

    private lateinit var exampleMatrix1 : Array<IntArray>
    private lateinit var adjacencyMap: HashMap<Int, ArrayList<Int>>

    @BeforeEach
    fun setup(){
       adjacencyMap = HashMap()
       exampleMatrix1 =  arrayOf(
           intArrayOf(0,1,1,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0),
           intArrayOf(1,0,1,1,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0),
           intArrayOf(1,1,0,1,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0),
           intArrayOf(1,1,1,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,0),
           intArrayOf(1,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1),
           intArrayOf(0,0,0,0,1,0,1,0,0,0,0,0,0,1,1,0,0,0,1),
           intArrayOf(0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,0,0,0,0),
           intArrayOf(1,0,0,0,0,0,0,0,1,0,0,0,1,0,1,1,0,0,0),
           intArrayOf(0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,1,1,0,0),
           intArrayOf(0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0),
           intArrayOf(0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0),
           intArrayOf(0,0,0,1,0,0,0,0,0,0,1,0,1,1,0,0,0,1,0),
           intArrayOf(1,0,0,0,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0),
           intArrayOf(0,0,0,0,0,1,1,0,0,0,0,1,1,0,1,0,0,1,0),
           intArrayOf(0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,1,0,0,0),
           intArrayOf(0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,0),
           intArrayOf(0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1),
           intArrayOf(0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1),
           intArrayOf(0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0))

       for (rowIndex in exampleMatrix1.indices) {
           val connectedCols = ArrayList<Int>()
           for (colIndex in exampleMatrix1[rowIndex].indices) {
               if (exampleMatrix1[rowIndex][colIndex] == 1) {
                   connectedCols.add(colIndex)
               }
           }
           adjacencyMap[rowIndex] = connectedCols
       }
    }

    @Test
    fun `test getMaxNullMatrixRows`(){
        val matrixUtil = SymmetricBinaryMatrix(adjacencyMap)
        var nullMatrix = matrixUtil.getMaxNullMatrixRows(0, HashMap<Int, Boolean>())
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 0")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(1, HashMap<Int, Boolean>())
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 1")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(2, HashMap<Int, Boolean>())
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 2")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(3, HashMap<Int, Boolean>())
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 3")

    }

}