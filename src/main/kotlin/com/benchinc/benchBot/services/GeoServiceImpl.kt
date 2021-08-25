package com.benchinc.benchBot.services

import com.benchinc.benchBot.data.Bench
import com.benchinc.benchBot.geo.QueryElementsHandler
import com.peertopark.java.geocalc.DegreeCoordinate
import com.peertopark.java.geocalc.EarthCalc
import com.peertopark.java.geocalc.Point
import com.pengrad.telegrambot.model.Location
import de.westnordost.osmapi.map.data.LatLon
import de.westnordost.osmapi.map.data.Node
import de.westnordost.osmapi.map.data.OsmLatLon
import de.westnordost.osmapi.overpass.OverpassMapDataApi
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class GeoServiceImpl(val overpassMapDataApi: OverpassMapDataApi) : GeoService, Logging {

    override fun findBenchById(id: Long) : Node? {
        val bench = QueryElementsHandler()
        try {
            overpassMapDataApi.queryElements("node($id)[amenity=bench];out;", bench)
        } catch(e : Exception) {
            logger.error(e)
            return null
        }
        if (bench.getElements().isEmpty() || bench.getElements().size > 1) {
            return null
        }
        return bench.getElements()[0]
    }

    override fun findBenches(around : Location, radius: Int) : List<Bench> {
        val benchesHandler = QueryElementsHandler()
        try {
            overpassMapDataApi.queryElements(
                "node(around: $radius, ${around.latitude()}, ${around.longitude()})" +
                        "[amenity=bench];out;", benchesHandler
            )
        } catch(e : Exception) {
            logger.error(e)
            return listOf()
        }
        val center = OsmLatLon(around.latitude().toDouble(), around.longitude().toDouble())
        return benchesHandler.getElements()
            .map { node -> Bench(node, distance(center, node.position)) }
            .sortedBy { it.distance }
    }

    private fun distance(from: LatLon, to: LatLon) : Double {
        return EarthCalc.getDistance(convert(from), convert(to))
    }

    private fun convert(latLon: LatLon) : Point {
        return Point(DegreeCoordinate.build(latLon.latitude), DegreeCoordinate.build(latLon.longitude))
    }

}