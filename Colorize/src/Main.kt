import java.io.File

fun main() {
    val filePath = "resources/dsjc250.5.col.txt"
    var graph : Graph? = Graph(filePath)

    println(message = "Nodes Num: ${graph?.getNodesNum()}")
    println(message = "Links Num: ${graph?.getLinksNum()}")
    println(message = "Degree node[1]: ${graph?.getNodeDegree(1)}")
    println(message = "Degree node[2]: ${graph?.getNodeDegree(2)}")
    println(message = "Degree node[3]: ${graph?.getNodeDegree(3)}")
}