/*
   Copyright (c) 2012 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/* $Id$ */
package test.r2.perf.server;

import com.linkedin.common.callback.Callback;
import com.linkedin.r2.message.RequestContext;
import com.linkedin.r2.message.rest.RestRequest;
import com.linkedin.r2.message.rest.RestResponse;
import com.linkedin.r2.sample.echo.EchoService;
import com.linkedin.r2.sample.echo.EchoServiceImpl;
import com.linkedin.r2.sample.echo.rest.RestEchoServer;
import com.linkedin.r2.transport.common.RestRequestHandler;
import com.linkedin.r2.transport.common.Server;
import com.linkedin.r2.transport.common.bridge.server.TransportDispatcher;
import com.linkedin.r2.transport.common.bridge.server.TransportDispatcherBuilder;

import java.net.URI;
import test.r2.perf.Generator;
import test.r2.perf.StringGenerator;


/**
 * @author Chris Pettitt
 * @version $Revision$
 */
public abstract class AbstractPerfServerFactory
{
  public Server create(int port, URI echoUri, int msg_size)
  {

    final TransportDispatcher dispatcher = new TransportDispatcherBuilder()
          .addRestHandler(echoUri, new RestEchoServer(new PerfServiceImpl(msg_size)))
          .build();

    return createServer(port, dispatcher);
  }

  protected abstract Server createServer(int port, TransportDispatcher dispatcher);

  public class PerfServiceImpl implements EchoService
  {
    private final Generator<String> _stringGenerator;

    public PerfServiceImpl(int msg_size)
    {
      _stringGenerator = new StringGenerator(msg_size);
    }

    @Override
    public void echo(String msg, Callback<String> callback)
    {
      callback.onSuccess(_stringGenerator.nextMessage());
    }
  }
}
