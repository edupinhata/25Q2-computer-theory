import java.io.File
import java.text.DecimalFormat

class Graph(filePath: String) {
    // TODO: nodes starts with 1, while edges with 0.
    private var linksNum: Int = 0
    private var nodesNum: Int = 0
    private val links : Array<IntArray>
    // TODO: transform links in a HashMap to improve performance
    private val nodesToEdge : Array<IntArray>
    private val edgeColors: Array<Int>
    private val degrees: Array<Int>
    private lateinit var edgesToNodes: HashMap<Int, IntArray>
    private lateinit var adjacency :EdgesAdjacencyStructure
    private val adjacencyDegree: Array<Int>
    private val adjacencyProcessed: HashMap<Int, Boolean>

    init {
        initializeNodeNums(filePath)
        links = Array(nodesNum) { IntArray(nodesNum) {0} }
        nodesToEdge = Array(nodesNum) {IntArray(nodesNum) {-1} }
        edgeColors = Array<Int>(linksNum){0}
        degrees = Array<Int>(nodesNum){0}
        adjacencyDegree = Array<Int>(nodesNum*nodesNum){0}
        adjacencyProcessed = HashMap<Int, Boolean>()
        initializeLinksFromFile(filePath)
        initializeAdjacencyStructure()
    }

    private fun initializeNodeNums(filePath: String) {
        println("Initializing nodes structure ...")
        val lines: List<String> = File(filePath).readLines()
        var lineVals: Array<String>

        for (line: String in lines) {
            lineVals = line.split(" ").toTypedArray()
            if (lineVals[0] == "p") {
                this.nodesNum = lineVals[2].toInt()
                this.linksNum = lineVals[3].toInt()
                return
            } else {
                continue
            }
        }
    }

    private fun initializeLinksFromFile(filePath : String) {
        println("Initializing edges structure ...")
        val lines : List<String> = File(filePath).readLines()
        var lineVals : Array<String>
        var edges = 0

        for ( line: String in lines ){
            lineVals = line.split(" ").toTypedArray()

            if (lineVals[0].equals("e")){
                this.addLink(lineVals[1].toInt(), lineVals[2].toInt())
                edges++
            }
            else
                continue
        }
        this.linksNum = edges
    }

    private fun initializeAdjacencyStructure() {
        println("Initializing adjacency structure structure ...")
        var curEdge = 0
        var formatter = DecimalFormat("#0.00")
        this.edgesToNodes = HashMap<Int, IntArray>()

        for (node1 in 0 until nodesNum) {
            print("Loading nodes to edge: ${formatter.format(node1/nodesNum.toDouble() * 100)}%\r")
            for (node2 in node1 until nodesNum) {
                if (links[node1][node2] == 1){
                    nodesToEdge[node1][node2] = curEdge
                    nodesToEdge[node2][node1] = curEdge
                    edgesToNodes[curEdge] = intArrayOf(node1, node2)
                    curEdge++
                }
            }
        }

        for (i in 1..nodesNum){
            this.degrees[i-1] = calculateNodeDegree(i)
        }

        for (i in 0..linksNum-1){
            this.adjacencyDegree[i] = calculateAdjacencyDegree(i, nodesToEdge, edgesToNodes)
            this.adjacencyProcessed[i] = false
        }

        this.adjacency = EdgesAdjacencyStructure(nodesToEdge, edgesToNodes, adjacencyDegree)
    }


    fun calculateAdjacencyDegree(edge: Int, nodesToEdges: Array<IntArray>, edgesToNodes:HashMap<Int, IntArray>): Int {
        val adjacentEdges = mutableSetOf<Int>()
        var node: Int
        for (i in 0..1){
            node = this.edgesToNodes[edge]!![i]
            adjacentEdges.addAll(nodesToEdges[node].toList())
        }
        return adjacentEdges.toList().filter { it != -1 }.size
    }

    fun getAllNodesIndexes(): ArrayList<Int> {
        val nodes = ArrayList<Int>()
        for (i in 1..nodesNum){
            nodes.add(i-1)
        }
        return nodes
    }

    fun getAllEdgesIndexes(): ArrayList<Int> {
        val edges = ArrayList<Int>()
        for (i in 0..linksNum-1){
            edges.add(i)
        }
        return edges
    }

