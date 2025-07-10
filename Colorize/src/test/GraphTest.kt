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
            p col 19 49
            e 1 2
            e 1 3
            e 1 4
            e 1 5
            e 1 8
            e 1 13
            e 2 3
            e 2 4
            e 2 5
            e 2 7
            e 2 10
            e 3 4
            e 3 5
            e 3 9
            e 3 17
            e 4 5
            e 4 11
            e 4 12
            e 5 6
            e 5 19
            e 6 7
            e 6 14
            e 6 15
            e 6 19
            e 7 10
            e 7 14
            e 7 15
            e 8 9
            e 8 13
            e 8 15
            e 8 16
            e 9 15
            e 9 16
            e 9 17
            e 10 11
            e 10 16
            e 11 12
            e 11 16
            e 12 13
            e 12 14
            e 12 18
            e 13 14
            e 13 18
            e 14 15
            e 14 18
            e 15 16
            e 17 18
            e 17 19
            e 18 19 
       """.trimIndent()
    }

    fun getGraphStr(): String {
        return """
        p edge 5
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
        p edge 6
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
        p edge 5
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
        p edge 8
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
    fun `test getSortedLinkedMappingRelativeToNode multiple working tests`(){
        // In this mapping, the list of the main node has all nodes not linked to it
        // the other nodes contain all nodes linked to it that is not linked to the main node
        val nodeToTest = 1
        var mapping = graph1.getSortedLinkedMappingRelativeToNode(nodeToTest)
        var nodeOrder = mapping.keys.toList()

        assertTrue(mapping[4]?.containsAll(arrayListOf(5)) ?: false)
        assertTrue(mapping[5]?.containsAll(arrayListOf(4)) ?: false)
        assertTrue(mapping[nodeToTest]?.containsAll(arrayListOf(4, 5)) ?: false)
        assert(nodeOrder == listOf(4, 5, 1) )

        mapping = graphDense.getSortedLinkedMappingRelativeToNode(nodeToTest)
        assertTrue(mapping.get(nodeToTest)?.isEmpty() ?: false)

        mapping = graphSparce.getSortedLinkedMappingRelativeToNode(nodeToTest)
        nodeOrder = mapping.keys.toList()

        assertTrue(mapping[4]?.isEmpty() ?: false)
        assertTrue(mapping[5]?.isEmpty() ?: false)
        assertTrue(mapping[6]?.isEmpty() ?: false)
        assertTrue(mapping[nodeToTest]?.containsAll(arrayListOf(4, 5, 6)) ?: false)
        assertEquals(nodeOrder, listOf(4, 5, 6, 1))

        mapping = graphDisconnected.getSortedLinkedMappingRelativeToNode(nodeToTest)
        nodeOrder = mapping.keys.toList()

        assertTrue(mapping[1]?.containsAll(arrayListOf( 4, 5, 6, 7, 8)) ?: false)
        assertTrue(mapping[4]?.containsAll(arrayListOf( 5 )) ?: false)
        assertTrue(mapping[5]?.containsAll(arrayListOf(4)) ?: false)
        assertTrue(mapping[6]?.containsAll(arrayListOf(7)) ?: false)
        assertTrue(mapping[7]?.containsAll(arrayListOf(6, 8)) ?: false)
        assertTrue(mapping[8]?.containsAll(arrayListOf( 7 )) ?: false)
        assertEquals(nodeOrder, listOf(4, 5, 6, 8, 7, 1 ))
    }

    @Test
    fun `test getRelativeLinkedList check all vertices relative to the first`(){
        var expectedRelativeLinkedList = LinkedHashMap<Int, ArrayList<Int>>()
        expectedRelativeLinkedList[6] = arrayListOf(7, 14, 15, 19)
        expectedRelativeLinkedList[7] = arrayListOf(6, 10, 14, 15)
        expectedRelativeLinkedList[9] = arrayListOf(15, 16, 17)
        expectedRelativeLinkedList[10] = arrayListOf(7, 11, 16)
        expectedRelativeLinkedList[11] = arrayListOf(10, 12, 16)
        expectedRelativeLinkedList[12] = arrayListOf(11, 14, 18)
        expectedRelativeLinkedList[14] = arrayListOf(6, 7, 12, 15, 18)
        expectedRelativeLinkedList[15] = arrayListOf(6, 7, 9, 14, 16)
        expectedRelativeLinkedList[16] = arrayListOf(9, 10, 11, 15)
        expectedRelativeLinkedList[17] = arrayListOf(9, 18, 19)
        expectedRelativeLinkedList[18] = arrayListOf(12, 14, 17, 19)
        expectedRelativeLinkedList[19] = arrayListOf(6, 17, 18)

        val mainNodeNonLinkedNodes = graphArticle.getNonLinkedNodes(1)
        mainNodeNonLinkedNodes.forEach { n ->
            val linkedList = graphArticle.getLinkedListRelativeToNonLinkedListNode(1, n)
            assertTrue(linkedList.containsAll(expectedRelativeLinkedList[n]!!))
        }
    }

    @Test
    fun `test getMaxNullMatrix on node with known non-linked nodes`() {
        val nodeToTest = 1 // Example node, adjust as necessary
        var nodesThatFormMaxNullMatrix = graph1.getMaxNullMatrix(nodeToTest)

        val nonLinkedNodes = graph1.getNonLinkedNodes(nodeToTest)
        assertEquals(nodesThatFormMaxNullMatrix.size, 2)
        assertEquals(nodesThatFormMaxNullMatrix, listOf(1, 4))

        nodesThatFormMaxNullMatrix = graphArticle.getMaxNullMatrix(nodeToTest)
        assertEquals(nodesThatFormMaxNullMatrix.size, 5)
        assertEquals(nodesThatFormMaxNullMatrix, listOf(1, 6, 9, 10, 12))

        nodesThatFormMaxNullMatrix = graphArticle.getMaxNullMatrix(2)
        assertEquals(nodesThatFormMaxNullMatrix.size, 5)
        //assertEquals(nodesThatFormMaxNullMatrix, listOf(2, 11, 13, 15, 17)) -> result from article
        assertEquals(nodesThatFormMaxNullMatrix, listOf(2, 6, 8, 11, 17))

        nodesThatFormMaxNullMatrix = graphArticle.getMaxNullMatrix(3)
        assertEquals(nodesThatFormMaxNullMatrix.size, 5)
        assertEquals(nodesThatFormMaxNullMatrix, listOf(3, 6, 8, 10, 12))
    }

    @Test
    fun `test getMaxNullMatrix on a node with no non-linked nodes`() {
        val nodeToTest = 2 // Example node that might be fully connected in the graph
        graph1.getMaxNullMatrix(nodeToTest)

        val nonLinkedNodes = graph1.getNonLinkedNodes(nodeToTest)
        assertTrue(nonLinkedNodes.isEmpty(), "No non-linked nodes should exist for node $nodeToTest")
    }

    @Test
    fun `test getMaxNullMatrix on an isolated node`() {
        // Add an isolated node to the graph for testing purposes
        val isolatedNode = 6
        graph1.addLink(isolatedNode, isolatedNode)

        graph1.getMaxNullMatrix(isolatedNode)

        val nonLinkedNodes = graph1.getNonLinkedNodes(isolatedNode)
        assertEquals(graph1.getNodesNum() - 1, nonLinkedNodes.size, "Isolated node should not be linked to any other nodes")
    }
}