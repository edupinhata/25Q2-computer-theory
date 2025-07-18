import java.text.DecimalFormat

fun main() {
    val fileNames = arrayOf(
    //"resources/dsjc250.5.col.txt",  // OK
    //"resources/dsjc500.1.col.txt",  // OK
    //"resources/dsjc500.5.col.txt",  // OK
    //"resources/dsjc500.9.col.txt",  // OK
    //"resources/dsjc1000.1.col.txt", // OK
    //"resources/dsjc1000.5.col.txt", // OK
    "resources/dsjc1000.9.col.txt",  // Memory problem
    //"resources/dsjr500.1c.col.txt", // OK
    //"resources/dsjr500.5.col.txt",  //OK
    //"resources/flat300_28_0.col.txt",  //OK
    //"resources/flat1000_50_0.col.txt",  //OK
    //"resources/flat1000_60_0.col.txt",  //OK
    //"resources/flat1000_76_0.col.txt"   //OK
    //"resources/latin_square.col.txt",  // memory
    //"resources/le450_25c.col.txt",     //OK
    //"resources/le450_25d.col.txt",    //OK
    //"resources/r250.5.col.txt",       //OK
    //"resources/r1000.1c.col.txt",     //OK
    //"resources/r1000.5.col.txt",      //OK
    //"resources/C2000.5.col",  //Memory
    //"resources/C4000.5.col",   //memory
    )
    for (fileName in fileNames) {
        try {
            println("Processing file: $fileName")
            runColorizeAlgorithm(fileName)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
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
    var startTime = System.currentTimeMillis()
    var formatter = DecimalFormat("#0.00")

    //Step 1: Construct an edge adjacency matrix for the given graph.
    var graph : Graph = Graph(fileName)
    var edgesNum = graph.getLinksNum()

    while (graph.hasUncoloredEdges()) {
        var adjacenceProcessed = graph.getNumAdjacencyProcessed()
        var processedPercentage: Double = adjacenceProcessed.toDouble()/edgesNum.toDouble()
        var formattedPercentage = formatter.format(processedPercentage*100)
        var processedSymbols = ""
        var unprocessedSymbols = ""
        for (i in 0..(processedPercentage*40).toInt()) {processedSymbols += "-"}
        for (i in 0..((1-processedPercentage)*40).toInt()) {unprocessedSymbols += " "}
        var percentageBlockStr = "|${processedSymbols}${unprocessedSymbols}| ${(formattedPercentage)}%"
        var edgesProcessedRatioStr = "${adjacenceProcessed}/${edgesNum}"
        var elapsedTimeStr = "${System.currentTimeMillis() - startTime} ms"
        print("$percentageBlockStr | Processed: $edgesProcessedRatioStr | Colors: ${color} | Elapsed: $elapsedTimeStr\r")
        //println("Color numbers: ${color-1} | Max Overall Degree: $maxOverallDegree | Already processed edges: ${graph.getNumAdjacencyProcessed()}")

        //Step 2: Find the sum of the elements in each row of the matrix constructed in step 1. Select the row that has
        //the maximum value.
        //println("Finding max degree edges")
        val maxDegreeEdges: ArrayList<Int> = graph.getAdjacentMaxDegreeEdges()
        if (maxOverallDegree == 0){
            maxOverallDegree = maxDegreeEdges[0]
        }

        //Case (a): If the maximum value is unique, then find the maximal null matrix formed by the zeros in the
        //selected row and go to step 3.
        if (maxDegreeEdges.size == 1) {
            val maxDegreeEdge = maxDegreeEdges[0]

            //println("Unique max degree edge found: $maxDegreeEdge. Forming maximal null matrix.")
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
            //println("Multiple max degree edges found. Forming maximal null matrices.")
            var tmpNullMatrixIndexes = ArrayList<Int>()
            maxDegreeEdges.forEach { edge ->
                tmpNullMatrixIndexes = graph.getAdjacenceMaxNullMatrixIndexes(edge) as ArrayList<Int>
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
        //println("Assigning colors to maximal null matrix.")
        maxNullMatrixIndexes.forEach { index -> graph.colorize(index, color) }
        color++

        //println("Recording already processed edges.")
        //Step 5: Remove all the rows and columns associated with the colored edges, then go to step 2 and repeat the
        //process until all the edges have been colored.
        graph.setAdjacencyProcessed(maxNullMatrixIndexes)
        maxNullMatrixIndexes.clear()

        if (color > maxOverallDegree) {
            println("Error: there are more colors than it should be");
            return;
        }
    }

    println("\n----------------------------------------")
    println("File processed: $fileName")
    println("Time consumed: ${System.currentTimeMillis() - startTime} ms")
    println("Number of colors: $color")
    //println("----------------------------------------")
    //graph.printColoredEdges()
    println("----------------------------------------")
    println("Is solution valid? ${graph.validateColorizeSolution()}")
    println("----------------------------------------")
}