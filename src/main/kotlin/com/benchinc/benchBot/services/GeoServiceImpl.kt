package com.benchinc.benchBot.services

import com.db.benchLib.clients.BenchesServiceClient
import com.db.benchLib.data.Bench
import com.peertopark.java.geocalc.DegreeCoordinate
import com.peertopark.java.geocalc.EarthCalc
import com.peertopark.java.geocalc.Point
import com.pengrad.telegrambot.model.Location
import de.westnordost.osmapi.map.data.LatLon
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class GeoServiceImpl(val benchesServiceClient: BenchesServiceClient) : GeoService, Logging {

    override fun findBenchById(id: String) : Bench {
        return benchesServiceClient.findBenchById(id)
    }

    override fun findBenches(around : Location, radius: Int) : List<Bench> {
        return benchesServiceClient.findBenchesNear(
            around.longitude().toDouble(),
            around.latitude().toDouble(),
            radius.toDouble()/1000)
    }

    private fun distance(from: LatLon, to: LatLon) : Double {
        return EarthCalc.getDistance(convert(from), convert(to))
    }

    private fun convert(latLon: LatLon) : Point {
        return Point(DegreeCoordinate.build(latLon.latitude), DegreeCoordinate.build(latLon.longitude))
    }

}