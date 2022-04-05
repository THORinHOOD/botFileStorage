package com.thorinhood.fileStorageBot.chatBotEngine.processors

import java.lang.Integer.min

class Tree(
    private val key: String,
    val processors: List<BaseProcessor>,
    private val tree: Map<String, Tree>
) {

    fun getProcessorsByProcSpace(procSpace: String) : List<BaseProcessor> =
        getProcessorsByProcSpace(procSpace, procSpace)

    private fun getProcessorsByProcSpace(originalProcSpace: String, procSpace: String) : List<BaseProcessor> {
        val result = mutableListOf<BaseProcessor>()
        if (key == "") {
            result.addAll(processors)
        }
        if (procSpace == "") {
            result.addAll(processors)
        } else {
            var sepIndex = procSpace.indexOf("#")
            if (sepIndex == -1) {
                sepIndex = procSpace.length
            }
            val currentKey = procSpace.substring(0, sepIndex)
            if (!tree.containsKey(currentKey)) {
                throw IllegalArgumentException("Can't find processor for $originalProcSpace")
            }
            result.addAll(
                tree[currentKey]!!.getProcessorsByProcSpace(
                    originalProcSpace,
                    procSpace.substring(min(sepIndex + 1, procSpace.length))
                )
            )
        }
        return result
    }

    companion object {
        fun createTree(processors: List<BaseProcessor>) : Tree {
            val (inThisNode, other) = processors.partition { processor -> processor.procSpace == "" }
            return Tree(
                "",
                inThisNode,
                createTreeMap(other, 1)
            )
        }

        private fun createTree(treeNode: TreeNode) : Tree {
            val key = treeNode.processors.first().keyByIndex(treeNode.depth)
            val (inThisNode, other) = treeNode.processors.partition {
                    processor -> processor.procSpace.count { it == '#' } < treeNode.depth
            }
            return Tree(
                key,
                inThisNode,
                createTreeMap(other, treeNode.depth + 1)
            )
        }

        private fun createTreeMap(other: List<BaseProcessor>, depth: Int) : Map<String, Tree> =
            if (other.isEmpty()) {
                mutableMapOf()
            } else {
                val treeNodes = mutableMapOf<String, TreeNode>()
                other
                    .map { it.keyByIndex(depth) to it }
                    .forEach { pair -> treeNodes.getOrPut(pair.first) { TreeNode(
                        depth,
                        mutableListOf()
                    ) }.processors.add(pair.second) }
                treeNodes.map { entry ->
                    entry.key to createTree(entry.value)
                }.toMap()
            }
    }

    class TreeNode(
        val depth: Int,
        val processors: MutableList<BaseProcessor>
    )
}