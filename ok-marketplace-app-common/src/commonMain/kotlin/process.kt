package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper

suspend fun <T> MkplAdProcessor.process(
    logger: IMpLogWrapper,
    logId: String,
    fromTransport: suspend MkplContext.() -> Unit,
    sendResponse: suspend MkplContext.() -> T,
    toLog: MkplContext.(logId: String) -> Any): T {

    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return logger.doWithLogging(id = logId) {
        ctx.fromTransport()
        logger.info(
            msg = "${ctx.command} request is got",
            data = ctx.toLog("${logId}-got")
        )
        exec(ctx)
        logger.info(
            msg = "${ctx.command} request is handled",
            data = ctx.toLog("${logId}-handled")
        )
        ctx.sendResponse()
    }
}
