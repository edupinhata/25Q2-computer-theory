fun main() {
    val fileName = "resources/dsjc250.5.col.txt"
    runColorizeAlgorithm(fileName)
}

/*
    Step 1: Construct an edge adjacency matrix for the given graph.
    Step 2: Find the sum of the elements in each row of the matrix constructed in step 1. Select the row that has
    the maximum value.
    Case (a): If the maximum value is unique, then find the maximal null matrix formed by the zeros in the
    selected row and go to step 3.
    Case (b): If there is a tie in the maximum value , select all those rows and find all maximal null matrices
    formed by the zeros in the corresponding selected rows then select the largest null matrix among all maximal
    null matrices and then go to step 3.
    Step 3: Check the uniqueness of the null matrix.
    Case (a): If the maximal null matrix selected in step 2 is unique then go to step 4.
    Case (b): If there is a tie in the largest null matrix selected in step 2, find the degree sum of all the edges
    associated with the rows of each largest null matrix, then choose the null matrix corresponding to the
    maximum degree sum.
     If it is unique, then go to step 4.
     If there is a tie in the maximum degree sum, then select any one largest null matrix arbitrarily
    among the tie and go to step 4.
    Step 4: Assign a color to the edges corresponding to the rows of the identified maximal null matrix obtained
    in step 3 and go to step 5.
    Step 5: Remove all the rows and columns associated with the colored edges, then go to step 2 and repeat the
    process until all the edges have been colored.

 */
fun runColorizeAlgorithm(fileName: String) {
    var maxNullMatrixIndexes = ArrayList<Int>()
    var color = 1
    var maxOverallDegree = 0

    //Step 1: Construct an edge adjacency matrix for the given graph.
    var graph : Graph = Graph(fileName)

    while (graph.hasUncoloredEdges()) {
        println("Color numbers: ${color-1} | Max Overall Degree: $maxOverallDegree | Already processed edges: ${graph.getNumAdjacencyProcessed()}")
        //Step 2: Find the sum of the elements in each row of the matrix constructed in step 1. Select the row that has
        //the maximum value.
        println("Finding max degree edges")
        val maxDegreeEdges: ArrayList<Int> = graph.getAdjacentMaxDegreeEdges()
        if (maxOverallDegree == 0){
            maxOverallDegree = maxDegreeEdges[0]
        }

        //Case (a): If the maximum value is unique, then find the maximal null matrix formed by the zeros in the
        //selected row and go to step 3.
        if (maxDegreeEdges.size == 1) {
            val maxDegreeEdge = maxDegreeEdges[0]

            println("Unique max degree edge found: $maxDegreeEdge. Forming maximal null matrix.")
            maxNullMatrixIndexes = graph.getAdjacenceMaxNullMatrixIndexes(maxDegreeEdge) as ArrayList<Int>
        }
        //Case (b): If there is a tie in the maximum value , select all those rows and find all maximal null matrices
        //formed by the zeros in the corresponding selected rows then select the largest null matrix among all maximal
        //null matrices and then go to step 3.
        //Step 3: Check the uniqueness of the null matrix.
        //Case (a): If the maximal null matrix selected in step 2 is unique then go to step 4.
        //Case (b): If there is a tie in the largest null matrix selected in step 2, find the degree sum of all the edges
        //associated with the rows of each largest null matrix, then choose the null matrix corresponding to the
        //maximum degree sum.
        // If it is unique, then go to step 4.
        // If there is a tie in the maximum degree sum, then select any one largest null matrix arbitrarily
        else {
            println("Multiple max degree edges found. Forming maximal null matrices.")
            var tmpNullMatrixIndexes = ArrayList<Int>()
            maxDegreeEdges.forEach { node ->
                tmpNullMatrixIndexes = graph.getAdjacenceMaxNullMatrixIndexes(node) as ArrayList<Int>
                if (maxNullMatrixIndexes.size == tmpNullMatrixIndexes.size) {
                    val maxNullMatrixDegreeSum = graph.getAdjacentSumOfDegrees(maxNullMatrixIndexes)
                    val tmpNullMatrixDegreeSum = graph.getAdjacentSumOfDegrees(tmpNullMatrixIndexes)
                    if (tmpNullMatrixDegreeSum > maxNullMatrixDegreeSum) {
                        maxNullMatrixIndexes = tmpNullMatrixIndexes
                    }
                } else if (tmpNullMatrixIndexes.size > maxNullMatrixIndexes.size) {
                    maxNullMatrixIndexes = tmpNullMatrixIndexes
                }
            }
        }
        //Step 4: Assign a color to the edges corresponding to the rows of the identified maximal null matrix obtained
        //in step 3 and go to step 5.
        println("Assigning colors to maximal null matrix.")
        maxNullMatrixIndexes.forEach { index -> graph.colorize(index, color) }
        color++

        println("Recording already processed edges.")
        //Step 5: Remove all the rows and columns associated with the colored edges, then go to step 2 and repeat the
        //process until all the edges have been colored.
        graph.setAdjacencyProcessed(maxNullMatrixIndexes)
        maxNullMatrixIndexes.clear()

        if (color > maxOverallDegree) {
            println("Error: there are more colors than it should be");
            return;
        }
    }

    graph.printColoredEdges()
}