    fun getEdgeIndex(node1 : Int, node2: Int): Int {
        validateNodes(node1, node2)
        return nodesToEdge[node1-1][node2-1]
    }

    fun setAdjacencyProcessed(edges: List<Int>){
        edges.forEach { edge ->
            this.adjacencyProcessed[edge] = true
        }
    }

    fun getNumAdjacencyProcessed() : Int {
        return this.adjacencyProcessed.values.filter { v -> v }.size
    }

    fun colorize(edge: Int, color: Int) {
        validateEdge(edge)
        edgeColors[edge] = color
    }

    fun isColored(edge : Int): Boolean {
        validateEdge(edge)
        return edgeColors[edge] != 0
    }

    fun hasUncoloredEdges(): Boolean {
        for (i in 0..linksNum-1){
            if (edgeColors[i] == 0){
                return true
            }
        }
        return false
    }

    fun getColorMapping(): HashMap<Int, ArrayList<Int>> {
        var lastColor = 0
        var colorMapping = HashMap<Int, ArrayList<Int>>()

        for (i in 0..linksNum-1){
            if (edgeColors[i] > lastColor){
                lastColor = edgeColors[i]
            }
        }

        for (c in 0..lastColor){
            colorMapping[c] = ArrayList<Int>()
        }

        for (i in 0..linksNum-1){
            if (edgeColors[i] != 0){
                colorMapping[edgeColors[i]]!!.add(i)
            }
        }
        return colorMapping
    }

    fun printColoredEdges() {
        val colorMapping = getColorMapping()

        //print mapping
        for (c in 0..<colorMapping.size){
            if (colorMapping[c]!!.size > 0){
                println("Color $c: ${colorMapping[c]!!.joinToString(", ")}")
            }
            else{
                println("Color $c: empty")
            }
        }
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
        return this.adjacencyDegree[edge]
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


    fun getAdjacenceMaxNullMatrixIndexes(edge: Int): List<Int> {
        validateEdge(edge)
        var nullMatrixIndexes = this.adjacency.getMaxNullMatrixRows(edge, this.adjacencyProcessed)
        return nullMatrixIndexes
    }

    fun getAdjacentSumOfDegrees(edges : List<Int>) : Int{
        var sumDegrees : Int = 0
        edges.forEach { edge ->
            sumDegrees += getAdjacenceDegree(edge)
        }
        return sumDegrees
    }

    fun getAdjacentMaxDegreeEdges(): ArrayList<Int> {
        var maxDegree = 0
        var maxDegreeEdges = ArrayList<Int>()
        var adjacencyProcessable = adjacencyProcessed.keys.filter { e -> adjacencyProcessed[e] == false }
        for (edge in adjacencyProcessable){
            var curEdgeDegree = getAdjacenceDegree(edge)
            if (curEdgeDegree == maxDegree){
                maxDegreeEdges.add(edge)
            }
            else if (curEdgeDegree > maxDegree){
                maxDegree = curEdgeDegree
                maxDegreeEdges = arrayListOf(edge)
            }
        }
        return maxDegreeEdges
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

    private fun validateEdge(edge: Int){
        require(edge in 0 until linksNum) { "Edge $edge not a valid edge"}
    }


    fun getNonLinkedNodesWithinNodes(node: Int, nodesToExplore: ArrayList<Int>): List<Int> {
        var nonLinkedNodes = getNonLinkedNodes(node)
        return nonLinkedNodes.filter { n ->  nodesToExplore.contains(n) }
    }

    fun getLinkedNodesWithinNodes(node: Int, nodesToExplore: ArrayList<Int>): List<Int> {
        var linkedNodes = getLinkedNodes(node)
        return linkedNodes.filter { n -> nodesToExplore.contains(n) }
    }

    fun validateColorizeSolution(): Boolean {
        val colorMapping = getColorMapping()
        for (c in 0..colorMapping.size-1){
            val edges = colorMapping[c]!!
            for (i in 0..<edges.size-1){
                for (j in i+1..<edges.size){
                    if (adjacency.getAdjacentEdges(edges[i])!!.contains(edges[j])){
                        println("Error: edge ${edges[i]} is adjacent to edge ${edges[j]} and has the same color $c")
                        return false
                    }
                }
            }
        }
        return true
    }
}