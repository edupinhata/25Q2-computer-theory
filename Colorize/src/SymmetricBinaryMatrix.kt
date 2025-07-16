import kotlin.collections.ArrayList

class SymmetricBinaryMatrix(private var matrix: HashMap<Int, ArrayList<Int>>) {

    private var nRows : Int = matrix.size
    private var nCols : Int = this.nRows
    //TODO: check if it's symetric

    fun getMatrix(): HashMap<Int, ArrayList<Int>> {
        return matrix
    }

    private fun getNullColumns(row: Int) : List<Int> {
        // Like a countingSort, but to generate the array O(n), n~=3
        val rowExpanded = Array<Int>(nCols){0}
        val nullColumns = ArrayList<Int>()

        matrix[row]!!.forEach{ c ->
            rowExpanded[c] = 1
        }

        for (c in rowExpanded.indices){
            if(rowExpanded[c] == 0) nullColumns.add(c)
        }
        return nullColumns
    }

    private fun getNonNullColumns(row: Int): ArrayList<Int> {
        return matrix[row]!!
    }

    private fun getNonNullColumnsSizeSorted(rows : List<Int>): HashMap<Int, Int> {
        var mapSize = HashMap<Int, Int>()
        for (row in rows){
            mapSize[row] = getNonNullColumns(row).size
        }

        return mapSize.toList().sortedBy {it.second}.toMap() as HashMap<Int, Int>
    }

    fun getMaxNullMatrixRows(row: Int, alreadyProcessedRows: HashMap<Int, Boolean>): ArrayList<Int> {
        var nullCols = getNullColumns(row)
        var nonNullColSizeSorted = getNonNullColumnsSizeSorted(nullCols)
        var acceptedRows = arrayListOf(row)
        var rejectedRows: MutableMap<Int, Boolean> = alreadyProcessedRows.toMap().toMutableMap()

        nonNullColSizeSorted.keys.forEach{ col ->
            if (rejectedRows[col] == false){
                acceptedRows.add(col)
                matrix[col]!!.forEach{ c -> rejectedRows[c] = true}
            }
        }
        //nullCols.forEach { col ->
        //    if (rejectedRows[col] == false){
        //        acceptedRows.add(col)
        //        nonNullColSizeSorted.keys.forEach{ nonNullCol ->  rejectedRows[nonNullCol] = true}
        //    }
        //}
        return acceptedRows
    }

    fun printMatrix(rows: List<Int>) {
        println("Rows: ${rows.joinToString(", ")}")
        println("Size: ${rows.size}")
        for (row in rows) {
            val cols = getNonNullColumns(row).joinToString(", ")
            println("$row: $cols")
        }
    }


}