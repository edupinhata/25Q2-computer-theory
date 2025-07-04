import java.io.File

class Graph(filePath: String) {
    private var linksNum: Int
    private var nodesNum: Int
    private val links : Array<IntArray>
    private val colors: Array<IntArray>
    private val degrees: Array<Int>

    init {
        nodesNum = readNodeNums(filePath)
        links = Array(nodesNum) { IntArray(nodesNum) {0} }
        colors = Array(nodesNum) { IntArray(nodesNum) {0} }
        degrees = Array<Int>(nodesNum){0}
        initializeFromFile(filePath)
        for (i in 1..nodesNum){
            this.degrees[i-1] = calculateNodeDegree(i)
        }
        linksNum = 0
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


    fun colorize(node1 : Int, node2 : Int, color: Int) {
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