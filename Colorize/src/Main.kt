fun main() {
    val filePath = "resources/dsjc250.5.col.txt"
    var graph : Graph = Graph(filePath)

    println(message = "Nodes Num: ${graph.getNodesNum()}")
    println(message = "Links Num: ${graph.getLinksNum()}")
    println(message = "Degree Node 1: ${graph.getNodeDegree(1)}")
    println(message = "Degree Node 2: ${graph.getNodeDegree(2)}")
    println(message = "Degree Node 3: ${graph.getNodeDegree(3)}")
    println(message = "Degree Node 4: ${graph.getNodeDegree(4)}")
    println(message = "Degree Node 5: ${graph.getNodeDegree(5)}")
    val maxDegreeNodes: List<Int> = graph.getMaxDegreeNodes()
    println(message = "MaxDegreeNodes: ${maxDegreeNodes} | MaxDegree: ${graph.getNodeDegree(maxDegreeNodes.get(0))}")
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
fun runColorizeAlgorithm() {
    val filePath = "resources/dsjc250.5.col.txt"
    var maxNullMatrixNodes = ArrayList<Int>()

    //Step 1: Construct an edge adjacency matrix for the given graph.
    var graph : Graph = Graph(filePath)

    //Step 2: Find the sum of the elements in each row of the matrix constructed in step 1. Select the row that has
    //the maximum value.
    val maxDegreeNodes: List<Int> = graph.getMaxDegreeNodes()

    //Case (a): If the maximum value is unique, then find the maximal null matrix formed by the zeros in the
    //selected row and go to step 3.
    if (maxDegreeNodes.size == 1){
        val maxDegreeNode = maxDegreeNodes.get(0)
        maxNullMatrixNodes = graph.getMaxNullMatrixIndexes(maxDegreeNode) as ArrayList<Int>
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
        var tmpNullMatrixNodes =  ArrayList<Int>()
        maxDegreeNodes.forEach { node ->
           tmpNullMatrixNodes = graph.getMaxNullMatrixIndexes(node) as ArrayList<Int>
            if (maxNullMatrixNodes.size == tmpNullMatrixNodes.size){
                val maxNullMatrixDegreeSum = graph.getSumOfNodeDegrees(maxNullMatrixNodes)
                val tmpNullMatrixDegreeSum = graph.getSumOfNodeDegrees(tmpNullMatrixNodes)
                if (tmpNullMatrixDegreeSum > maxNullMatrixDegreeSum){
                    maxNullMatrixNodes = tmpNullMatrixNodes
                }

            }
            if (tmpNullMatrixNodes.size > maxNullMatrixNodes.size){
                maxNullMatrixNodes = tmpNullMatrixNodes
            }
        }
    }
    //Step 4: Assign a color to the edges corresponding to the rows of the identified maximal null matrix obtained
    //in step 3 and go to step 5.

    //Just realized that I was using the nodes when I was supposed to use the vertices. Need to FIX IT!!!



}