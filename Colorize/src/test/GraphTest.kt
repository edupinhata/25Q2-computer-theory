import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class GraphTest {

    private lateinit var graph1: Graph
    private lateinit var graphArticle: Graph
    private lateinit var graphSparce: Graph
    private lateinit var graphDense: Graph
    private lateinit var graphDisconnected: Graph

    @BeforeEach
    fun setup() {
        graph1 = initializeTestGraph(getGraphStr())
        graphArticle = initializeTestGraph(getArticleGraph())
        graphSparce = initializeTestGraph(getSparseGraphStr())
        graphDense = initializeTestGraph(getDenseGraphStr())
        graphDisconnected = initializeTestGraph(getDisconnectedGraphStr())
    }

    fun getArticleGraph(): String{
       return """
            p col 11 38
            e 1 5
            e 1 6
            e 1 7
            e 1 8
            e 1 11
            e 2 5
            e 2 6
            e 2 9
            e 2 10
            e 3 5
            e 3 8
            e 3 9
            e 4 6
            e 4 7
            e 4 10
            e 7 9
            e 8 10
            e 9 11
            e 10 11
       """.trimIndent()
    }

    fun getGraphStr(): String {
        return """
        p edge 5 6
        e 1 2
        e 1 3
        e 4 5
        """.trimIndent()
    }

    fun getSparseGraphStr(): String {
       /*
         1 -- 2
           \
            3

          Nodes 4, 5, 6 have no links.
        */
        return """
        p edge 6 4
        e 1 2
        e 1 3
        """.trimIndent()
    }

    fun getDenseGraphStr(): String{
        /*
        Almost every node is connected to each other
            5
           /  \
          1 -- 2
          | \/ |
          | /\ |
          3 -- 4
           \  /
            5
         */
        return """
        p edge 5 20
        e 1 2
        e 1 3
        e 1 4
        e 1 5
        e 2 3
        e 2 4
        e 2 5
        e 3 4
        e 3 5
        e 4 5
        """.trimIndent()
    }

    fun getDisconnectedGraphStr(): String {
       /*
      Subgraph 1:
          1 -- 2
           \  /
            3

          Subgraph 2:
          4 -- 5

          Subgraph 3:
          6 -- 7 -- 8
        */
        return """
        p edge 8 12
        e 1 2
        e 2 3
        e 3 1
        e 4 5
        e 6 7
        e 7 8
        """.trimIndent()
    }

    fun initializeTestGraph(graphFile: String): Graph{
        val tempFile = File.createTempFile("temp", ".txt")
        tempFile.writeText(graphFile)
        val graph = Graph(tempFile.absolutePath)
        tempFile.deleteOnExit()
        return graph
    }

    @Test
    fun `test getNonLinkedNodes on node with known non-linked nodes`(){
       val nodeTotest = 1
       var nonLinkedNodes = graph1.getNonLinkedNodes(nodeTotest)
        assertTrue(nonLinkedNodes.containsAll(arrayListOf(4, 5)))
        assertFalse(nonLinkedNodes.contains(1))
        assertFalse(nonLinkedNodes.contains(2))
        assertFalse(nonLinkedNodes.contains(3))


        nonLinkedNodes = graphSparce.getNonLinkedNodes(nodeTotest)
        assertTrue(nonLinkedNodes.containsAll(arrayListOf(4, 5, 6)))
        assertFalse(nonLinkedNodes.contains(1))
        assertFalse(nonLinkedNodes.contains(2))
        assertFalse(nonLinkedNodes.contains(3))

        nonLinkedNodes = graphDense.getNonLinkedNodes(nodeTotest)
        assertTrue(nonLinkedNodes.isEmpty())
    }

    @Test
    fun `test getLinkedNodes on node with known linked nodes`(){
        val nodeTotest = 1
        var linkedNodes = graph1.getLinkedNodes(nodeTotest)
        assertTrue(linkedNodes.containsAll(arrayListOf(2, 3)))
        assertFalse(linkedNodes.contains(1))
        assertFalse(linkedNodes.contains(4))
        assertFalse(linkedNodes.contains(5))


        linkedNodes = graphSparce.getLinkedNodes(nodeTotest)
        assertTrue(linkedNodes.containsAll(arrayListOf(2, 3)))
        assertFalse(linkedNodes.contains(4))
        assertFalse(linkedNodes.contains(5))
        assertFalse(linkedNodes.contains(6))

        linkedNodes = graphDense.getLinkedNodes(nodeTotest)
        assertTrue(linkedNodes.containsAll(arrayListOf(2, 3, 4, 5)))
    }

    @Test
    fun `test getMaxNullMatrix on node with known non-linked nodes`() {
        val nodeToTest = 1 // Example node, adjust as necessary

        var rowsOfMaxNullAdjacenceMatrix = graph1.getAdjacenceMaxNullMatrixIndexes(nodeToTest)
        assertEquals(rowsOfMaxNullAdjacenceMatrix.size, 2)
        assertEquals(rowsOfMaxNullAdjacenceMatrix, listOf(0, 2))

        rowsOfMaxNullAdjacenceMatrix = graphArticle.getAdjacenceMaxNullMatrixIndexes(nodeToTest)
        assertEquals(rowsOfMaxNullAdjacenceMatrix.size, 5)

        rowsOfMaxNullAdjacenceMatrix = graphArticle.getAdjacenceMaxNullMatrixIndexes(2)
        assertEquals(rowsOfMaxNullAdjacenceMatrix.size, 5)

        rowsOfMaxNullAdjacenceMatrix = graphArticle.getAdjacenceMaxNullMatrixIndexes(3)
        assertEquals(rowsOfMaxNullAdjacenceMatrix.size, 5)
    }
}