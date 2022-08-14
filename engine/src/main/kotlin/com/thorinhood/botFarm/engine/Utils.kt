package com.thorinhood.botFarm.engine

import org.apache.logging.log4j.kotlin.KotlinLogger

class Utils {
    companion object {
        fun initLog(logger: KotlinLogger, name: String) {
            logger.info("\n#####################################\n" +
                    "Sessions storage : $name\n" +
                    "#####################################")
        }
    }
}