import java.io.File

class Graph(filePath: String) {
    private var linksNum: Int
    private var nodesNum: Int
    private val links : Array<IntArray>
    private val colors: Array<IntArray>
    private val degrees: Array<Int>

    init {
        nodesNum = readNodeNums(filePath)
        linksNum = 0
        links = Array(nodesNum) { IntArray(nodesNum) {0} }
        colors = Array(nodesNum) { IntArray(nodesNum) {0} }
        degrees = Array<Int>(nodesNum){0}
        initializeFromFile(filePath)
        for (i in 1..nodesNum){
            this.degrees[i-1] = calculateNodeDegree(i)
        }
    }

    private fun readNodeNums(filePath: String): Int {
        val lines: List<String> = File(filePath).readLines()
        var lineVals: Array<String>
        var nodesNum = 0

        for (line: String in lines) {
            lineVals = line.split(" ").toTypedArray()
            if (lineVals[0] == "p") {
                nodesNum = lineVals[2].toInt()
                return nodesNum
            } else {
                continue
            }
        }
        return nodesNum
    }

    private fun initializeFromFile(filePath : String) {
        val lines : List<String> = File(filePath).readLines()
        var lineVals : Array<String>

        for ( line: String in lines ){
            lineVals = line.split(" ").toTypedArray()
            if (lineVals[0].equals("p")){
                nodesNum = lineVals[2].toInt()
            }
            else if (lineVals[0].equals("e")){
                this.addLink(lineVals[1].toInt(), lineVals[2].toInt())
            }
            else
                continue
        }
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

    fun getMaxNullMatrix(mainNode: Int): List<Int> {
        var nodeLinkedRelativeMapping = getSortedLinkedMappingRelativeToNode(mainNode)
        var acceptedNodes = arrayListOf(mainNode)
        var rejectedNodes = ArrayList<Int>()

        nodeLinkedRelativeMapping[mainNode]?.forEach { node ->
            if (!rejectedNodes.contains(node)){
                acceptedNodes.add(node)
                rejectedNodes.addAll(nodeLinkedRelativeMapping[node]!!)
            }
        }
        println("======= Null Matrix Size: " + acceptedNodes.size)
        return acceptedNodes
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
        linksNum += 2
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

}