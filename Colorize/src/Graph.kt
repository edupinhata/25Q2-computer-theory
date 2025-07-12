import java.io.File

class Graph(filePath: String) {
    private var linksNum: Int = 0
    private var nodesNum: Int = 0
    private val links : Array<IntArray>
    private val nodesToEdge : Array<IntArray>
    private val adjacence : Array<IntArray>
    private val colors: Array<IntArray>
    private val degrees: Array<Int>
    private val adjacenceDegree: Array<Int>

    init {
        initializeNodeNums(filePath)
        links = Array(nodesNum) { IntArray(nodesNum) {0} }
        nodesToEdge = Array(nodesNum) {IntArray(nodesNum) {-1} }
        adjacence = Array(linksNum) { IntArray(linksNum) {0} }
        colors = Array(nodesNum) { IntArray(nodesNum) {0} }
        degrees = Array<Int>(nodesNum){0}
        adjacenceDegree = Array<Int>(nodesNum*nodesNum){0}
        initializeLinksFromFile(filePath)
        initializeAdjacenceMatrix()
        for (i in 1..nodesNum){
            this.degrees[i-1] = calculateNodeDegree(i)
        }

        for (i in 0..linksNum-1){
            this.adjacenceDegree[i] = adjacence[i].reduce { acc, adj -> acc + adj}
        }
    }

    private fun initializeNodeNums(filePath: String) {
        val lines: List<String> = File(filePath).readLines()
        var lineVals: Array<String>

        for (line: String in lines) {
            lineVals = line.split(" ").toTypedArray()
            if (lineVals[0] == "p") {
                this.nodesNum = lineVals[2].toInt()
                this.linksNum = lineVals[3].toInt()/2
                return
            } else {
                continue
            }
        }
    }


    private fun initializeLinksFromFile(filePath : String) {
        val lines : List<String> = File(filePath).readLines()
        var lineVals : Array<String>

        for ( line: String in lines ){
            lineVals = line.split(" ").toTypedArray()

            if (lineVals[0].equals("e")){
                this.addLink(lineVals[1].toInt(), lineVals[2].toInt())
            }
            else
                continue
        }
    }

    private fun initializeAdjacenceMatrix() {
        var curEdge = 0
        for (node1 in 0..nodesNum-1) {
            for (node2 in node1..nodesNum - 1) {
                if (links[node1][node2] == 1){
                    nodesToEdge[node1][node2] = curEdge
                    nodesToEdge[node2][node1] = curEdge
                    curEdge++
                }
            }
        }

        for (node1 in 1..nodesNum){
            val linkedNodes = getLinkedNodes(node1)
            for (node2 in linkedNodes){
                val edge1 = getEdgeIndex(node1, node2)
                for (node3 in linkedNodes){
                    if (node2 != node3){
                        val edge2 = getEdgeIndex(node1, node3)
                        adjacence[edge1][edge2] = 1
                        adjacence[edge2][edge1] = 1
                    }
                }
            }
        }
    }

    public fun getAllNodesIndexes(): ArrayList<Int> {
        val nodes = ArrayList<Int>()
        for (i in 1..nodesNum){
            nodes.add(i-1)
        }
        return nodes
    }

    public fun getAllEdgesIndexes(): ArrayList<Int> {
        val edges = ArrayList<Int>()
        for (i in 0..linksNum-1){
            edges.add(i)
        }
        return edges
    }


    public fun getEdgeIndex(node1 : Int, node2: Int): Int {
        /*

         */
        validateNodes(node1, node2)
        return nodesToEdge[node1-1][node2-1]
    }

    public fun getAdjacenceMatrix() : Array<IntArray>{
        return this.adjacence
    }

    fun colorize(node1: Int, node2: Int, color: Int) {
        validateNodes(node1, node2)
        colors[node1-1][node2-1] = color
        colors[node2-1][node1-1] = color
    }

    fun isColored(node1 : Int, node2 : Int): Boolean {
        validateNodes(node1, node2)
        return colors[node1-1][node2-1] != 0 || colors[node2-1][node1-1] != 0
    }

    fun getNodeDegree(node: Int): Int {
        validateNode(node)
        return this.degrees[node-1]
    }

    fun getAdjacenceDegree(node1: Int, node2: Int): Int {
        val edge = getEdgeIndex(node1, node2)
        return getAdjacenceDegree(edge)
    }

    fun getAdjacenceDegree(edge: Int): Int{
        validateEdge(edge)
        return this.adjacenceDegree[edge]
    }

    fun getMaxDegreeNodes(): List<Int>{
        return getMaxDegreeNodesBetween(1, this.nodesNum)
    }

