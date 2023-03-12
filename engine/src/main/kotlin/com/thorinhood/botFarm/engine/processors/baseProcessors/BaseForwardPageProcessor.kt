package com.thorinhood.botFarm.engine.processors.baseProcessors

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistory
import com.thorinhood.botFarm.engine.messages.HasProcSpace
import com.thorinhood.botFarm.engine.messages.HasSessionId
import com.thorinhood.botFarm.engine.pagination.BasePageStrategy
import com.thorinhood.botFarm.engine.processors.Processor

//abstract class BaseForwardPageProcessor<T, IR, IS>(
//    private val basePageStrategy: BasePageStrategy<T>
//) : Processor<IR, IS> where IR: HasProcSpace, IR: HasSessionId{
//
//    override fun process(message: IR, transitionsHistory: TransitionsHistory): List<IS> {
//        TODO("Not yet implemented")
//    }
//
//    override fun isThisProcessor(message: IR): Boolean {
//        TODO("Not yet implemented")
//    }
//
////    override fun processInner(
////        session: Session,
////        update: Update
////    ): ProcessResult = ProcessResult(
////        update.callbackQuery()?.let { callbackQuery ->
////            basePageStrategy.paginate(callbackQuery, session, PaginationType.FORWARD)
////        } ?: listOf()
////    )
////
////    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
////        update.callbackQuery()?.let { callbackQuery ->
////            callbackQuery.data()
////                .split("_")[0] == LABEL
////        } ?: false
//
//    companion object {
//        const val LABEL = "forward"
//    }
//}