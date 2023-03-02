package net.youssfi.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.youssfi.stubs.BankServiceGrpc;
import net.youssfi.stubs.Ebank;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GrpcClient4 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",9999)
                .usePlaintext()
                .build();

        BankServiceGrpc.BankServiceStub asyncStub=BankServiceGrpc.newStub(managedChannel);


        StreamObserver<Ebank.ConvertCurrencyRequest> streamObserver = asyncStub.fullStream(new StreamObserver<Ebank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Ebank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("=============");
                System.out.println(convertCurrencyResponse);
                System.out.println("============");
            }
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("FIN");
            }
        });

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {
                Ebank.ConvertCurrencyRequest request= Ebank.ConvertCurrencyRequest.newBuilder()
                        .setAmount(Math.random()*5000)
                        .build();
                streamObserver.onNext(request);
                ++counter;
                System.out.println("Counter = "+counter);
                if(counter==10){
                    streamObserver.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);

        System.in.read();
    }
}
