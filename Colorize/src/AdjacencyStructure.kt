class EdgesAdjacencyStructure(
    private var nodesToEdges: Array<IntArray>,
    private var edgesToNodes: HashMap<Int, IntArray>,
    private var adjacencyDegree: Array<Int>) {

    val edgesNum: Int = edgesToNodes.size

    fun getAdjacentEdges(edge: Int): ArrayList<Int> {
        val adjacentEdges = mutableSetOf<Int>()
        var node: Int
        for (i in 0..1){
           node = this.edgesToNodes[edge]!![i]
           adjacentEdges.addAll(this.nodesToEdges[node].toList())
        }
        return adjacentEdges.toList().filter { it != -1 } as ArrayList<Int>
    }

    private fun getNullColumns(row: Int) : List<Int> {
        // Like a countingSort, but to generate the array O(n), n~=3
        val rowExpanded = Array<Int>(edgesNum){0}
        val nullColumns = ArrayList<Int>()

        getAdjacentEdges(row).forEach{ c ->
            rowExpanded[c] = 1
        }

        for (c in rowExpanded.indices){
            if(rowExpanded[c] == 0) nullColumns.add(c)
        }
        return nullColumns
    }

    private fun getNonNullColumns(edge: Int): ArrayList<Int> {
        return getAdjacentEdges(edge)
    }

    private fun getNonNullColumnsSizeSorted(edges : List<Int>): HashMap<Int, Int> {
        var startTime = System.currentTimeMillis()
        var mapSize = HashMap<Int, Int>()
        for (row in edges){
            mapSize[row] = adjacencyDegree[row]
        }

        val sortedMap = mapSize.toList().sortedBy {it.second}.toMap() as HashMap<Int, Int>
        //println("Time for getNonNullColumnsSizeSorted = ${System.currentTimeMillis() - startTime} ms")
        return sortedMap
    }

    fun getMaxNullMatrixRows(row: Int, alreadyProcessedRows: HashMap<Int, Boolean>): ArrayList<Int> {
        var startTime = System.currentTimeMillis()
        var nullCols = getNullColumns(row)
        var nonNullColSizeSorted = getNonNullColumnsSizeSorted(nullCols)
        var acceptedRows = arrayListOf(row)
        var rejectedRows: MutableMap<Int, Boolean> = alreadyProcessedRows.toMap().toMutableMap()

        nonNullColSizeSorted.keys.forEach{ col ->
            if (rejectedRows[col] == false){
                acceptedRows.add(col)
                getAdjacentEdges(col).forEach{ c -> rejectedRows[c] = true}
            }
        }

        //println("Time for getMaxNullMatrixRows($row) = ${System.currentTimeMillis() - startTime} ms")
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