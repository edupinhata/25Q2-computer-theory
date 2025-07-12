package test

import intSymmetricMatrixUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class intSymmetricMatrixUtilTest {

    private lateinit var exampleMatrix1 : Array<IntArray>

    @BeforeEach
    fun setup(){
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
    }

    @Test
    fun `test getMaxNullMatrixRows`(){
        val matrixUtil = intSymmetricMatrixUtil(exampleMatrix1)
        var nullMatrix = matrixUtil.getMaxNullMatrixRows(0)
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 0")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(1)
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 1")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(2)
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 2")
        nullMatrix = matrixUtil.getMaxNullMatrixRows(3)
        matrixUtil.printMatrix(nullMatrix)
        println("Matrix row 3")

    }

}