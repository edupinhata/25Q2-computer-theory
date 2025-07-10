import java.io.File

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
    val maxDegreeNode: Int = graph.getMaxDegreeNode()
    println(message = "MaxDegreeNode: ${maxDegreeNode} | MaxDegree: ${graph.getNodeDegree(maxDegreeNode)}")
}