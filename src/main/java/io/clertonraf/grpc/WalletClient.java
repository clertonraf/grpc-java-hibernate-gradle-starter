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

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
      logger.info("user "+ user + " will try to deposit " + amount+currency);
    WalletRequest request = WalletRequest.newBuilder().setUser(user).setAmount(amount).setCurrency(currency).build();
    WalletResponse response;
    try {
      response = blockingStub.deposit(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }

    logger.info("Deposit status: " + response.getMessage());
  }

  /** Withdraw */
  public String withdraw(String user, double amount, String currency)  {
    logger.info("user "+ user + " will try to withdraw " + amount+currency);
    WalletRequest request = WalletRequest.newBuilder().setUser(user).setAmount(amount).setCurrency(currency).build();
    WalletResponse response;
    try {
      response = blockingStub.withdraw(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return null;
    }

    logger.info("Withdraw status: " + response.getMessage());
    return response.getMessage();
  }

    /** Balance */
    public Map<String,Double> getBalance(String user)  {
        BalanceRequest request = BalanceRequest.newBuilder().setUser(user).build();
        BalanceResponse response;
        Map<String,Double> balances = new HashMap<>();
        try {
            response = blockingStub.balance(request);
            balances.put("EUR",response.getBalanceEUR());
            balances.put("GBP",response.getBalanceGBP());
            balances.put("USD",response.getBalanceUSD());

            balances.keySet().stream().forEach(e -> logger.info("Balance: "+balances.get(e)+e));

        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }

        return balances;
    }

    public void roundA(String user) {
        this.deposit(user,100.0,"USD");
        this.withdraw(user,200.0,"EUR");
        this.deposit(user,100.0,"EUR");
        this.getBalance(user);
        this.withdraw(user,100.0, "USD");
        this.getBalance(user);
        this.withdraw(user,100.0,"USD");
    }

    public void roundB(String user) {
        this.withdraw(user,100.0,"GBP");
        this.deposit(user,300.0,"GBP");
        this.withdraw(user,100.0, "GBP");
        this.withdraw(user,100.0,"GBP");
        this.withdraw(user,100.0,"GBP");
    }

    public void roundC(String user) {

        this.getBalance(user);
        this.deposit(user,100.0,"USD");
        this.deposit(user,100.0,"USD");
        this.withdraw(user,100.0,"USD");
        this.deposit(user,100.0, "USD");
        this.getBalance(user);
        this.withdraw(user,200.0,"USD");
        this.getBalance(user);
    }

    public void executeRounds(String user, int numRounds) {
        for(int i=0;i<numRounds;i++){
            int round = new Random().nextInt(3);
            switch(round) {
                case 0:
                    logger.info(Thread.currentThread().getId()+" - User "+ user + " at Round A");
                    this.roundA(user);
                    break;
                case 1:
                    logger.info(Thread.currentThread().getId()+" - User "+ user + " at Round B");
                    this.roundB(user);
                    break;
                default:
                    logger.info(Thread.currentThread().getId()+" - User "+ user + " at Round C");
                    this.roundC(user);
            }


        }
    }

    public void runConcurrentThreadsPerUser(String user, int numThreads, int numRounds) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for(int i=0;i<numThreads;i++) {
            executor.execute(
                    () -> {
                        logger.info(Thread.currentThread().getId()+"");
                        this.executeRounds(user,numRounds);
                    }
            );

        }
        executor.shutdown();
        try {
            if(executor.awaitTermination(Integer.MAX_VALUE,TimeUnit.SECONDS)) {
                logger.info("END ROUNDS");

            }
        } catch(InterruptedException ie){
            logger.log(Level.SEVERE,"Couldn't stop thread",ie);
        }
    }

    public void runConcurrentUsers(final int numUsers, final int numThreads, final int numRounds) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for(int i=0;i<numUsers;i++) {
            final int user = i;
            executor.execute(
                    () -> {
                        logger.info(Thread.currentThread().getId()+"");
                        this.runConcurrentThreadsPerUser(Integer.toString(user), numThreads,numRounds);
                    }
            );

        }
        executor.shutdown();
        try {
            if(executor.awaitTermination(Integer.MAX_VALUE,TimeUnit.SECONDS)) {
                logger.info("END USERS");
            }
        } catch(InterruptedException ie){
            logger.log(Level.SEVERE,"Couldn't stop thread",ie);
        }

    }

    private static void inputCliValidation(String ...args) {
        if(args.length !=3) {
            logger.info(
                    "Please, check the arguments.\n " +
                            "users (number of concurrent users emulated),\n " +
                            "concurrent_threads_per_user (number of concurrent requests a user will make),\n " +
                            "rounds_per_thread (number of rounds each thread is executing)\n \n " +
                            "valid example: java -jar build/libs/grpc-java-hibernate-gradle-starter-client-1.0-SNAPSHOT.jar 5 4 3"
            );
            System.exit(0);
        } else {
            for(String s: args) {
                try {
                    int num = Integer.valueOf(s);
                    if(num < 0) {
                        logger.info(
                                "Please, check the arguments.\n " +
                                        "At least one of them is not a valid POSITIVE integer.\n \n"+
                                        "valid example: java -jar build/libs/grpc-java-hibernate-gradle-starter-client-1.0-SNAPSHOT.jar 5 4 3"
                        );
                        System.exit(0);
                    }
                } catch(NumberFormatException nfe) {
                    logger.info(
                            "Please, check the arguments.\n " +
                                    "At least one of them is not a valid integer.\n \n"+
                                    "valid example: java -jar build/libs/grpc-java-hibernate-gradle-starter-client-1.0-SNAPSHOT.jar 5 4 3"
                    );
                    System.exit(0);
                }

            }
        }
    }

  public static void main(String ...args) throws Exception {

      inputCliValidation(args);

      WalletClient client = new WalletClient("localhost", 50051);

      /* Access a service running on the local machine on port 50051 */
      client.runConcurrentUsers(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));   }
}
