package com.testingzone.neo4j.trace.http

import cats.effect.MonadCancel
import com.testingzone.neo4j.trace.Trace.TraceId
import com.testingzone.neo4j.trace.TraceProvider
import com.testingzone.neo4j.trace.http.HttpTraceInterceptor.headerName
import sttp.tapir.model.ServerRequest
import sttp.tapir.server.interceptor.{EndpointInterceptor, RequestHandler, RequestInterceptor, Responder}

class HttpTraceInterceptor[F[_] : TraceProvider : MonadCancel[*[_], Throwable]] extends RequestInterceptor[F] {


  private def maybeTraceId(request: ServerRequest): Option[TraceId] = request.header(headerName).map(TraceId.apply)

  override def apply[R, B](
                            responder: Responder[F, B],
                            requestHandler: EndpointInterceptor[F] => RequestHandler[F, R, B]
                          ): RequestHandler[F, R, B] =
    RequestHandler.from { case (request, endpoints, monad) =>
      TraceProvider[F]
        .resource(maybeTraceId(request))
        .use(_ => requestHandler(EndpointInterceptor.noop)(request, endpoints)(monad))
    }
}

object HttpTraceInterceptor {

  val headerName = "X-TRACE-ID"

  def apply[F[_] : TraceProvider : MonadCancel[*[_], Throwable]]: HttpTraceInterceptor[F] = new HttpTraceInterceptor[F]
}