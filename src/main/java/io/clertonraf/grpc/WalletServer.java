/*
 * Copyright 2015, gRPC Authors All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.clertonraf.grpc;

import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.service.WalletService;
import io.clertonraf.grpc.service.impl.WalletServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class WalletServer {
  private static final Logger logger = Logger.getLogger(WalletServer.class.getName());

  private static final WalletService service = WalletServiceImpl.getInstance();

  private Server server;

  public void start() throws IOException {
    /* The port on which the server should run */
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new WalletImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        WalletServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  public void start(int port) throws IOException {
    /* The port on which the server should run */
    server = ServerBuilder.forPort(port)
            .addService(new WalletImpl())
            .build()
            .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        WalletServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final WalletServer server = new WalletServer();
    server.start();
    server.blockUntilShutdown();
  }

  static class WalletImpl extends WalletGrpc.WalletImplBase {

    @Override
    public void deposit(WalletRequest req, StreamObserver<WalletResponse> responseObserver) {
      WalletResponse reply = WalletResponse
                .newBuilder()
                .setMessage(
                        service.deposit(Integer.parseInt(req.getUser()), BigDecimal.valueOf(req.getAmount()), req.getCurrency())
                )
                .build();

      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

      @Override
      public void withdraw(WalletRequest req, StreamObserver<WalletResponse> responseObserver) {
          WalletResponse reply = WalletResponse
                  .newBuilder()
                  .setMessage(
                          service.withdraw(Integer.parseInt(req.getUser()), BigDecimal.valueOf(req.getAmount()), req.getCurrency())
                  )
                  .build();
          responseObserver.onNext(reply);
          responseObserver.onCompleted();
      }

      public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
          Map<String, BigDecimal> response = service.getBalance(Integer.parseInt(request.getUser()));
        BalanceResponse reply = BalanceResponse
                   .newBuilder()
                   .setBalanceEUR(response.get(Currency.EUR.name()).doubleValue())
                   .setBalanceGBR(response.get(Currency.GBR.name()).doubleValue())
                .setBalanceUSD(response.get(Currency.USD.name()).doubleValue())
                .build();

           responseObserver.onNext(reply);
           responseObserver.onCompleted();
      }
  }
}