    fun getMaxDegreeNodesBetween(node1: Int, node2: Int) : List<Int> {
        validateNodes(node1, node2)
        var maxDegreeNodes = ArrayList<Int>()

        var minNode = node1
        var maxNode = node2
        if (node1 > node2) {
            minNode = node2
            maxNode = node1
        }
        var maxDegree = 0

        for (i in minNode..maxNode){
            if (getNodeDegree(i) == maxDegree){
                maxDegreeNodes.add(i)
            }
            if (getNodeDegree(i) > maxDegree){
               maxDegree =  getNodeDegree(i)
               maxDegreeNodes = arrayListOf(i)
            }
        }
        return maxDegreeNodes
    }

    fun getLinkedNodes(node: Int): ArrayList<Int>{
        var linkedNodes =  ArrayList<Int>()
        for (i: Int in 1..nodesNum){
            if (links[node-1][i-1] == 1 && i != node){
                linkedNodes.add(i)
            }
        }
        return linkedNodes
    }

    fun getNonLinkedNodes(node: Int): ArrayList<Int>{
        var nonLinkedNodes =  ArrayList<Int>()
        for (i: Int in 1..nodesNum){
            if (links[node-1][i-1] == 0 && i != node){
                nonLinkedNodes.add(i)
            }
        }
        return nonLinkedNodes
    }

    fun getNonLinkedNodesWithinNodes(node: Int, nodesToExplore: ArrayList<Int>): List<Int> {
        var nonLinkedNodes = getNonLinkedNodes(node)
        return nonLinkedNodes.filter { n ->  nodesToExplore.contains(n) }
    }

    fun getLinkedNodesWithinNodes(node: Int, nodesToExplore: ArrayList<Int>): List<Int> {
        var linkedNodes = getLinkedNodes(node)
        return linkedNodes.filter { n -> nodesToExplore.contains(n) }
    }

    fun getSortedLinkedMappingRelativeToNode(relativeNode: Int): HashMap<Int, List<Int>> {
        var mainNodeNonLinkedNodes = getNonLinkedNodes(relativeNode)
        var linkedNodesRelativeMap: HashMap<Int, List<Int>> = HashMap<Int, List<Int>>()
        linkedNodesRelativeMap[relativeNode] = mainNodeNonLinkedNodes

        for (node in mainNodeNonLinkedNodes){
            linkedNodesRelativeMap[node] = getLinkedListRelativeToNonLinkedListNode(relativeNode, node)
        }

        return linkedNodesRelativeMap.entries
            .sortedWith ( compareBy(
                {it.value.size}, {it.key}))
            .associate { it.toPair() }
            .toMap(LinkedHashMap())
    }

    fun getLinkedListRelativeToNonLinkedListNode(nonLinkedNode: Int, nodeToGetLinkedList: Int): List<Int> {
        val mainNonLinkedList = getNonLinkedNodes(nonLinkedNode)
        val linkedList = getLinkedNodes(nodeToGetLinkedList)
       return mainNonLinkedList.filter { node -> linkedList.contains(node)}
    }

    fun getMaxNullMatrixIndexes(mainNode: Int): List<Int> {
        validateNode(mainNode)
        val matrixUtil = intSymmetricMatrixUtil(this.adjacence)
        var nullMatrixIndexes = matrixUtil.getMaxNullMatrixRows(mainNode-1)
        matrixUtil.printMatrix(nullMatrixIndexes)
        return nullMatrixIndexes
    }

    fun getNodesNum() : Int {
        return nodesNum
    }

    fun getLinksNum() : Int {
        return linksNum
    }

    fun addLink(node1 : Int, node2: Int){
        validateNodes(node1, node2)
        links[node1-1][node2-1] = 1
        links[node2-1][node1-1] = 1
    }

    fun getSumOfNodeDegrees(nodes: List<Int>) : Int {
        var sumDegree : Int = 0
        nodes.forEach { node ->
           sumDegree += calculateNodeDegree(node)
        }
        return sumDegree
    }

    private fun calculateNodeDegree(node: Int) : Int {
        validateNode(node)
        return links[node-1].reduce { acc, adj -> acc + adj}
    }

    private fun validateNode(node: Int){
        require(node in 1 until nodesNum+1) { "Node $node not a valid node"}
    }

    private fun validateNodes(node1: Int, node2: Int){
        validateNode(node1)
        validateNode(node2)
    }

    public fun validateEdge(edge: Int){
        require(edge in 0 until linksNum) { "Edge $edge not a valid edge"}
    }

}