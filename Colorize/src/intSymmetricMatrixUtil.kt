import java.util.ArrayList

class intSymmetricMatrixUtil(matrix: Array<IntArray>) {

    private var matrix :  Array<IntArray>
    private var nRows : Int
    private var nCols : Int

    init {
        this.matrix = matrix
        this.nRows = matrix.size
        this.nCols = matrix[0].size
        // TODO: Verify if it's symmetric
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

    public fun getMaxNullMatrixRows(row: Int): ArrayList<Int> {
        var nullCols = getNullColumns(row)
        var nonNullColMapping = getNonNullColumnsMapping(nullCols)
        var acceptedRows = arrayListOf(row)
        var rejectedRows = ArrayList<Int>()

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
        for (row in rows) {
            var rowStr = ""
            for (col in rows){
                rowStr +=  "${matrix[row][col]} "
            }
            println(rowStr)
        }
    }


}