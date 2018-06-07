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

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link WalletServer}.
 */
public class WalletClient {
  private static final Logger logger = Logger.getLogger(WalletClient.class.getName());

  private final ManagedChannel channel;
  private final WalletGrpc.WalletBlockingStub blockingStub;

  /** Construct client connecting to HelloWorld server at {@code host:port}. */
  public WalletClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true)
        .build());
  }

  /** Construct client for accessing RouteGuide server using the existing channel. */
  WalletClient(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = WalletGrpc.newBlockingStub(channel);
    logger.info("Client started");
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /** Deposit */
  public void deposit(String user, double amount, String currency) {
    logger.info("Will try to greet " + user + " ...");
    WalletRequest request = WalletRequest.newBuilder().setUser(user).setAmount(amount).setCurrency(currency).build();
    WalletResponse response;
    try {
      response = blockingStub.deposit(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }

    logger.info("Deposit: " + response.getMessage());
  }

  /** Withdraw */
  public String withdraw(String user, double amount, String currency)  {
    logger.info("Will try to greet " + user + " ...");
    WalletRequest request = WalletRequest.newBuilder().setUser(user).setAmount(amount).setCurrency(currency).build();
    WalletResponse response;
    try {
      response = blockingStub.withdraw(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return null;
    }

    logger.info("Deposit: " + response.getMessage());
    return response.getMessage();
  }

    /** Balance */
    public Map<String,Double> getBalance(String user)  {
        logger.info("Will try to greet " + user + " ...");
        BalanceRequest request = BalanceRequest.newBuilder().setUser(user).build();
        BalanceResponse response;
        Map<String,Double> balances = new HashMap<>();
        try {
            response = blockingStub.balance(request);
            balances.put("EUR",response.getBalanceEUR());
            balances.put("GBR",response.getBalanceGBR());
            balances.put("USD",response.getBalanceUSD());
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }

        return balances;
    }

  public static void main(String[] args) throws Exception {
    WalletClient client = new WalletClient("localhost", 50051);
    try {
      /* Access a service running on the local machine on port 50051 */
      String user = "world";
      if (args.length > 0) {
        user = args[0]; /* Use the arg as the name to greet if provided */
      }
    } finally {
      client.shutdown();
    }
  }
}
