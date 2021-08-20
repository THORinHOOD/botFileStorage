package com.benchinc.benchBot.geo

import de.westnordost.osmapi.map.data.BoundingBox
import de.westnordost.osmapi.map.data.Node
import de.westnordost.osmapi.map.data.Relation
import de.westnordost.osmapi.map.data.Way
import de.westnordost.osmapi.map.handler.MapDataHandler

class QueryElementsHandler : MapDataHandler {

    private val elements = mutableListOf<Node>()

    override fun handle(bounds: BoundingBox?) {
    }

    override fun handle(node: Node?) {
        node?.let {
            elements.add(node)
        }
    }

    override fun handle(way: Way?) {
    }

    override fun handle(relation: Relation?) {
    }

    fun getElements() : List<Node> {
        return elements
    }

}