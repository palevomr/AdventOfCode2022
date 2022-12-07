import java.io.File
import java.util.Stack

class Day7FileTree {
    companion object {

        const val TOTAL_SIZE = 70_000_000

        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_7")
            val stack = Stack<Folder>()
            var current: Folder? = null
            var totalSize = 0
            file.forEachLine {
                when {
                    it.startsWith("\$ cd") -> {
                        val name = it.takeLastWhile { it != ' ' }
                        if (name == "..") {
                            val folderSize = stack.pop().totalValue
                            if (folderSize < 100000) {
                                totalSize += folderSize
                            }
                            stack.peek().totalValue += folderSize
                        } else {
                            val lastFolder = if (stack.isEmpty()) null else stack.peek().children.firstOrNull { it.name == name }
                            if (lastFolder == null) {
                                stack.push(Folder(name, 0, ArrayList()))
                            } else {
                                stack.push(lastFolder)
                            }
                        }
                    }
                    it.startsWith("\$ ls") -> {
                        current = stack.peek()
                    }
                    it.startsWith("dir") -> {
                        current?.children?.add(Folder(it.takeLastWhile { it != ' ' }, 0, ArrayList()))
                    }
                    else -> {
                        val size = it.takeWhile { it != ' ' }.toInt()
                        current?.let {
                            it.totalValue += size
                        }
                    }
                }
            }

            while (stack.isNotEmpty()) {
                val folder = stack.pop()
                val folderSize = folder.totalValue
                if (folderSize < 100000) {
                    totalSize += folderSize
                }

                if (stack.isNotEmpty()) {
                    stack.peek().totalValue += folderSize
                }
            }
        }
    }
}

data class Folder(val name: String, var totalValue: Int, val children: ArrayList<Folder>)