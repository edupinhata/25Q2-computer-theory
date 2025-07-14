import kotlin.collections.ArrayList

class IntSymmetricMatrix(matrix: Array<IntArray>) {

    private var matrix :  Array<IntArray>
    private var nRows : Int
    private var nCols : Int
    private var degrees : Array<Int>

    init {
        this.matrix = matrix
        this.nRows = matrix.size
        this.nCols = matrix[0].size
        this.degrees = getDegrees()
        // TODO: Verify if it's symmetric
    }

    fun getMatrix(): Array<IntArray> {
        return matrix
    }

    private fun getDegrees(): Array<Int> {
       val degrees = Array<Int>(nRows) {0}
       for (row in 0..nRows-1){
           degrees[row] = matrix[row].reduce { acc, adj -> acc + adj}
       }
        return degrees
    }

    fun getNullColumns(row: Int) : List<Int> {
        val nullColumns = ArrayList<Int>()
        for (col in 0..nCols-1){
            if (matrix[row][col] == 0 && row != col){
                nullColumns.add(col)
            }
        }
        return nullColumns
    }

    fun getNonNullColumns(row: Int): ArrayList<Int> {
       var nonNullColumns = ArrayList<Int>()
        for (col in 0..nCols-1){
            if (matrix[row][col] != 0 ){
                nonNullColumns.add(col)
            }
        }
        return nonNullColumns
    }

    fun getNonNullColumnsMapping(rows : List<Int>): HashMap<Int, ArrayList<Int>> {
        var map = HashMap<Int, ArrayList<Int>>()
        for (row in rows){
            map[row] = getNonNullColumns(row)
        }

        return map.entries
            .sortedWith ( compareBy(
                {it.value.size}, {it.key}))
            .associate { it.toPair() }
            .toMap(LinkedHashMap())
    }

    fun getMaxDegreeRows(alreadyProcessedRows: ArrayList<Int>) : ArrayList<Int> {
        var maxDegree = 0
        var maxDegreeRows = ArrayList<Int>()
        var curDegree = 0
        for (row in 0..nRows-1){
            // TODO: Improve performance in this check
            if (row in alreadyProcessedRows){continue}
            curDegree = getDegree(row)
            if (curDegree == maxDegree){
                maxDegreeRows.add(row)
            }
            else if (curDegree > maxDegree){
                maxDegree = curDegree
                maxDegreeRows = arrayListOf(row)
            }
        }
        return maxDegreeRows
    }

    fun getMaxDegree(alreadyProcessedRows: ArrayList<Int>): Int {
        return getDegree(getMaxDegreeRows(alreadyProcessedRows)[0])
    }

    fun getDegree(row: Int) : Int {
        return this.degrees[row]
    }

    fun getMaxNullMatrixRows(row: Int, alreadyProcessedRows: ArrayList<Int>): ArrayList<Int> {
        var nullCols = getNullColumns(row)
        var nonNullColMapping = getNonNullColumnsMapping(nullCols)
        var acceptedRows = arrayListOf(row)
        var rejectedRows = alreadyProcessedRows.toMutableList()

        nullCols.forEach { col ->
            if (!rejectedRows.contains(col)){
                acceptedRows.add(col)
                rejectedRows.addAll(nonNullColMapping[col]!!)
            }
        }
        println("======= Null Matrix Size: " + acceptedRows.size)
        return acceptedRows
    }

    fun printMatrix(rows: List<Int>) {
        println("Rows: ${rows.joinToString(", ")}")
        println("Size: ${rows.size}")
        for (row in rows) {
            var rowStr = ""
            for (col in rows){
                rowStr +=  "${matrix[row][col]} "
            }
            println(rowStr)
        }
    }


}